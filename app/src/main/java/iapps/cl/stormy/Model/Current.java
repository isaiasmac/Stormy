package iapps.cl.stormy.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import iapps.cl.stormy.R;


/**
 * Created by iSaias on 10/10/15.
 */
public class Current {

    private String town;
    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;

    public String getTimeZone() {

        return timeZone;
    }

    public void setTimeZone(String timeZone) {

        this.timeZone = timeZone;
    }

    private String timeZone;

    public int getIcon() {
        return Forecast.getIconId(this.icon);
    }

    public void setIcon(String icon) {

        this.icon = icon;
    }

    public long getTime() {

        return time;
    }

    public void setTime(long time) {

        this.time = time;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("H:mm");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);

        return timeString;
    }


    public double getTemperature() {

        return temperature;
    }

    public void setTemperature(double temperature) {

        this.temperature = temperature;
    }

    public double getHumidity() {

        return humidity;
    }

    public void setHumidity(double humidity) {

        this.humidity = humidity;
    }

    public double getPrecipChance() {

        return precipChance;
    }

    public void setPrecipChance(double precipChance) {

        this.precipChance = precipChance;
    }

    public String getSummary() {

        return summary;
    }

    public void setSummary(String summary) {

        this.summary = summary;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
