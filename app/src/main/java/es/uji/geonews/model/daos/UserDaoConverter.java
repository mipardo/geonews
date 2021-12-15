package es.uji.geonews.model.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.data.CurrentsData;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.OpenWeatherForecastData;
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
        setOfflineFutureData(serviceManager, userDao);
    }

    private static void setOfflineFutureData(ServiceManager serviceManager, UserDao userDao){
        HashMap<Integer, HashMap<ServiceName, List<ServiceData>>> newOfflineFutureData = new HashMap<>();

        OfflineFutureDataDao offlineFutureDataDao = userDao.getOfflineFutureData();

        Map<String, List<ServiceData>> openWeatherOfflineFutureData = offlineFutureDataDao.getOpenWeatherOfflineFutureData();
        for (String locationIdString : openWeatherOfflineFutureData.keySet()){
            int locationId = Integer.parseInt(locationIdString);
            if (!newOfflineFutureData.containsKey(locationId)) {
                newOfflineFutureData.put(locationId, new HashMap<>());
            }
            HashMap<ServiceName, List<ServiceData>> locationServiceData = newOfflineFutureData.get(locationId);
            List<ServiceData> serviceDataList = new ArrayList<>();
            for (ServiceData serviceData : openWeatherOfflineFutureData.get(locationIdString)) {

            }
            locationServiceData.put(ServiceName.OPEN_WEATHER,
                    openWeatherOfflineFutureData.get(locationIdString));
        }
    }

    private static void setOfflineData(ServiceManager serviceManager, UserDao userDao){
        HashMap<Integer, HashMap<ServiceName, ServiceData>> newOfflineData = new HashMap<>();

        OfflineDataDao offlineDataDao = userDao.getOfflineDataDao();

        Map<String, AirVisualData> airVisualOfflineData = offlineDataDao.getAirVisualOfflineData();
        for (String locationIdString : airVisualOfflineData.keySet()){
            int locationId = Integer.parseInt(locationIdString);
            if (!newOfflineData.containsKey(locationId)) {
                newOfflineData.put(locationId, new HashMap<>());
            }
            Map<ServiceName, ServiceData> locationServiceData = newOfflineData.get(locationId);
            locationServiceData.put(ServiceName.AIR_VISUAL, airVisualOfflineData.get(locationIdString));
        }

        Map<String, OpenWeatherData> openWeatherOfflineData = offlineDataDao.getOpenWeatherOfflineData();
        for (String locationIdString : openWeatherOfflineData.keySet()){
            int locationId = Integer.parseInt(locationIdString);
            if (!newOfflineData.containsKey(locationId)) {
                newOfflineData.put(locationId, new HashMap<>());
            }
            Map<ServiceName, ServiceData> locationServiceData = newOfflineData.get(locationId);
            locationServiceData.put(ServiceName.OPEN_WEATHER, openWeatherOfflineData.get(locationIdString));
        }

        Map<String, CurrentsData> currentsOfflineData = offlineDataDao.getCurrentsOfflineData();
        for (String locationIdString : currentsOfflineData.keySet()){
            int locationId = Integer.parseInt(locationIdString);
            if (!newOfflineData.containsKey(locationId)) {
                newOfflineData.put(locationId, new HashMap<>());
            }
            Map<ServiceName, ServiceData> locationServiceData = newOfflineData.get(locationId);
            locationServiceData.put(ServiceName.CURRENTS, currentsOfflineData.get(locationIdString));
        }

        serviceManager.setOfflineData(newOfflineData);
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
