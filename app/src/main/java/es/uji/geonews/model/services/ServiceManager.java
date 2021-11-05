package es.uji.geonews.model.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceManager {

    private final Map<String, Service> serviceMap;

    public ServiceManager(){
        serviceMap = new HashMap<>();
    }


    public List<Service> getServices(){
        return new ArrayList<>(serviceMap.values());
    }

    public List<ServiceHttp> getHttpServices(){
        List<ServiceHttp> httpServices = new ArrayList<>();
        for(Service service: serviceMap.values()){
            if(service instanceof ServiceHttp){
                httpServices.add((ServiceHttp) service);
            }
        }
        return httpServices;
    }
    public void addService(Service service){
        serviceMap.put(service.getServiceName(), service);
    }

    public Service getService(String name) {
        return serviceMap.get(name);
    }


}
