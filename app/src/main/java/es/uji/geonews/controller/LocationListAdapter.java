package es.uji.geonews.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.model.Location;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private final List<Location> locations;

    public LocationListAdapter(List<Location> locations) {
        this.locations = locations;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView placeNameOutput;
        private TextView coordsOutput;

        public ViewHolder(View itemView) {
            super(itemView);
            placeNameOutput = itemView.findViewById(R.id.place_name_output);
            coordsOutput =  itemView.findViewById(R.id.coords_output);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.location_card,
                parent,
                false
        );
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.placeNameOutput.setText(location.getPlaceName());
        holder.coordsOutput.setText(location.getGeographCoords().toString());
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }



}
