package iapps.cl.stormy.Model;

import iapps.cl.stormy.R;

/**
 * Created by iSaias on 10/12/15.
 */
public class Forecast {

    private Current current;
    private Hour[] hour;
    private Day[] day;

    public Current getCurrent() {

        return current;
    }

    public void setCurrent(Current current) {

        this.current = current;
    }

    public Hour[] getHour() {

        return hour;
    }

    public void setHour(Hour[] hour) {

        this.hour = hour;
    }

    public Day[] getDay() {

        return day;
    }

    public void setDay(Day[] day) {

        this.day = day;
    }


    public static  int getIconId(String iconString){
        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night.

        int iconId = R.drawable.clear_day;

        if (iconString.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (iconString.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (iconString.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (iconString.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (iconString.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (iconString.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (iconString.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (iconString.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }
}
