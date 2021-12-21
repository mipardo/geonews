package es.uji.geonews.controller.tasks;

import android.content.Context;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

import es.uji.geonews.R;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;

public class RemoveFromFavorites extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final Location location;
    private final LottieAnimationView favoritesButton;
    private boolean removedFromFavorites;

    public RemoveFromFavorites(Context context, Location location, LottieAnimationView favoritesButton){
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
                        if(removedFromFavorites) {
                            favoritesButton.setSpeed(-1);
                            favoritesButton.playAnimation();
                            //favoritesButton.setImageResource(R.drawable.heart);
                        }
                    }
                });
            }
        }).start();
    }


}
