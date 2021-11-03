package es.uji.geonews.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class LocationManager {
    private final Map<Integer, Location> locations;
    private final Map<Integer, Location> favoriteLocations;
    private final ServiceManager serviceManager;
    private LocationFactory locationFactory;

    public LocationManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        this.locations = new HashMap<>();
        this.favoriteLocations = new HashMap<>();
        CoordsSearchService coordsSearchService = (CoordsSearchService) serviceManager.getService("Geocode");
        this.locationFactory = new LocationFactory(coordsSearchService);
    }

    public List<Location> getActiveLocations() {
        List<Location> activeLocations = new ArrayList<>();
        for (Location location: locations.values()){
            if (location.isActive()) activeLocations.add(location);
        }
        return activeLocations;
    }

    public List<Location> getNonActiveLocations() {
        List<Location> nonActiveLocations = new ArrayList<>();
        for (Location location: locations.values()){
            if (!location.isActive()) nonActiveLocations.add(location);
        }
        return nonActiveLocations;
    }

    public List<Location> getFavouriteLocations() {
        return new ArrayList<>(favoriteLocations.values());    }

    public Location addLocation(String string) throws UnrecognizedPlaceNameException,
            ServiceNotAvailableException, NotValidCoordinatesException {
        Location location = locationFactory.createLocation(string);
        if (location!= null) locations.put(location.getId(), location);
       return location;
    }

    public boolean removeLocation(int locationId){
        Location location = locations.get(locationId);
        if (location != null && !location.isActive()){
            locations.remove(locationId);
            return true;
        }
        return false;
    }

    public List<String> validateLocation(int locationId){
        Location location = locations.get(locationId);
        List<String> services = new ArrayList<>();
        for(Service service: serviceManager.getServices()){
            if(!service.getServiceName().equals("Geocode") && service.validateLocation(location)){
                services.add(service.getServiceName());
            }
        }
        return services;
    }

    public boolean setAliasToLocation(String alias, int locationId){
        //Check if alias is already asign to any other locaton
        if (checkIfExistAlias(alias)) return false;

        //Check if location is in active and inactive locations
        Location location = getLocaton(locationId);

        if (alias != null && !alias.equals("")) {
            location.setAlias(alias);
            return true;
        }
        return false;
    }

    private boolean checkIfExistAlias(String newAlias){
        for(Location location: locations.values()){
            if (location.getAlias().equals(newAlias)){
                return true;
            }
        }
        return false;
    }

    public Location getLocaton(int idLocation){
        return locations.get(idLocation);
    }

    public boolean activateLocation(int id) {
        Location location = locations.get(id);
        if (location != null && !location.isActive()){
            return location.activate();
        }
        return false;
    }

    public boolean deactivateLocation(int id) {
        Location location = locations.get(id);
        if (location != null && location.isActive()){
            return location.deactivate();
        }
        return false;
    }

    public Service getService(String serviceName) {
        return serviceManager.getService(serviceName);
    }

}
