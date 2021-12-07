package es.uji.geonews.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.OpenWeatherForecastData;

public class FiveDaysForecastAdapter extends RecyclerView.Adapter<FiveDaysForecastAdapter.ViewHolder> {
    private List<OpenWeatherForecastData> forecast;

    public FiveDaysForecastAdapter(List<OpenWeatherForecastData> forecast) {
        this.forecast = forecast;
    }

    public void updateForecast(List<OpenWeatherForecastData> forecast) {
        this.forecast = forecast;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView date;
        private final TextView description;
        private final TextView maxTemp;
        private final TextView minTemp;
        private final ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTextview);
            description = itemView.findViewById(R.id.descriptionTextview);
            icon = itemView.findViewById(R.id.icon_layout);
            maxTemp = itemView.findViewById(R.id.maxTempTextview);
            minTemp = itemView.findViewById(R.id.minTempTextview);
        }

        public void bind(OpenWeatherForecastData data) {
            description.setText(data.getDescription());
            maxTemp.setText(String.valueOf(data.getMaxTemp()));
            minTemp.setText(String.valueOf(data.getMinTemp()));
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
        OpenWeatherForecastData data = forecast.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return forecast.size();
    }
}
