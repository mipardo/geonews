package es.uji.geonews.model.managers;

import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.GpsService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceName;

public class GeoNewsManager {

    private LocationManager locationManager;
    private ServiceManager serviceManager;

    public GeoNewsManager(){
        serviceManager = new ServiceManager();
        serviceManager.addService(new GpsService());
        serviceManager.addService(new AirVisualService());
        serviceManager.addService(new CurrentsService());
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new GeocodeService());
        locationManager = new LocationManager((GeocodeService) serviceManager.getService(ServiceName.GEOCODE));
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public Location addLocation(String location)
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException {
        boolean added = false;
        Location newLocation = locationManager.createLocation(location);
        List<ServiceName> activeServicesForLocation = serviceManager.validateLocation(newLocation);
        if (! activeServicesForLocation.isEmpty()) {
            added = locationManager.addLocation(newLocation);
        }
        if (added){
            serviceManager.initLocationServices(newLocation);
            return newLocation;
        }
        return null;
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

    public void deactivateService(ServiceName service) {
        serviceManager.deactivateService(service);
    }

    public List<Location> getActiveLocations() {
        return locationManager.getActiveLocations();
    }

    public List<Location> getNonActiveLocations() throws NoLocationRegisteredException {
        return locationManager.getNonActiveLocations();
    }
}
