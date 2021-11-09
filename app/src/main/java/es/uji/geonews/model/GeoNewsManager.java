package es.uji.geonews.model;

import java.util.List;

import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.ServiceManager;

public class GeoNewsManager {

    private LocationManager locationManager;
    private ServiceManager serviceManager;

    public GeoNewsManager(){
        GeocodeService geocodeService = new GeocodeService();
        serviceManager = new ServiceManager();
        locationManager = new LocationManager(geocodeService);
    }

    public boolean addLocation(String location)
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        Location newLocation = locationManager.addLocation(location);
        List<String> activeServicesForLocation = serviceManager.validateLocation(newLocation);
        return !activeServicesForLocation.isEmpty();
    }

}
