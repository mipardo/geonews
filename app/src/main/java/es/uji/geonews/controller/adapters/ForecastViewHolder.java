package es.uji.geonews.controller.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.TimeZone;

import es.uji.geonews.R;
import es.uji.geonews.model.data.DailyWeather;

public class ForecastViewHolder extends RecyclerView.ViewHolder {
    private final TextView date;
    private final TextView description;
    private final TextView temp;
    private final ImageView icon;
    private final TextView maxTemp;
    private final TextView minTemp;

    public ForecastViewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.dateTextview);
        description = itemView.findViewById(R.id.descriptionTextview);
        icon = itemView.findViewById(R.id.icon_layout);
        temp = itemView.findViewById(R.id.avgTempTextview);
        maxTemp = itemView.findViewById(R.id.max_temp_output);
        minTemp = itemView.findViewById(R.id.min_temp_output);
    }

    public void bind(DailyWeather data) {
        date.setText(getStringDateAndTime(data.getTimestamp()));
        String desc = data.getDescription().substring(0, 1).toUpperCase() + data.getDescription().substring(1);
        description.setText(desc);
        temp.setText(Math.round(data.getCurrentTemp()) + "º");
        Picasso.get()
                .load("https://openweathermap.org/img/wn/" + data.getIcon() + "@2x.png")
                .into(icon);
        minTemp.setText(Math.round(data.getTempMin()) + "º");
        maxTemp.setText(Math.round(data.getTempMax()) + "º");
    }

    private String getStringDateAndTime(long timestamp) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp * 1000),
                TimeZone.getDefault().toZoneId());
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("dd MMMM")
                .toFormatter(new Locale("es", "ES"));
        return date.format(fmt);
    }
}
