package es.uji.geonews.model.managers;

import android.content.Context;

import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.database.LocalDBManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.GpsService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceName;

public class GeoNewsManager {
    private String userId;
    private final LocationManager locationManager;
    private final ServiceManager serviceManager;
    private DatabaseManager databaseManager;

    public GeoNewsManager(Context context){
        databaseManager = new DatabaseManager();
        serviceManager = new ServiceManager();
        serviceManager.addService(new GpsService());
        serviceManager.addService(new AirVisualService());
        serviceManager.addService(new CurrentsService());
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new GeocodeService());
        locationManager = new LocationManager((GeocodeService) serviceManager.getService(ServiceName.GEOCODE));
        userId = loadUserId(context);
    }

    public GeoNewsManager(LocationManager locationManager, ServiceManager serviceManager) {
        this.locationManager = locationManager;
        this.serviceManager = serviceManager;
    }

    public Location addLocation(String location)
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException {
        Location newLocation = locationManager.createLocation(location);
        boolean added = locationManager.addLocation(newLocation);

        if (added){
            serviceManager.initLocationServices(newLocation);
            databaseManager.saveAll(userId, locationManager, serviceManager);
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
            if( locationManager.activateLocation(id)){
                databaseManager.saveAll(userId, locationManager, serviceManager);
                return true;
            }
        }
        return false;
    }

    public boolean deactivateLocation(int id) {
        if(locationManager.deactivateLocation(id)){
            databaseManager.saveAll(userId, locationManager, serviceManager);
            return true;
        }
        return false;
    }

    public boolean deactivateService(ServiceName service) {
        return serviceManager.deactivateService(service);
    }

    public List<Location> getActiveLocations() throws NoLocationRegisteredException{
        return locationManager.getActiveLocations();
    }

    public List<Location> getNonActiveLocations() throws NoLocationRegisteredException {
        return locationManager.getNonActiveLocations();
    }

    public Location getLocation(int id) throws NoLocationRegisteredException {
        return  locationManager.getLocation(id);
    }

    public boolean addServiceToLocation(ServiceName serviceName, Location location)
            throws ServiceNotAvailableException {
        return serviceManager.addServiceToLocation(serviceName, location);
    }

    public Data getData(ServiceName serviceName, Location location)
            throws ServiceNotAvailableException {
        return serviceManager.getData(serviceName, location);
    }

    public List<ServiceName> getServicesOfLocation(int id) {
        return serviceManager.getServicesOfLocation(id);
    }

    public boolean removeServiceFromLocation(ServiceName serviceName, Location location) {
        return serviceManager.removeServiceFromLocation(serviceName, location);
    }

    public boolean activateService(ServiceName serviceName) {
        return serviceManager.activateService(serviceName);
    }

    public Service getService(ServiceName serviceName) {
        return serviceManager.getService(serviceName);
    }

    public void loadAll() {
        databaseManager.loadAll(userId, locationManager, serviceManager);
    }

    public void saveAll() {
        databaseManager.saveAll(userId, locationManager, serviceManager);
    }

    public String loadUserId(Context context) {
        return databaseManager.getUserId(context);
    }

    public boolean setAliasToLocation(String alias, int locationId) throws NoLocationRegisteredException {
        if(locationManager.setAliasToLocation(alias, locationId)){
            databaseManager.saveAll(userId, locationManager, serviceManager);
            return true;
        }
        return false;
    }

    public boolean removeLocation(int locationId) {
        if(locationManager.removeLocation(locationId)) {
            databaseManager.saveAll(userId, locationManager, serviceManager);
            return true;
        }
        return false;
    }

    public boolean addToFavorites(int locationId) {
        return locationManager.addToFavorites(locationId);
    }

    public List<Location> getFavouriteLocations() throws NoLocationRegisteredException {
        return locationManager.getFavouriteLocations();
    }

    public boolean removeFromFavorites(int locationId) {
        return locationManager.removeFromFavorites(locationId);
    }
}
