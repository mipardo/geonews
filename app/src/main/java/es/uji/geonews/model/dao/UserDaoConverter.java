package es.uji.geonews.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class UserDaoConverter {

    protected static Map<String, Map<String, Data>> convertLastData(Map<Integer, Map<ServiceName, Data>> lastData) {
        Map<String, Map<String, Data>> convertedLastData = new HashMap<>();
        for (Map.Entry<Integer, Map<ServiceName, Data>> entry: lastData.entrySet()) {
            convertedLastData.put(String.valueOf(entry.getKey()), convertServices(entry.getValue()));
        }
        return convertedLastData;
    }

    protected static Map<String, List<String>> convertLocationServices(Map<Integer, List<ServiceName>> locationServices) {
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

    protected static <T> Map<String, T> convertLocations (Map<Integer, T> values) {
        Map<String, T> convertedMap = new HashMap<>();
        for (Integer key : values.keySet()) {
            convertedMap.put(String.valueOf(key), values.get(key));
        }
        return convertedMap;
    }

    protected static <T> Map<String, T> convertServices (Map<ServiceName, T> map) {
        Map<String, T> convertedMap = new HashMap<>();
        for (ServiceName key : map.keySet()) {
            if (!(key == ServiceName.GPS))
                convertedMap.put(key.name, map.get(key));
        }
        return convertedMap;
    }

    // Conversión de vuelta
    public static void fillLocationManager(LocationManager locationManager, UserDao userDao) {
        locationManager.setLocations(convertLocationsBack(userDao.getLocations()));
        locationManager.setLocationCounter(userDao.getLocationCounter());
        locationManager.setFavoriteLocations(convertLocationsBack(userDao.getFavoriteLocations()));
    }

    public static void fillServiceManager(ServiceManager serviceManager, UserDao userDao) {
        serviceManager.setServices(convertServicesBack(userDao.getServices()));
        serviceManager.setLocationServices(convertLocationServicesBack(userDao.getLocationServices()));
        serviceManager.setLastData(convertLastDataBack(userDao.getLastData()));
    }

    private static Map<Integer, Map<ServiceName, Data>> convertLastDataBack(Map<String, Map<String, Data>> lastData) {
        Map<Integer, Map<ServiceName, Data>> convertedLastData = new HashMap<>();
        for (Map.Entry<String, Map<String, Data>> entry: lastData.entrySet()) {
            convertedLastData.put(Integer.parseInt(entry.getKey()), convertServicesBack(entry.getValue()));
        }
        return convertedLastData;
    }

    private static Map<Integer, List<ServiceName>> convertLocationServicesBack(Map<String, List<String>> locationServices) {
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

    private static  <T> Map<ServiceName, T> convertServicesBack (Map<String, T> map) {
        Map<ServiceName, T> convertedMap = new HashMap<>();
        for (String key : map.keySet()) {
            convertedMap.put(ServiceName.fromString(key), map.get(key));
        }
        return convertedMap;
    }

    private static  <T> Map<Integer, T> convertLocationsBack (Map<String, T> values) {
        Map<Integer, T> convertedMap = new HashMap<>();
        for (String key : values.keySet()) {
            convertedMap.put(Integer.parseInt(key), values.get(key));
        }
        return convertedMap;
    }

}
