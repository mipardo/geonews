package es.uji.geonews.model.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.services.ContextDataGetter;
import es.uji.geonews.model.services.DataGetterStrategy;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceName;

public class ServiceManager {

    private final Map<ServiceName, Service> serviceMap;
    private final Map<Integer, List<ServiceName>> locationServices;
    private final ContextDataGetter contextDataGetter;


    public ServiceManager(){
        this.serviceMap = new HashMap<>();
        this.locationServices = new HashMap<>();
        this.contextDataGetter = new ContextDataGetter();
    }

    public Map<String, String> getServices() throws ServiceNotAvailableException {
        if (serviceMap.isEmpty()) {
            throw new ServiceNotAvailableException();
        }

        Map<String, String> services = new ArrayMap<>();
        for (Service service : serviceMap.values()) {
            services.put(service.getServiceName().name, service.getDescription());
        }
        return services;
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

    public Service getService(ServiceName serviceName) {
        return serviceMap.get(serviceName);
    }

    public void addService(Service service){
        serviceMap.put(service.getServiceName(), service);
    }


    public Data getData(ServiceName serviceName, Location location) throws ServiceNotAvailableException {
        List<ServiceName> activeServices = locationServices.get(location.getId());

        if (!serviceMap.get(serviceName).isAvailable()) {
            throw new ServiceNotAvailableException();
        }

        if (location != null && activeServices.contains(serviceName)) {
            DataGetterStrategy service = (DataGetterStrategy) getService(serviceName);
            contextDataGetter.setService(service);
            return contextDataGetter.getData(location);
        }
        return null;
    }


    public List<ServiceName> validateLocation(Location location){
        List<ServiceName> services = new ArrayList<>();
        for(ServiceHttp service: getHttpServices()){
            if(!service.getServiceName().name.equals("Geocode") &&
                    service.isAvailable() && service.validateLocation(location)){
                services.add(service.getServiceName());
            }
        }
        return services;
    }

    public boolean addServiceToLocation(ServiceName serviceName, Location location)
            throws ServiceNotAvailableException {

        Service service = getService(serviceName);
        if (location == null || !(service instanceof  ServiceHttp)) return false;
        ServiceHttp serviceHttp = (ServiceHttp) service;
        if (!serviceHttp.validateLocation(location) || !serviceHttp.isAvailable()) throw new ServiceNotAvailableException();

        int locationId = location.getId();
        List<ServiceName> currentServicesInLocation = locationServices.get(locationId);
        if (!currentServicesInLocation.contains(serviceName)) {
            currentServicesInLocation.add(serviceName);
            locationServices.put(locationId, currentServicesInLocation);
            return true;
        }
        return false;
    }

    public boolean removeServiceFromLocation(ServiceName serviceName, Location location) {
        if (location != null) {
            int locationId = location.getId();
            List<ServiceName> currentServicesInLocation = locationServices.get(locationId);
            if (currentServicesInLocation != null) {
                if (currentServicesInLocation.contains(serviceName)) {
                    currentServicesInLocation.remove(serviceName);
                    return true;
                }
            }
        }
        return false;
    }

    public List<ServiceName> getServicesOfLocation(int locationId) {
        List<ServiceName> servicesOfLocation = locationServices.get(locationId);
        if (servicesOfLocation == null)
            return new ArrayList<>();
        return servicesOfLocation;
    }

    public boolean deactivateService(ServiceName serviceName) {
        Service service = getService(serviceName);
        if (service != null && service.isActive()) {
            service.deactivate();
            return true;
        }
        return false;
    }

    public boolean activateService(ServiceName serviceName) {
        Service service = getService(serviceName);
        if (service != null && !service.isActive() && ((ServiceHttp) service).checkConnection()) {
            service.activate();
            return true;
        }
        return false;
    }

    public void initLocationServices(Location newLocation) {
        locationServices.put(newLocation.getId(), new ArrayList<>());
    }

    public List<ServiceName> getAvailableServices(){
        List<ServiceName> httpServices = new ArrayList<>();
        for(Service service: serviceMap.values()){
            if(service instanceof ServiceHttp){
                httpServices.add(service.getServiceName());
            }
        }
        return httpServices;
    }

    public List<ServiceName> getActiveServices() {
        List<ServiceName> activeServices = new ArrayList<>();
        for (Service service: serviceMap.values()) {
            if (service.isAvailable()) {
                activeServices.add(service.getServiceName());
            }
        }
        return activeServices;
    }
}
