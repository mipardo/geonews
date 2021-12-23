package es.uji.geonews.controller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.model.data.HourlyWeather;

public class PrecipitationListAdapter extends RecyclerView.Adapter<PrecipitationViewHolder> {
    private List<HourlyWeather> prepitations;

    public PrecipitationListAdapter(List<HourlyWeather> prepitations) {
        this.prepitations = prepitations;
    }

    public void updatePrecipitations(List<HourlyWeather> prepitations) {
        this.prepitations = prepitations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PrecipitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.precipitation_card,
                parent,
                false
        );
        return new PrecipitationViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull PrecipitationViewHolder holder, int position) {
        HourlyWeather hourlyWeather = prepitations.get(position);
        holder.bind(hourlyWeather);
    }

    @Override
    public int getItemCount() {
        return prepitations.size();
    }




}
