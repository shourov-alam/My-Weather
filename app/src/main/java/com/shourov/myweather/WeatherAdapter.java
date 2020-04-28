package com.shourov.myweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    Context context;
    List<Day> days;

    WeatherAdapter(Context context, List<Day> days){

        this.context=context;
        this.days=days;

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =LayoutInflater.from(context);

                                   View view=    layoutInflater.inflate(R.layout.sample_view,parent,false);

                                       MyViewHolder myViewHolder = new MyViewHolder(view);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       // days.get(position).getDt();
        Date date = new Date((long)(days.get(position).getDt())*1000);
        String dates = DateFormat.getDateTimeInstance().format(date);

        holder.date.setText(dates);
        holder.sky.setText(days.get(position).getWeather().get(0).getDescription());
        holder.wind.setText(days.get(position).getWind().getSpeed().toString()+" Km/h");
        holder.pressure.setText(days.get(position).getMain().getPressure().toString()+" mbar");
        holder.humidity.setText(days.get(position).getMain().getHumidity().toString()+"%");
        holder.temp.setText(convert(days.get(position).getMain().getTemp())+"°C");
       // holder.temp.setText(days.get(position).getMain().getTemp().toString());
        holder.max.setText(convert(days.get(position).getMain().getTempMax())+"°C");
        holder.min.setText(convert(days.get(position).getMain().getTempMin())+"°C");



    }

    public String convert(double tmp) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        String temp_convert=decimalFormat.format(tmp-273.15);

        return temp_convert;


    }
   /* @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    } */

    @Override
    public int getItemCount() {
        return days.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date,sky,wind,pressure,humidity,temp,max,min;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

         date=   itemView.findViewById(R.id.date);
            sky=   itemView.findViewById(R.id.sky);
            wind=   itemView.findViewById(R.id.wind);
            pressure=   itemView.findViewById(R.id.pressure);
            humidity=   itemView.findViewById(R.id.humidity);
            temp=   itemView.findViewById(R.id.current_temp);
            max=   itemView.findViewById(R.id.high);
            min=   itemView.findViewById(R.id.low);


        }
    }
}
