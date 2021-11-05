package es.uji.geonews.model.services;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;

public class GpsService extends Service{
    public GpsService(){
        super("GpsService", "Servicio GPS para la obtención de la ubicacion actual");
    }

    public GeographCoords getMyCoords() throws GPSNotAvailableException {
        if (isAvailable()){
            return null;
        }
        throw new GPSNotAvailableException();
    }

}
