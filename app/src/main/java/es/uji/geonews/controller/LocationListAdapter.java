package es.uji.geonews.controller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
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
        private final TextView mainNameOutput;
        private final TextView subnameOutput;

        public ViewHolder(View itemView) {
            super(itemView);
            mainNameOutput = itemView.findViewById(R.id.main_name_output);
            subnameOutput =  itemView.findViewById(R.id.subname_output);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(view).navigate(R.id.action_locationListFragment_to_locationFragment);
                    Log.e("asd", "dasdasd");
                    Toast.makeText(view.getContext(), "dsadasd", Toast.LENGTH_LONG).show();
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
        holder.mainNameOutput.setText(mainName);
        holder.subnameOutput.setText(subname);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }



}
