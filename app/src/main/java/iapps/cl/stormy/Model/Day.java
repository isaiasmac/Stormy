package iapps.cl.stormy.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by iSaias on 10/12/15.
 */
public class Day implements Parcelable{

    private long time;
    private String summary;
    private double temperatureMax;
    private String icon;
    private String timeZone;

    public Day(){ }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {

        this.time = time;
    }

    public String getSummary() {

        return summary;
    }

    public void setSummary(String summary) {

        this.summary = summary;
    }

    public int getTemperatureMax() {

        return (int) Math.round(temperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {

        this.temperatureMax = temperatureMax;
    }

    public String getIcon() {

        return icon;
    }

    public int getIconId(){
        return Forecast.getIconId(this.icon);
    }

    public void setIcon(String icon) {

        this.icon = icon;
    }

    public String getTimeZone() {

        return timeZone;
    }

    public void setTimeZone(String timeZone) {

        this.timeZone = timeZone;
    }

    public String getDayOfTheWeek(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(this.timeZone));
        Date dateTime = new Date(this.time * 1000);

        String dayWeek = formatter.format(dateTime);
        return dayWeek.substring(0, 1).toUpperCase() + dayWeek.substring(1); // Capitalize
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.time);
        dest.writeString(this.summary);
        dest.writeDouble(this.temperatureMax);
        dest.writeString(this.icon);
        dest.writeString(this.timeZone);
    }

    private Day(Parcel in){
        this.time = in.readLong();
        this.summary = in.readString();
        this.temperatureMax = in.readDouble();
        this.icon = in.readString();
        this.timeZone = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };


}
