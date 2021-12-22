package es.uji.geonews.controller.tasks;

import android.content.Context;

import com.airbnb.lottie.LottieAnimationView;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class AddToFavorites extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Location location;
    private final LottieAnimationView favoritesButton;
    private boolean addedToFavorites;


    public AddToFavorites(Context context, Location location, LottieAnimationView favoritesButton){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.favoritesButton = favoritesButton;
        this.location = location;
    }

    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                addedToFavorites = geoNewsManager.addToFavorites(location.getId());
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(addedToFavorites) {
                            favoritesButton.setSpeed(1);
                            favoritesButton.playAnimation();
                            //favoritesButton.setImageResource(R.drawable.heart_fully);
                        }
                    }
                });
            }
        }).start();
    }


}
