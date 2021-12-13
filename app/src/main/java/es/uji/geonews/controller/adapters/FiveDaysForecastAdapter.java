package es.uji.geonews.controller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.model.data.OpenWeatherForecastData;
import es.uji.geonews.model.data.ServiceData;

public class FiveDaysForecastAdapter extends RecyclerView.Adapter<FiveDaysForecastViewHolder> {
    private List<ServiceData> forecast;

    public FiveDaysForecastAdapter(List<ServiceData> forecast) {
        this.forecast = forecast;
    }

    public void updateForecast(List<ServiceData> forecast) {
        this.forecast = forecast;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FiveDaysForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.weather_card,
                parent,
                false
        );
        return new FiveDaysForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiveDaysForecastViewHolder holder, int position) {
        OpenWeatherForecastData data = (OpenWeatherForecastData) forecast.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return forecast.size();
    }
}
