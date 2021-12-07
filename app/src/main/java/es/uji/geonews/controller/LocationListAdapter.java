package es.uji.geonews.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.controller.fragments.OnItemClickListener;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private List<Location> locations;
    private final OnItemClickListener listener;

    public LocationListAdapter(List<Location> locations, OnItemClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    public void updateLocations(List<Location> locations) {
        this.locations = locations;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mainNameOutput;
        private final TextView subnameOutput;
        private final FloatingActionButton favourite;
        private final FloatingActionButton info;

        public ViewHolder(View itemView) {
            super(itemView);
            mainNameOutput = itemView.findViewById(R.id.main_name_output);
            subnameOutput =  itemView.findViewById(R.id.subname_output);
            favourite =  itemView.findViewById(R.id.add_to_favorites_button);
            info =  itemView.findViewById(R.id.location_information_button);
        }

        public void bind(Location location, OnItemClickListener listener) {
            String mainName;
            String subname;
            if (! location.getAlias().equals("")) {     // If location has alias
                mainName = location.getAlias();
                if (location.getPlaceName() != null) subname = location.getPlaceName();
                else subname = location.getGeographCoords().toString();
            } else {                                    // If location has no alias
                if (location.getPlaceName() != null) {
                    mainName = location.getPlaceName();
                    subname = location.getGeographCoords().toString();
                }
                else{
                    mainName = location.getGeographCoords().toString();
                    subname = "Topónimo desconocido";
                }
            }
            mainNameOutput.setText(mainName);
            subnameOutput.setText(subname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(location);
                }
            });

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("locationId", location.getId());
                    NavController navController = Navigation.findNavController(view);
                    GeoNewsManager geoNewsManager = GeoNewsManagerSingleton.getInstance(view.getContext());
                    try {
                        if(geoNewsManager.getLocation(location.getId()).isActive()){
                            navController.navigate(R.id.activeLocationInfoFragment, bundle);
                        } else {
                            navController.navigate(R.id.nonActiveLocationInfoFragment, bundle);
                        }
                    } catch (NoLocationRegisteredException e) {
                        e.printStackTrace();
                    }
                }
            });
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
        holder.bind(location, listener);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }



}
