package es.uji.geonews.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import es.uji.geonews.R;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.OpenWeatherForecastData;
import es.uji.geonews.model.data.ServiceData;

public class FiveDaysForecastAdapter extends RecyclerView.Adapter<FiveDaysForecastAdapter.ViewHolder> {
    private List<ServiceData> forecast;

    public FiveDaysForecastAdapter(List<ServiceData> forecast) {
        this.forecast = forecast;
    }

    public void updateForecast(List<ServiceData> forecast) {
        this.forecast = forecast;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView date;
        private final TextView description;
        private final TextView avgTemp;
        private final ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTextview);
            description = itemView.findViewById(R.id.descriptionTextview);
            icon = itemView.findViewById(R.id.icon_layout);
            avgTemp = itemView.findViewById(R.id.avgTempTextview);
        }

        public void bind(OpenWeatherForecastData data) {
            date.setText(getStringDateAndTime(data.getTimestamp()));
            String desc = data.getDescription().substring(0, 1).toUpperCase() + data.getDescription().substring(1);
            description.setText(desc);
            avgTemp.setText(Math.round(data.getActTemp()) + "º");
            Picasso.get()
                    .load("https://openweathermap.org/img/wn/" + data.getIcon() + "@2x.png")
                    .into(icon);
        }
    }

    @NonNull
    @Override
    public FiveDaysForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.weather_card,
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiveDaysForecastAdapter.ViewHolder holder, int position) {
        OpenWeatherForecastData data = (OpenWeatherForecastData) forecast.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return forecast.size();
    }

    private String getStringDateAndTime(long timestamp) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp * 1000),
                TimeZone.getDefault().toZoneId());
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("dd MMMM HH:mm")
                .toFormatter(new Locale("es", "ES"));
        return date.format(fmt);
    }
}
