package es.uji.geonews.model;

import java.time.LocalDate;
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
    private final Map<Integer, Location> activeLocations;
    private final Map<Integer, Location> nonActiveLocations;
    private final Map<Integer, Location> favoriteLocations;
    private final ServiceManager serviceManager;
    private final CoordsSearchService coordsSearchService;
    private int idLocationCounter;

    public LocationManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        activeLocations = new HashMap<>();
        nonActiveLocations = new HashMap<>();
        favoriteLocations = new HashMap<>();
        coordsSearchService = (CoordsSearchService) serviceManager.getService("Geocode");
        idLocationCounter = 1; // TODO: Recojer de la DB

    }

    public List<Location> getActiveLocations() {
        return new ArrayList<>(activeLocations.values());
    }

    public List<Location> getNonActiveLocations() {
        return new ArrayList<>(nonActiveLocations.values());
    }

    public List<Location> getFavouriteLocations() {
        return new ArrayList<>(favoriteLocations.values());    }

    public Location addLocationByPlaceName(String placeName)
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        if(coordsSearchService.isAvailable()){
            GeographCoords coords = coordsSearchService.getCoordsFrom(placeName);
            Location location = new Location(idLocationCounter++,placeName, coords, LocalDate.now());
            nonActiveLocations.put(location.getId(), location);
            return location;
        }
        return null;
    }

    public Location addLocationByCoords(GeographCoords coords)
            throws NotValidCoordinatesException, ServiceNotAvailableException, GPSNotAvailableException {
        if (coords == null) {
            throw new GPSNotAvailableException();
        }

        if(!areValidCoords(coords)){
            throw new NotValidCoordinatesException();
        }

        if (!hasEnoughPrecision(coords)) {
            return null;
        }

        if (coordsSearchService.isAvailable()) {
            String placeName = coordsSearchService.getPlaceNameFromCoords(coords);
            Location location = new Location(idLocationCounter++, placeName, coords, LocalDate.now());
            nonActiveLocations.put(location.getId(), location);
            return location;
        }

        return null;
    }

    public boolean removeLocation(int locationId){
        if ( nonActiveLocations.containsKey(locationId)){
            nonActiveLocations.remove(locationId);
            return true;
        }
        return false;
    }

    public List<String> validateLocation(int locationId){
        Location location = nonActiveLocations.get(locationId);
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
        for(Location location: activeLocations.values()){
            if (location.getAlias().equals(newAlias)){
                return true;
            }
        }
        for(Location location: nonActiveLocations.values()){
            if (location.getAlias().equals(newAlias)){
                return true;
            }
        }
        return false;
    }

    public Location getLocaton(int idLocation){
        Location location = activeLocations.get(idLocation);
        if (location == null){
            location = nonActiveLocations.get(idLocation);
        }
        return location;
    }

    private boolean areValidCoords(GeographCoords coords){
        return (coords.getLatitude() < 90 && coords.getLatitude() > -90 &&
                coords.getLongitude()<180 && coords.getLongitude()>-180);
    }

    private boolean hasEnoughPrecision(GeographCoords coords) {
        String latString = String.valueOf(coords.getLatitude()).split("\\.")[1];
        String lonString = String.valueOf(coords.getLatitude()).split("\\.")[1];

        return latString.length() >= 4 && lonString.length() >= 4;
    }

    public boolean activateLocation(int id) {
        Location location = nonActiveLocations.get(id);
        if (location != null) {
            activeLocations.put(id, location);
            nonActiveLocations.remove(id);
            return  true;
        }
        return false;
    }

    public boolean deactivateLocation(int id) {
        Location location = activeLocations.get(id);
        if (location != null) {
            nonActiveLocations.put(id, location);
            activeLocations.remove(id);
            return  true;
        }
        return false;
    }

    public boolean hasActiveLocation(int id) {
        return activeLocations.containsKey(id);
    }

    public boolean hasNonActiveLocation(int id) {
        return nonActiveLocations.containsKey(id);
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

}
