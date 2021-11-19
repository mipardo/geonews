package es.uji.geonews.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.services.Service;

public class UserDao {
    int userId;
    Map<Integer, Location> locations;
    Map<Integer, Location> favoriteLocations;
    List<Service> activeServices;
    Map<Integer, Location> locationServices;
    Map<String, Data> lastData;
    String lastModification;    /// Aqui si nos interesa el horas minutos y segundos

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

    public List<Service> getActiveServices() {
        return activeServices;
    }

    public void setActiveServices(List<Service> activeServices) {
        this.activeServices = activeServices;
    }

    public Map<Integer, Location> getLocationServices() {
        return locationServices;
    }

    public void setLocationServices(Map<Integer, Location> locationServices) {
        this.locationServices = locationServices;
    }

    public Map<String, Data> getLastData() {
        return lastData;
    }

    public void setLastData(Map<String, Data> lastData) {
        this.lastData = lastData;
    }

    public String getLastModification() {
        return lastModification;
    }

    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }

}
