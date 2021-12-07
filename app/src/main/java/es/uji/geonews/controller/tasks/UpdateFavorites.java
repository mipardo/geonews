package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import androidx.navigation.Navigation;

import es.uji.geonews.R;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class UpdateFavorites extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Location location;


    public UpdateFavorites(Context context, Location location, View view){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.location = location;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (location.isFavorite()) {
                    geoNewsManager.removeFromFavorites(location.getId());
                } else if (location.isActive()){
                    geoNewsManager.addToFavorites(location.getId());
                }
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });
            }
        }).start();
    }


}
