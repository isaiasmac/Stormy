package iapps.cl.stormy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import iapps.cl.stormy.Model.Day;
import iapps.cl.stormy.R;

/**
 * Created by iSaias on 10/12/15.
 */
public class DayAdapter extends BaseAdapter {

    private Context context;
    private Day[] days;


    public DayAdapter(Context context, Day[] days){
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return this.days.length;
    }

    @Override
    public Object getItem(int position) {
        return this.days[position];
    }

    @Override
    public long getItemId(int position) {
        //return days[position].getTime();
        return 0; // not used
    }


    // Similar CellForRowAtIndexPath :)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Day day = days[position];

        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax() + "");

        if (position == 0){
            holder.dayLabel.setText("Hoy");
        }
        else{
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }

        return convertView;
    }

    private static class ViewHolder{
        ImageView iconImageView; // public by default
        TextView temperatureLabel;
        TextView dayLabel;
    }
}
