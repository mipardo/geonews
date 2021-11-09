package es.uji.geonews.model.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;

public class ServiceManager {

    private final Map<String, Service> serviceMap;
    private ContextDataGetter contextDataGetter;


    public ServiceManager(){
        serviceMap = new HashMap<>();
        contextDataGetter = new ContextDataGetter();
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

    public Data getData(DataGetterStrategy dataGetterStrategy, Location location)
            throws ServiceNotAvailableException {
        contextDataGetter.setService(dataGetterStrategy);
        return contextDataGetter.getData(location);
    }

    public void removeService(String serviceName) {
        serviceMap.remove(serviceName);
    }
}
