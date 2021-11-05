package es.uji.geonews.model;

import es.uji.geonews.model.exceptions.GPSNotAvailableException;

public class GPSManager {
    public GPSManager(){
        super();
    }

    public GeographCoords getMyCoords() throws GPSNotAvailableException {
        if (isAvailable()){
            return null;
        }
        throw new GPSNotAvailableException();
    }

    private boolean isAvailable(){
        return true;
    }
}
