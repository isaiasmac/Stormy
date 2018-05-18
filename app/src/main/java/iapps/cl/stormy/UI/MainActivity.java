package iapps.cl.stormy.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import iapps.cl.stormy.Model.Current;
import iapps.cl.stormy.Model.Day;
import iapps.cl.stormy.Model.Forecast;
import iapps.cl.stormy.Model.Hour;
import iapps.cl.stormy.R;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";

    private Forecast forecast;

    private String forecastURL = "https://api.forecast.io/forecast/";
    private String forecastKey = "57d5ae8abaa86bc1a9419156699635e9";
    private double latitude = 0.0;
    private double longitude = 0.0;

    private TextView locationTextView;
    private TextView temperatureTextView;
    private TextView timeTextView;
    private TextView humedadTextView;
    private TextView lluviaTextView;
    private TextView summaryTextView;
    private ImageView iconImageView;
    private Button dailyButton;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    public void getForecastInfo(){
        // https://api.forecast.io/forecast/57d5ae8abaa86bc1a9419156699635e9/-33.3904261,-70.6750224

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(forecastURL+forecastKey+"/"+latitude+","+longitude+"?units=si")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("Forecast Call Failure: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {

                        String jsonResponse = response.body().string();
                        forecast = parseForecastDetails(jsonResponse);
                        fillLabelsData();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        locationTextView = (TextView) findViewById(R.id.locationTextView);

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {
            Log.d(TAG, location.toString());
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();

            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
            StringBuilder builder = new StringBuilder();

            try {
                List<Address> address = geoCoder.getFromLocation(this.latitude, this.longitude, 1);
                Log.i(TAG, "address: "+address);

                int maxLines = address.get(0).getMaxAddressLineIndex();
                for (int i = 0; i < maxLines; i++) {
                    String addressStr = address.get(0).getAddressLine(i);

                    if (i >= 1){
                        builder.append(addressStr);
                        builder.append(" ");
                    }
                }

                final String finalAddress = builder.toString();
                Log.i(TAG, "finalAddress: " + finalAddress);
                locationTextView.setText(finalAddress);

                getForecastInfo();

            }
            catch (IOException e) {

            }
            catch (NullPointerException e) {

            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // 60.06484046, 8.61328125 | Norway
        // 64.9794, -18.8525 | Iceland
        // 53.48477703, -2.24601746 | Manchester
        this.latitude = 53.48477703;
        this.longitude = -2.24601746;
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        locationTextView.setText("Manchester, England");

        getForecastInfo();

        Log.i(TAG, "Location services Failed. Please reconnect.");
    }


    private void fillLabelsData() {
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        humedadTextView =  (TextView) findViewById(R.id.humedadTextView);
        lluviaTextView = (TextView) findViewById(R.id.lluviaTextView);
        summaryTextView = (TextView) findViewById(R.id.summaryTextView);
        iconImageView = (ImageView) findViewById(R.id.iconImageView);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Current current = forecast.getCurrent();
                int intTemperature = (int) current.getTemperature();
                temperatureTextView.setText(String.valueOf(intTemperature + "°"));
                timeTextView.setText("A las " + current.getFormattedTime() + " hrs.");
                humedadTextView.setText(current.getHumidity() + "%");
                lluviaTextView.setText(current.getPrecipChance() + "%");
                summaryTextView.setText(current.getSummary());

                //Drawable drawable = getResources().getDrawable(current.getIcon()); // deprecated
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), current.getIcon(), null);
                iconImageView.setImageDrawable(drawable);
            }
        });
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException{
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrent(jsonData));
        forecast.setDay(getDaily(jsonData));
        forecast.setHour(getHourly(jsonData));
        return forecast;
    }

    private Hour[] getHourly(String jsonData) throws JSONException{
        JSONObject dataJSON = new JSONObject(jsonData);
        String timeZone = dataJSON.getString("timezone");
        JSONObject hourlyJSON = dataJSON.getJSONObject("hourly");
        JSONArray data = hourlyJSON.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i = 0; i < data.length(); i++){
            JSONObject hourJSON = data.getJSONObject(i);

            Hour hour = new Hour();
            hour.setIcon(hourJSON.getString("icon"));
            hour.setSummary(hourJSON.getString("summary"));
            hour.setTemperature(hourJSON.getDouble("temperature"));
            hour.setTime(hourJSON.getLong("time"));
            hour.setTimezone(timeZone);

            hours[i] = hour;
        }

        return hours;
    }

    private Day[] getDaily(String jsonData) throws JSONException{
        JSONObject dataJSON = new JSONObject(jsonData);
        String timeZone = dataJSON.getString("timezone");
        JSONObject dailyJSON = dataJSON.getJSONObject("daily");
        JSONArray data = dailyJSON.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for (int i = 0; i < data.length(); i++){
            JSONObject daysJSON = data.getJSONObject(i);

            Day day = new Day();
            day.setIcon(daysJSON.getString("icon"));
            day.setTime(daysJSON.getLong("time"));
            day.setSummary(daysJSON.getString("summary"));
            day.setTemperatureMax(daysJSON.getDouble("temperatureMax"));
            day.setTimeZone(timeZone);

            days[i] = day;
        }

        return days;
    }


    private Current getCurrent(String jsonResponse) throws JSONException{

        JSONObject dataJSON = new JSONObject(jsonResponse);
        JSONObject currentlyJSON = dataJSON.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currentlyJSON.getDouble("humidity"));
        current.setIcon(currentlyJSON.getString("icon"));
        current.setTemperature(currentlyJSON.getDouble("temperature"));
        current.setTime(currentlyJSON.getLong("time"));
        current.setSummary(currentlyJSON.getString("summary"));
        current.setPrecipChance(currentlyJSON.getDouble("precipProbability"));
        current.setTimeZone(dataJSON.getString("timezone"));

        return current;
    }

    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, this.forecast.getDay());
        intent.putExtra("LOCATION_VALUE", this.locationTextView.getText());
        startActivity(intent);
    }
}
