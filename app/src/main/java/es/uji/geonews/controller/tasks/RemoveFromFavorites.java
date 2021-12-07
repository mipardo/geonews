package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import es.uji.geonews.R;
import es.uji.geonews.controller.LocationListAdapter;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class RemoveFromFavorites extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Location location;
    private final ImageView favoritesButton;
    private boolean removedFromFavorites;

    public RemoveFromFavorites(Context context, Location location, ImageView favoritesButton){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.favoritesButton = favoritesButton;
        this.location = location;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                removedFromFavorites = geoNewsManager.removeFromFavorites(location.getId());
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(removedFromFavorites) favoritesButton.setImageResource(R.drawable.heart);
                    }
                });
            }
        }).start();
    }


}
