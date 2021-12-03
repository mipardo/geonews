package es.uji.geonews.model.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;

public class UserDao {
    private String userId;
    private int locationCounter;
    private Map<String, Location> locations;
    private Map<String, Location> favoriteLocations;
    private Map<String, ServiceDao> services;
    private Map<String, List<String>> locationServices;
    private Map<String, Map<String, Data>> lastData;
    private String lastModification;

    public UserDao() {}

    public UserDao (String userId, LocationManager locationManager, ServiceManager serviceManager) {
        this.userId = userId;
        this.locationCounter = locationManager.getLocationCounter();
        this.locations = UserDaoConverter.convertLocationsToHashMap(locationManager.getLocations());
        this.favoriteLocations = UserDaoConverter.convertLocationsToHashMap(locationManager.getFavouriteLocations());
        this.services = ServiceWrapper.convertServices(serviceManager.getServices());
        this.locationServices =  UserDaoConverter.convertLocationServicesHashMap(serviceManager.getLocationServices());
        this.lastData = UserDaoConverter.convertLastData(serviceManager.getLastData());
        this.lastModification = LocalDateTime.now().toString();
    }

    // Getter & Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, Location> locations) {
        this.locations = locations;
    }

    public Map<String, Location> getFavoriteLocations() {
        return favoriteLocations;
    }

    public void setFavoriteLocations(Map<String, Location> favoriteLocations) {
        this.favoriteLocations = favoriteLocations;
    }

    public Map<String, ServiceDao> getServices() {
        return services;
    }

    public void setServices(Map<String, ServiceDao> services) {
        this.services = services;
    }

    public Map<String, List<String>> getLocationServices() {
        return locationServices;
    }

    public void setLocationServices(Map<String, List<String>> locationServices) {
        this.locationServices = locationServices;
    }

    public Map<String, Map<String, Data>> getLastData() {
        return lastData;
    }

    public void setLastData(Map<String, Map<String, Data>> lastData) {
        this.lastData = lastData;
    }

    public String getLastModification() {
        return lastModification;
    }

    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }

    public int getLocationCounter() {
        return locationCounter;
    }

    public void setLocationCounter(int locationCounter) {
        this.locationCounter = locationCounter;
    }
}
