package es.uji.geonews.controller.adapters;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;


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
    private final TextView countryOutput;
    private final ImageView countryFlag;
    private final LottieAnimationView favouriteButton;
    private final ImageView infoButton;

    public LocationViewHolder(View itemView) {
        super(itemView);
        mainNameOutput = itemView.findViewById(R.id.main_name_output);
        subnameOutput = itemView.findViewById(R.id.subname_output);
        favouriteButton = itemView.findViewById(R.id.add_to_favorites_button);
        infoButton = itemView.findViewById(R.id.location_information_button);
        countryOutput = itemView.findViewById(R.id.country_output);
        countryFlag = itemView.findViewById(R.id.flag_output);
    }

    public void bind(Location location, OnItemClickListener listener) {
        mainNameOutput.setText(location.getMainName());
        subnameOutput.setText(location.getSubName());
        countryOutput.setText(location.getCountry().getName());
        Picasso.get()
                .load("https://flagcdn.com/60x45/" + location.getCountry().getCode().toLowerCase() + ".png")
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.missing_flag)
                .into(countryFlag, null);
        if (location.isFavourite()) {
            favouriteButton.setVisibility(View.VISIBLE);
            favouriteButton.setProgress(1);
        } else if(location.isActive()) {
            favouriteButton.setVisibility(View.VISIBLE);
            favouriteButton.setProgress(0);
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
                        navController.navigate(R.id.action_locationListFragment_to_activeLocationInfoFragment, bundle);
                    } else {
                        navController.navigate(R.id.action_locationListFragment_to_nonActiveLocationInfoFragment, bundle);
                    }
                } catch (NoLocationRegisteredException e) {
                    e.printStackTrace();
                }
            }
        });

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.isFavourite()){
                    new RemoveFromFavorites(itemView.getContext(), location, favouriteButton).execute();
                } else {
                    new AddToFavorites(itemView.getContext(), location, favouriteButton).execute();
                }
            }
        });
    }
}

