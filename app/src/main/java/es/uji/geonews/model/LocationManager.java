package es.uji.geonews.model;

import java.util.List;

import es.uji.geonews.model.Services.ServiceManager;

public class LocationManager {
    private List<Location> activeLocations;
    private List<Location> nonActiveLocations;
    private List<Location> favoriteLocations;
    private ServiceManager serviceManager;

    public LocationManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public List<Location> getActiveLocations() {
        return activeLocations;
    }

    public List<Location> getNonActiveLocations() {
        return nonActiveLocations;
    }

    public List<Location> getFavoriteLocations() {
        return favoriteLocations;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
