package es.uji.geonews.controller.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.TimeZone;

import es.uji.geonews.R;
import es.uji.geonews.model.data.HourlyWeather;


public class PrecipitationViewHolder extends RecyclerView.ViewHolder {
    private final TextView hourOutput;
    private final TextView precipitationOutput;

    public PrecipitationViewHolder(View itemView) {
        super(itemView);
        hourOutput = itemView.findViewById(R.id.precipitation_hour_output);
        precipitationOutput = itemView.findViewById(R.id.precipitation_output);
    }

    public void bind(HourlyWeather hourlyWeather) {
        long precipitation = Math.round(hourlyWeather.getPop() * 100);
        hourOutput.setText(getStringTime(hourlyWeather.getTimestamp()) + "");
        precipitationOutput.setText(precipitation + "%");
    }

    private String getStringTime(long timestamp) {
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp * 1000),
                TimeZone.getDefault().toZoneId());
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("HH:mm")
                .toFormatter(new Locale("es", "ES"));
        return date.format(fmt);
    }
}

