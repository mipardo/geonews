package es.uji.geonews.controller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.model.data.DailyWeather;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> {
    private List<DailyWeather> forecast;

    public ForecastAdapter(List<DailyWeather> forecast) {
        this.forecast = forecast;
    }

    public void updateForecast(List<DailyWeather> forecast) {
        this.forecast = forecast;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.weather_card,
                parent,
                false
        );
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        holder.bind(forecast.get(position));
    }

    @Override
    public int getItemCount() {
        return forecast.size();
    }
}
