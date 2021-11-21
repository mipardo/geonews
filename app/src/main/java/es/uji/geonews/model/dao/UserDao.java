package es.uji.geonews.model.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceName;

public class UserDao {
    int userId;
    Map<Integer, Location> locations;
    Map<Integer, Location> favoriteLocations;
    Map<ServiceName, Service> services;
    Map<Integer, List<ServiceName>> locationServices;
    Map<Integer, Map<ServiceName, Data>> lastData;
    String lastModification;    /// Aqui si nos interesa el horas minutos y segundos

    public UserDao() {}

    public UserDao (int userId, LocationManager locationManager, ServiceManager serviceManager) {
        this.userId = userId;
        this.locations = locationManager.getLocations();
        this.favoriteLocations = locationManager.getFavoriteLocations();
        this.services = serviceManager.getServiceMap();
        this.locationServices = serviceManager.getLocationServices();
        this.lastData = serviceManager.getLastData();
        this.lastModification = LocalDateTime.now().toString();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Map<Integer, Location> getLocations() {
        return locations;
    }

    public void setLocations(Map<Integer, Location> locations) {
        this.locations = locations;
    }

    public Map<Integer, Location> getFavoriteLocations() {
        return favoriteLocations;
    }

    public void setFavoriteLocations(Map<Integer, Location> favoriteLocations) {
        this.favoriteLocations = favoriteLocations;
    }

    public Map<ServiceName, Service> getServices() {
        return services;
    }

    public void setServices(Map<ServiceName, Service> services) {
        this.services = services;
    }

    public Map<Integer, List<ServiceName>> getLocationServices() {
        return locationServices;
    }

    public void setLocationServices(Map<Integer, List<ServiceName>> locationServices) {
        this.locationServices = locationServices;
    }

    public Map<Integer, Map<ServiceName, Data>> getLastData() {
        return lastData;
    }

    public void setLastData(Map<Integer, Map<ServiceName, Data>> lastData) {
        this.lastData = lastData;
    }

    public String getLastModification() {
        return lastModification;
    }

    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }

}
