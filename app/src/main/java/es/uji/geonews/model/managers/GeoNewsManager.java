package es.uji.geonews.model.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.exceptions.DatabaseNotAvailableException;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
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
import es.uji.geonews.model.services.ServiceName;

public class GeoNewsManager {
    private String userId;
    private final LocationManager locationManager;
    private final ServiceManager serviceManager;
    private final DatabaseManager databaseManager;

    public GeoNewsManager(Context context){
        databaseManager = new DatabaseManager(context);
        serviceManager = new ServiceManager();
        serviceManager.addService(new GpsService(context));
        serviceManager.addService(new AirVisualService());
        serviceManager.addService(new CurrentsService());
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new GeocodeService());
        locationManager = new LocationManager((GeocodeService) serviceManager.getService(ServiceName.GEOCODE));
        userId = loadUserId(context);
        try {
            loadAll();
        } catch (DatabaseNotAvailableException e) { e.printStackTrace(); }
    }

    public GeoNewsManager(LocationManager locationManager, ServiceManager serviceManager,
                          DatabaseManager databaseManager, String userId) {
        this.locationManager = locationManager;
        this.serviceManager = serviceManager;
        this.databaseManager = databaseManager;
        this.userId = userId;
    }

    public void setUserId(String code) {
        this.userId = code;
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

    public Location addLocationByGps() throws GPSNotAvailableException,
            NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        GpsService gpsService = (GpsService) serviceManager.getService(ServiceName.GPS);
        return addLocation(gpsService.currentCoords().toString());
    }

    public void updateGpsCoords(){
        GpsService gpsService = (GpsService) serviceManager.getService(ServiceName.GPS);
        gpsService.updateGpsCoords();
    }

    public boolean removeLocation(int locationId) {
        boolean removed = locationManager.removeLocation(locationId);
        saveAll(removed);
        return removed;
    }

    public boolean activateLocation(int locationId) throws NoLocationRegisteredException {
        Location location = locationManager.getLocation(locationId);
        if (serviceManager.validateLocation(location).size() > 0) {
            boolean activated = locationManager.activateLocation(locationId);
            saveAll(activated);
            return activated;
        }
        return false;
    }

    public boolean deactivateLocation(int locationId) {
        boolean deactivated = locationManager.deactivateLocation(locationId);
        saveAll(deactivated);
        return deactivated;
    }

    public boolean activateService(ServiceName serviceName) {
        boolean activated = serviceManager.activateService(serviceName);
        saveAll(activated);
        return activated;
    }

    public boolean deactivateService(ServiceName service) {
        boolean deactivated = serviceManager.deactivateService(service);
        saveAll(deactivated);
        return deactivated;
    }

    public Location getLocation(int locationId) throws NoLocationRegisteredException {
        return  locationManager.getLocation(locationId);
    }

    public List<Location> getAllLocations(){
        List<Location> allLocations = new ArrayList<>();
        allLocations.addAll(getActiveLocations());
        allLocations.addAll(getNonActiveLocations());
        return allLocations;
    }

    public List<Location> getActiveLocations() {
        return locationManager.getActiveLocations();
    }

    public List<Location> getNonActiveLocations() {
        return locationManager.getNonActiveLocations();
    }

    public boolean setAliasToLocation(String alias, int locationId) throws NoLocationRegisteredException {
        boolean added = locationManager.setAliasToLocation(alias, locationId);
        saveAll(added);
        return added;
    }

    public boolean addToFavorites(int locationId) {
        boolean added = locationManager.addToFavorites(locationId);
        saveAll(added);
        return added;
    }

    public List<Location> getFavouriteLocations() {
        return locationManager.getFavouriteLocations();
    }
    public List<Location> getNoFavouriteLocations() {
        return locationManager.getNoFavouriteLocations();
    }

    public boolean removeFromFavorites(int locationId) {
        boolean removed = locationManager.removeFromFavorites(locationId);
        saveAll(removed);
        return removed;
    }

    public ServiceData getData(ServiceName serviceName, int locationId)
            throws ServiceNotAvailableException, NoLocationRegisteredException {
        Location location = getLocation(locationId);
        ServiceData data = serviceManager.getData(serviceName, location);
        saveAll(true);
        return data;
    }

    public ServiceData getOfflineData(ServiceName serviceName, int locationId) throws NoLocationRegisteredException {
        Location location = getLocation(locationId);
        return serviceManager.getOfflineData(serviceName, location);
    }

    public Service getService(ServiceName serviceName) {
        return serviceManager.getService(serviceName);
    }

    public Map<String, String> getServicesDescription() throws ServiceNotAvailableException {
        return serviceManager.getServicesDescription();
    }

    public List<ServiceName> getActiveServices() {
        return serviceManager.getActiveServices();
    }

    public List<ServiceName> getPublicServices() {
        return serviceManager.getPublicServices();
    }

    public List<ServiceName> getServicesOfLocation(int locationId) {
        return serviceManager.getServicesOfLocation(locationId);
    }

    public boolean addServiceToLocation(ServiceName serviceName, int locationId)
            throws ServiceNotAvailableException, NoLocationRegisteredException {
        Location location = locationManager.getLocation(locationId);
        boolean added = serviceManager.addServiceToLocation(serviceName, location);
        saveAll(added);
        return added;
    }

    public boolean removeServiceFromLocation(ServiceName serviceName, int locationId)
            throws NoLocationRegisteredException {
        Location location = locationManager.getLocation(locationId);
        boolean removed = serviceManager.removeServiceFromLocation(serviceName, location);
        saveAll(removed);
        return removed;
    }

    public void loadAll() throws DatabaseNotAvailableException {
        databaseManager.loadAll(userId, locationManager, serviceManager);
    }

    private void saveAll(boolean operationResult){
        if (operationResult) {
            databaseManager.saveAll(userId, locationManager, serviceManager);
        }
    }

    public void loadRemoteState(String importCode) throws DatabaseNotAvailableException {
        databaseManager.loadRemoteState(importCode, locationManager, serviceManager);
    }

    public String loadUserId(Context context) {
        return databaseManager.getUserId(context);
    }

    //  WARNING: This method is just used for the tests.
    // Make sure it is not been used in the Controller or Model
    public void removeUserConfiguration(String userIdToDelete){
        databaseManager.removeUser(userIdToDelete);
    }

    public String generateExportCode() {
        return databaseManager.saveGeneratedCode(userId);
    }

    public void loadAllSharedCodes() {
        databaseManager.loadAllSharedCodes();
    }

    public boolean checkImportCode(String importCode) {
        return databaseManager.checkImportCode(importCode);
    }
}
