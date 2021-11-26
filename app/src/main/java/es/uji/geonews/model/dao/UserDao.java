package es.uji.geonews.model.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceName;

public class UserDao {
    private String userId;
    private int locationCounter;
    private Map<String, Location> locations;
    private Map<String, Location> favoriteLocations;
    private Map<String, Service> services;
    private Map<String, List<String>> locationServices;
    private Map<String, Map<String, Data>> lastData;
    private String lastModification;    /// Aqui si nos interesa el horas minutos y segundos

    public UserDao() {}

    public UserDao (String userId, LocationManager locationManager, ServiceManager serviceManager) {
        this.userId = userId;
        this.locationCounter = locationManager.getLocationCounter();
        this.locations = convertLocations(locationManager.getLocations());
        this.favoriteLocations = convertLocations(locationManager.getFavoriteLocations());
        this.services = convertServices(serviceManager.getServices());
        this.locationServices =  convertLocationServices(serviceManager.getLocationServices());
        this.lastData = convertLastData(serviceManager.getLastData());
        this.lastModification = LocalDateTime.now().toString();
    }

    private Map<String, Map<String, Data>> convertLastData(Map<Integer, Map<ServiceName, Data>> lastData) {
        Map<String, Map<String, Data>> convertedLastData = new HashMap<>();
        for (Map.Entry<Integer, Map<ServiceName, Data>> entry: lastData.entrySet()) {
            convertedLastData.put(String.valueOf(entry.getKey()), convertServices(entry.getValue()));
        }
        return convertedLastData;
    }

    private Map<String, List<String>> convertLocationServices(Map<Integer, List<ServiceName>> locationServices) {
        Map<String, List<String>> convertedMap = new HashMap<>();
        for (Map.Entry<Integer, List<ServiceName>> entry : locationServices.entrySet()) {
            List<String> servicesString = new ArrayList<>();
            for (ServiceName s: entry.getValue()) {
                servicesString.add(s.name);
            }
            convertedMap.put(String.valueOf(entry.getKey()), servicesString);
        }
        return convertedMap;
    }

    private <T> Map<String, T> convertLocations (Map<Integer, T> values) {
        Map<String, T> convertedMap = new HashMap<>();
        for (Integer key : values.keySet()) {
            convertedMap.put(String.valueOf(key), values.get(key));
        }
        return convertedMap;
    }

    private <T> Map<String, T> convertServices (Map<ServiceName, T> map) {
        Map<String, T> convertedMap = new HashMap<>();
        for (ServiceName key : map.keySet()) {
            convertedMap.put(key.name, map.get(key));
        }
        return convertedMap;
    }

    // Conversión de vuelta
    public void fillLocationManager(LocationManager locationManager) {
        locationManager.setLocations(convertLocationsBack(locations));
        locationManager.setLocationCounter(locationCounter);
        locationManager.setFavoriteLocations(convertLocationsBack(favoriteLocations));
    }

    public void fillServiceManager(ServiceManager serviceManager) {
        serviceManager.setServices(convertServicesBack(services));
        serviceManager.setLocationServices(convertLocationServicesBack(locationServices));
        serviceManager.setLastData(convertLastDataBack(lastData));
    }

    private Map<Integer, Map<ServiceName, Data>> convertLastDataBack(Map<String, Map<String, Data>> lastData) {
        Map<Integer, Map<ServiceName, Data>> convertedLastData = new HashMap<>();
        for (Map.Entry<String, Map<String, Data>> entry: lastData.entrySet()) {
            convertedLastData.put(Integer.parseInt(entry.getKey()), convertServicesBack(entry.getValue()));
        }
        return convertedLastData;
    }

    private Map<Integer, List<ServiceName>> convertLocationServicesBack(Map<String, List<String>> locationServices) {
        Map<Integer, List<ServiceName>> convertedMap = new HashMap<>();
        for (Map.Entry<String , List<String>> entry : locationServices.entrySet()) {
            List<ServiceName> servicesString = new ArrayList<>();
            for (String s: entry.getValue()) {
                servicesString.add(ServiceName.fromString(s));
            }
            convertedMap.put(Integer.parseInt(entry.getKey()), servicesString);
        }
        return convertedMap;
    }

    private <T> Map<ServiceName, T> convertServicesBack (Map<String, T> map) {
        Map<ServiceName, T> convertedMap = new HashMap<>();
        for (String key : map.keySet()) {
            convertedMap.put(ServiceName.fromString(key), map.get(key));
        }
        return convertedMap;
    }

    private <T> Map<Integer, T> convertLocationsBack (Map<String, T> values) {
        Map<Integer, T> convertedMap = new HashMap<>();
        for (String key : values.keySet()) {
            convertedMap.put(Integer.parseInt(key), values.get(key));
        }
        return convertedMap;
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

    public Map<String, Service> getServices() {
        return services;
    }

    public void setServices(Map<String, Service> services) {
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


}
