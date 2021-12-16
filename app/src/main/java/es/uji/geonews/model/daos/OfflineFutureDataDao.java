package es.uji.geonews.model.daos;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.data.CurrentsData;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.OpenWeatherForecastData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.services.ServiceName;

public class OfflineFutureDataDao {
    private final Map<String, OpenWeatherForecastData> openWeatherOfflineFutureData;

    public OfflineFutureDataDao(HashMap<Integer, HashMap<ServiceName, ServiceData>> offlineFutureData) {
        openWeatherOfflineFutureData = new HashMap<>();
        convertOfflineFutureData(offlineFutureData);
    }


    private void convertOfflineFutureData(HashMap<Integer, HashMap<ServiceName, ServiceData>> offlineFutureData){
        for(int locationId : offlineFutureData.keySet()) {
            Map<ServiceName, ServiceData> locationOfflineFutureData = offlineFutureData.get(locationId);
            if (locationOfflineFutureData != null){
                for(ServiceName serviceName : locationOfflineFutureData.keySet()){
                    if (serviceName.equals(ServiceName.OPEN_WEATHER)) {
                        ServiceData serviceDataList = locationOfflineFutureData.get(serviceName);
                        if (serviceDataList != null) {
                            openWeatherOfflineFutureData.put(String.valueOf(locationId),
                                    (OpenWeatherForecastData) serviceDataList);
                        }
                    }
                }
            }
        }
    }

    public Map<String, OpenWeatherForecastData> getOpenWeatherOfflineFutureData() {
        return openWeatherOfflineFutureData;
    }


}
