package es.uji.geonews.model.dao;

import java.util.HashMap;
import java.util.Map;

import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceName;

public class ServiceDataWrapper {

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

//    private static void setOfflineData(ServiceManager serviceManager, UserDao userDao){
//        Map<Integer, Map<ServiceName, ServiceData>> offlineData = serviceManager.getOfflineData();
//        for(String stringLocationId : userDao.getLastData().keySet()){
//            int locationId = Integer.parseInt(stringLocationId);
//            Map<String, ServiceData> locationOfflineDataLoaded = userDao.getLastData().get(stringLocationId);
//            if (locationOfflineDataLoaded == null) break;
//            Map<ServiceName, ServiceData> locationOfflineData = new HashMap<>();
//            for(String serviceNameString: locationOfflineDataLoaded.keySet()){
//                if (ServiceName.fromString(serviceNameString).name.equals(ServiceName.AIR_VISUAL.name)){
//                    AirVisualData airVisualDataLoaded = (AirVisualData) locationOfflineDataLoaded.get(serviceNameString);
//                    if (airVisualDataLoaded != null) {
//                        AirVisualData airVisualData = new AirVisualData();
//                        airVisualData.setWindSpeed(airVisualDataLoaded.getWindSpeed());
//                        airVisualData.setWindDirection(airVisualDataLoaded.getWindDirection());
//                        airVisualData.setWeatherIcon(airVisualDataLoaded.getWeatherIcon());
//                        airVisualData.setTemperature(airVisualDataLoaded.getTemperature());
//                        airVisualData.setPressure(airVisualDataLoaded.getPressure());
//                        airVisualData.setHumidity(airVisualDataLoaded.getHumidity());
//                        airVisualData.setAqiCn(airVisualDataLoaded.getAqiCn());
//                        airVisualData.setAqiUs(airVisualDataLoaded.getAqiUs());
//                        airVisualData.setMainCn(airVisualDataLoaded.getMainCn());
//                        airVisualData.setMainUs(airVisualDataLoaded.getMainUs());
//                        locationOfflineData.put(ServiceName.AIR_VISUAL, airVisualData);
//                    }
//                } else if (ServiceName.fromString(serviceNameString).name.equals(ServiceName.OPEN_WEATHER.name)){
//
//                } else {
//
//                }
//            }
//            offlineData.put(locationId, locationOfflineData);
//        }
//    }

}