package es.uji.geonews.model.services;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {

    private final Map<String, Service> serviceMap;

    public ServiceManager(){
        serviceMap = new HashMap<>();
    }

    public void addService(Service service){
        serviceMap.put(service.getServiceName(), service);
    }

    public Service getService(String name) {
        return serviceMap.get(name);
    }
}
