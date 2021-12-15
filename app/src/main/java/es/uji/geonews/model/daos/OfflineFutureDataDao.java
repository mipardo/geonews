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
    private final Map<String, List<OpenWeatherForecastData>> openWeatherOfflineFutureData;

    public OfflineFutureDataDao(HashMap<Integer, HashMap<ServiceName, List<ServiceData>>> offlineFutureData) {
        openWeatherOfflineFutureData = new HashMap<>();
        convertOfflineFutureData(offlineFutureData);
    }


    private void convertOfflineFutureData(HashMap<Integer, HashMap<ServiceName, List<ServiceData>>> offlineFutureData){
        for(int locationId : offlineFutureData.keySet()) {
            Map<ServiceName, List<ServiceData>> locationOfflineFutureData = offlineFutureData.get(locationId);
            if (locationOfflineFutureData != null){
                for(ServiceName serviceName : locationOfflineFutureData.keySet()){
                    if (serviceName.equals(ServiceName.OPEN_WEATHER)) {
                        List<ServiceData> serviceDataList = locationOfflineFutureData.get(serviceName);
                        if (serviceDataList != null) {
                            List<OpenWeatherForecastData> openWeatherForecastDataList = new ArrayList<>();
                            for (ServiceData serviceData : serviceDataList){
                                OpenWeatherForecastData data = (OpenWeatherForecastData) serviceData;
                                openWeatherForecastDataList.add(data);
                            }
                            openWeatherOfflineFutureData.put(String.valueOf(locationId),
                                    openWeatherForecastDataList);
                        }
                    }
                }
            }
        }
    }

    public Map<String, List<OpenWeatherForecastData>> getOpenWeatherOfflineFutureData() {
        return openWeatherOfflineFutureData;
    }


}
