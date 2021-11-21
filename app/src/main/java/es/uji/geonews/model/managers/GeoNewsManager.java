package es.uji.geonews.model.managers;

import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.database.LocalDBManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.GpsService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceName;

public class GeoNewsManager {

    private LocationManager locationManager;
    private ServiceManager serviceManager;
    private DatabaseManager databaseManager;
    private LocalDBManager localDBManager;

    public GeoNewsManager(){
        databaseManager = new DatabaseManager();
        serviceManager = new ServiceManager();
        localDBManager = new LocalDBManager();
        serviceManager.addService(new GpsService());
        serviceManager.addService(new AirVisualService());
        serviceManager.addService(new CurrentsService());
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new GeocodeService());
        locationManager = new LocationManager((GeocodeService) serviceManager.getService(ServiceName.GEOCODE));
    }

    public GeoNewsManager(LocationManager locationManager, ServiceManager serviceManager) {
        this.locationManager = locationManager;
        this.serviceManager = serviceManager;
    }

    public Location addLocation(String location)
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException {
        Location newLocation = locationManager.createLocation(location);
        boolean added = locationManager.addLocation(newLocation);

        if (added){
            localDBManager.saveAll(1,locationManager,serviceManager);
            serviceManager.initLocationServices(newLocation);
            //databaseManager.saveLocation(newLocation);
            return newLocation;
        }
        return null;
    }

    public boolean addService(ServiceHttp service) {
        if (service.checkConnection()) {
            serviceManager.addService(service);
            return true;
        }
        return false;
    }

    public boolean activateLocation(int id) throws NoLocationRegisteredException {
        Location location = locationManager.getLocation(id);
        if (serviceManager.validateLocation(location).size() > 0) {
            return locationManager.activateLocation(id);
        }
        return false;
    }

    public void deactivateLocation(int id) {
        locationManager.deactivateLocation(id);
    }

    public void deactivateService(ServiceName service) {
        serviceManager.deactivateService(service);
    }

    public List<Location> getActiveLocations() {
        return locationManager.getActiveLocations();
    }

    public List<Location> getNonActiveLocations() throws NoLocationRegisteredException {
        return locationManager.getNonActiveLocations();
    }

    public Location getLocation(int id) throws NoLocationRegisteredException {
        return  locationManager.getLocation(id);
    }

    public boolean addServiceToLocation(ServiceName serviceName, Location location)
            throws ServiceNotAvailableException {
        return serviceManager.addServiceToLocation(serviceName, location);
    }

    public Data getData(ServiceName serviceName, Location location)
            throws ServiceNotAvailableException {
        return serviceManager.getData(serviceName, location);
    }

    public List<ServiceName> getServicesOfLocation(int id) {
        return serviceManager.getServicesOfLocation(id);
    }

    public boolean removeServiceFromLocation(ServiceName serviceName, Location location) {
        return serviceManager.removeServiceFromLocation(serviceName, location);
    }

    public boolean activateService(ServiceName serviceName) {
        return serviceManager.activateService(serviceName);
    }

    public Service getService(ServiceName serviceName) {
        return serviceManager.getService(serviceName);
    }

    public void saveAllData() {
        databaseManager.saveData(locationManager, serviceManager);
    }
}
