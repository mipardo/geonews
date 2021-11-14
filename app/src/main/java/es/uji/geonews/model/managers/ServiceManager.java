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

    public Service getService(ServiceName serviceName) {
        return serviceMap.get(serviceName);
    }

    public void addService(Service service){
        serviceMap.put(service.getServiceName(), service);
    }


    public Data getData(ServiceName serviceName, Location location) throws ServiceNotAvailableException {
        List<ServiceName> activeServices = locationServices.get(location.getId());
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
        if (!service.isAvailable()) throw new ServiceNotAvailableException();

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

    public void deactivateService(ServiceName serviceName) {
        Service service = getService(serviceName);
        if (service != null) service.deactivate();
    }

    public void initLocationServices(Location newLocation) {
        locationServices.put(newLocation.getId(), new ArrayList<>());
    }
}
