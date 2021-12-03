package es.uji.geonews.model.managers;

import android.content.Context;

public class GeoNewsManagerSingleton {
    private static GeoNewsManager geoNewsManager;

    private GeoNewsManagerSingleton(){

    }

    public static GeoNewsManager getInstance(Context context){
        if (geoNewsManager == null) {
            geoNewsManager = new GeoNewsManager(context);
        }
        return geoNewsManager;
    }
    
}
