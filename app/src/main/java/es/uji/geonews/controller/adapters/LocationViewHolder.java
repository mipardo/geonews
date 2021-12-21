package es.uji.geonews.controller.adapters;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import es.uji.geonews.R;
import es.uji.geonews.controller.fragments.OnItemClickListener;
import es.uji.geonews.controller.tasks.AddToFavorites;
import es.uji.geonews.controller.tasks.RemoveFromFavorites;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;


public class LocationViewHolder extends RecyclerView.ViewHolder {
    private final TextView mainNameOutput;
    private final TextView subnameOutput;
    private final LottieAnimationView favouriteButton;
    private final ImageView infoButton;

    public LocationViewHolder(View itemView) {
        super(itemView);
        mainNameOutput = itemView.findViewById(R.id.main_name_output);
        subnameOutput = itemView.findViewById(R.id.subname_output);
        favouriteButton = itemView.findViewById(R.id.add_to_favorites_button);
        infoButton = itemView.findViewById(R.id.location_information_button);
    }

    public void bind(Location location, OnItemClickListener listener) {
        setLocationTitleAndSubtitle(location);
        if (location.isFavorite()) {
            favouriteButton.setVisibility(View.VISIBLE);
            favouriteButton.playAnimation();
            //favouriteButton.setImageResource(R.drawable.heart_fully);
        } else if(location.isActive()) {

            favouriteButton.setVisibility(View.VISIBLE);
        } else {
            favouriteButton.setVisibility(View.INVISIBLE);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(location);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
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

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.isFavorite()){
                    new RemoveFromFavorites(itemView.getContext(), location, favouriteButton).execute();
                } else {

                    new AddToFavorites(itemView.getContext(), location, favouriteButton).execute();
                }
            }
        });
    }

    private void setLocationTitleAndSubtitle(Location location){
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
    }
}

