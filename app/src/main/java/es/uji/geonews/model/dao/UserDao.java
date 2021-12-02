package es.uji.geonews.model.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceHttp;

public class UserDao {
    private String userId;
    private int locationCounter;
    private Map<String, Location> locations;
    private Map<String, Location> favoriteLocations;
    private Map<String, ServiceHttp> services;
    private Map<String, List<String>> locationServices;
    private Map<String, Map<String, Data>> lastData;
    private String lastModification;    /// Aqui si nos interesa el horas minutos y segundos

    public UserDao() {}

    public UserDao (String userId, LocationManager locationManager, ServiceManager serviceManager) {
        this.userId = userId;
        this.locationCounter = locationManager.getLocationCounter();
        this.locations = UserDaoConverter.convertLocations(locationManager.getLocations());
        this.favoriteLocations = UserDaoConverter.convertLocations(locationManager.getFavoriteLocations());
        this.services = UserDaoConverter.convertServices(serviceManager.getHttpServicesMap());
        this.locationServices =  UserDaoConverter.convertLocationServices(serviceManager.getLocationServices());
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

    public Map<String, ServiceHttp> getServices() {
        return services;
    }

    public void setServices(Map<String, ServiceHttp> services) {
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
