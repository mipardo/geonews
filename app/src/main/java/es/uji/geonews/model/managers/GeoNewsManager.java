package es.uji.geonews.model.managers;

import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.ServiceHttp;

public class GeoNewsManager {

    private LocationManager locationManager;
    private ServiceManager serviceManager;

    public GeoNewsManager(){
        GeocodeService geocodeService = new GeocodeService();
        serviceManager = new ServiceManager();
        locationManager = new LocationManager(geocodeService);
    }

    public Location addLocation(String location)
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        Location newLocation = locationManager.addLocation(location);
        List<String> activeServicesForLocation = serviceManager.validateLocation(newLocation);
        serviceManager.initLocationServices(newLocation);
        if (activeServicesForLocation.isEmpty()) {
            //TODO Checkear que pasa si no hay servicios disponibles en este momento
            return null;
        }
        return newLocation;
    }

    public boolean addService(ServiceHttp service) {
        if (service.checkConnection()) {
            serviceManager.addService(service);
            return true;
        }
        return false;
    }

    public boolean activateLocation(int id) throws NoLocationRegisteredException {
        Location location = locationManager.getLocation(id);
        if (serviceManager.validateLocation(location).size() > 0) {
            return locationManager.activateLocation(id);
        }
        return false;
    }



    public void deactivateLocation(int id) {
        locationManager.deactivateLocation(id);
    }

    public void deactivateService(String service) {
        serviceManager.deactivateService(service);
    }

    public List<Location> getActiveLocations() {
        return locationManager.getActiveLocations();
    }
}
