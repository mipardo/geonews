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

    private Map<ServiceName, Service> services;
    private Map<Integer, List<ServiceName>> locationServices;
    private final ContextDataGetter contextDataGetter;
    private Map<Integer, Map<ServiceName, Data>> lastData; // This is the last data loaded from services

    public ServiceManager(){
        this.services = new HashMap<>();
        this.locationServices = new HashMap<>();
        this.contextDataGetter = new ContextDataGetter();
        this.lastData = new HashMap<>();
    }

    public Map<String, String> getServicesDescription() throws ServiceNotAvailableException {
        if (services.isEmpty()) {
            throw new ServiceNotAvailableException();
        }
        Map<String, String> servicesDescription = new HashMap<>();
        for (ServiceName serviceName : getPublicServices()) {
            servicesDescription.put(serviceName.name, this.services.get(serviceName).getDescription());
        }
        return servicesDescription;
    }

    public List<ServiceHttp> getHttpServices(){
        List<ServiceHttp> httpServices = new ArrayList<>();
        for(Service service: services.values()){
            if(service instanceof ServiceHttp){
                httpServices.add((ServiceHttp) service);
            }
        }
        return httpServices;
    }

    public Service getService(ServiceName serviceName) {
        return services.get(serviceName);
    }

    public void addService(Service service){
        services.put(service.getServiceName(), service);
    }


    public Data getData(ServiceName serviceName, Location location) throws ServiceNotAvailableException {
        List<ServiceName> activeServices = locationServices.get(location.getId());

        if (!services.get(serviceName).isAvailable()) {
            throw new ServiceNotAvailableException();
        }

        if (location != null && activeServices.contains(serviceName)) {
            DataGetterStrategy service = (DataGetterStrategy) getService(serviceName);
            contextDataGetter.setService(service);
            Data lastLocationServiceData = contextDataGetter.getData(location);
            Map<ServiceName, Data> serviceData = lastData.get(location.getId());
            if (serviceData == null) {
                serviceData = new HashMap<>();
            }
            serviceData.put(serviceName, lastLocationServiceData);
            lastData.put(location.getId(), serviceData);
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
        // TODO: YO miraría de no devolver excepción && cambiar isAvailable por is asctive
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

    public List<ServiceName> getPublicServices(){
        List<ServiceName> publicServices = new ArrayList<>();
        for(Service service: services.values()){
            if(service instanceof ServiceHttp && !service.getServiceName().equals(ServiceName.GEOCODE)){
                publicServices.add(service.getServiceName());
            }
        }
        return publicServices;
    }

    public List<ServiceName> getActiveServices() {
        List<ServiceName> activeServices = new ArrayList<>();
        for (Service service: services.values()) {
            if (service.isActive()) {
                activeServices.add(service.getServiceName());
            }
        }
        return activeServices;
    }

    public Map<Integer, Map<ServiceName, Data>> getLastData() {
        return lastData;
    }

    public Map<ServiceName, Service> getServices() {
        return services;
    }

    public Map<Integer, List<ServiceName>> getLocationServices() {
        return locationServices;
    }

    public void setServices(Map<ServiceName, Service> services) {
        this.services = services;
    }

    public void setLocationServices(Map<Integer, List<ServiceName>> locationServices) {
        this.locationServices = locationServices;
    }

    public void setLastData(Map<Integer, Map<ServiceName, Data>> lastData) {
        this.lastData = lastData;
    }
}
