package iapps.cl.stormy.UI;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Arrays;

import iapps.cl.stormy.Adapters.DayAdapter;
import iapps.cl.stormy.Model.Day;
import iapps.cl.stormy.R;

public class DailyForecastActivity extends ListActivity {

    private Day[] days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        days = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter adapter = new DayAdapter(this, days);
        setListAdapter(adapter);

        String locationValue = intent.getStringExtra("LOCATION_VALUE");
        TextView locationView = (TextView) findViewById(R.id.locationTextView);
        locationView.setText(locationValue);
    }



}
