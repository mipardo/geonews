package es.uji.geonews.model.dao;

import java.util.HashMap;
import java.util.Map;

import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.services.ServiceName;

public class ServiceDataDao {
    private Map<String, Map<String, ServiceData>> offlineServiceData;

    public ServiceDataDao () {}

    public ServiceDataDao (Map<Integer, Map<ServiceName, ServiceData>> offlineData){
        this.offlineServiceData = ServiceDataWrapper.convertOfflineData(offlineData);
    }


    public Map<String, Map<String, ServiceData>> getOfflineServiceData() {
        return offlineServiceData;
    }

    public void setOfflineServiceData(Map<String, Map<String, ServiceData>> offlineServiceData) {
        this.offlineServiceData = offlineServiceData;
    }
}
