package es.uji.geonews.model;

import es.uji.geonews.model.exceptions.GPSNotAvailableException;

public class GPSManager {
    public static GeographCoords getMyCoords()
            throws GPSNotAvailableException {
        if (isAvailable()){
            return new GeographCoords(39.98001, -0.04901);
        }
        throw new GPSNotAvailableException();
    }

    private static boolean isAvailable(){
        return true;
    }
}
