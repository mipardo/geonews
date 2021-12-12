package es.uji.geonews.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceName;

public class UserDaoConverter {


    protected static Map<String, List<String>> convertLocationServicesHashMap(Map<Integer, List<ServiceName>> locationServices) {
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

    protected static Map<String, Location> convertLocationsToHashMap(List<Location> locations) {
        Map<String, Location> convertedMap = new HashMap<>();
        for (Location location : locations) {
            convertedMap.put(String.valueOf(location.getId()), location);
        }
        return convertedMap;
    }

    protected static <T> Map<String, T> convertServices (Map<ServiceName, T> map) {
        Map<String, T> convertedMap = new HashMap<>();
        for (ServiceName key : map.keySet()) {
            convertedMap.put(key.name, map.get(key));
        }
        return convertedMap;
    }

    // Conversión de vuelta
    public static void fillLocationManager(LocationManager locationManager, UserDao userDao) {
        locationManager.setLocations(convertLocationsBack(userDao.getLocations()));
        locationManager.setLocationCounter(userDao.getLocationCounter());
    }

    public static void fillServiceManager(ServiceManager serviceManager, UserDao userDao) {
        setServices(serviceManager, userDao);
        serviceManager.setLocationServices(convertLocationServicesBack(userDao.getLocationServices()));
        setOfflineData(serviceManager, userDao);
    }

    private static void setOfflineData(ServiceManager serviceManager, UserDao userDao){
        Map<Integer, Map<ServiceName, ServiceData>> offlineData = serviceManager.getOfflineData();
        for(String stringLocationId : userDao.getOfflineData().keySet()){
            int locationId = Integer.parseInt(stringLocationId);
            Map<String, ServiceData> locationOfflineDataLoaded = userDao.getOfflineData().get(stringLocationId);
            if (locationOfflineDataLoaded == null) break;
            Map<ServiceName, ServiceData> locationOfflineData = new HashMap<>();
            for(String serviceNameString: locationOfflineDataLoaded.keySet()){
            }
            offlineData.put(locationId, locationOfflineData);
        }
    }

    private static void setServices(ServiceManager serviceManager, UserDao userDao){
        for(Service service: serviceManager.getServices().values()){
            ServiceDao storedService = userDao.getServices().get(service.getServiceName().name);
            if (storedService != null) {
                service.setActivationDate(storedService.getActivationDate());
                service.setActive(storedService.isActive());
            }
        }
    }
    protected static Map<String, Map<String, ServiceData>> convertOfflineData(Map<Integer, Map<ServiceName, ServiceData>> offlineData) {
        Map<String, Map<String, ServiceData>> res = new HashMap<>();
        for (Map.Entry<Integer, Map<ServiceName, ServiceData>> entry: offlineData.entrySet()) {
            res.put(String.valueOf(entry.getKey()), UserDaoConverter.convertServices(entry.getValue()));
        }
        return res;
    }


    protected static Map<Integer, Map<ServiceName, ServiceData>> convertOfflineDataBack(Map<String, Map<String, ServiceData>> offlinDataLoaded) {
        Map<Integer, Map<ServiceName, ServiceData>> res = new HashMap<>();
        for (Map.Entry<String, Map<String, ServiceData>> entry: offlinDataLoaded.entrySet()) {
            res.put(Integer.parseInt(entry.getKey()), UserDaoConverter.convertServicesBack(entry.getValue()));
        }
        return res;
    }

    protected static Map<Integer, List<ServiceName>> convertLocationServicesBack(Map<String, List<String>> locationServices) {
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

    protected static  <T> Map<ServiceName, T> convertServicesBack (Map<String, T> map) {
        Map<ServiceName, T> convertedMap = new HashMap<>();
        for (String key : map.keySet()) {
            convertedMap.put(ServiceName.fromString(key), map.get(key));
        }
        return convertedMap;
    }

    private static <T> Map<Integer, T> convertLocationsBack (Map<String, T> values) {
        Map<Integer, T> convertedMap = new HashMap<>();
        for (String key : values.keySet()) {
            convertedMap.put(Integer.parseInt(key), values.get(key));
        }
        return convertedMap;
    }

}
