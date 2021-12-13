package es.uji.geonews.model.daos;


import java.util.HashMap;
import java.util.Map;

import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.data.CurrentsData;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.services.ServiceName;

public class OfflineDataDao {
    private final Map<String, AirVisualData> airVisualOfflineData;
    private final Map<String, OpenWeatherData> openWeatherOfflineData;
    private final Map<String, CurrentsData> currentsOfflineData;

    public OfflineDataDao(HashMap<Integer, HashMap<ServiceName, ServiceData>> offlineData) {
        airVisualOfflineData = new HashMap<>();
        openWeatherOfflineData = new HashMap<>();
        currentsOfflineData = new HashMap<>();
        convertOfflineData(offlineData);
    }


    private void convertOfflineData(HashMap<Integer, HashMap<ServiceName, ServiceData>> offlineData){
        for(int location : offlineData.keySet()){
            Map<ServiceName, ServiceData> locationOfflineData = offlineData.get(location);
            if (locationOfflineData != null){
                for(ServiceName serviceName : locationOfflineData.keySet()){
                    if (serviceName.equals(ServiceName.AIR_VISUAL)){
                        airVisualOfflineData.put(String.valueOf(location),
                                (AirVisualData) locationOfflineData.get(serviceName));
                    } else if (serviceName.equals(ServiceName.OPEN_WEATHER)) {
                        openWeatherOfflineData.put(String.valueOf(location),
                                (OpenWeatherData) locationOfflineData.get(serviceName));
                    } else {
                        currentsOfflineData.put(String.valueOf(location),
                                (CurrentsData) locationOfflineData.get(serviceName));
                    }
                }
            }
        }
    }

    public Map<String, AirVisualData> getAirVisualOfflineData() {
        return airVisualOfflineData;
    }

    public Map<String, CurrentsData> getCurrentsOfflineData() {
        return currentsOfflineData;
    }

    public Map<String, OpenWeatherData> getOpenWeatherOfflineData() {
        return openWeatherOfflineData;
    }

}
