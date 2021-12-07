package es.uji.geonews.model.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;

public class LocationManager {
    private Map<Integer, Location> locations;
    private final LocationCreator locationCreator;

    public LocationManager(GeocodeService geocodeService) {
        this.locations = new HashMap<>();
        this.locationCreator = new LocationCreator(geocodeService);
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

    /* This returns the list of active and not active locaitons*/
    public List<Location> getLocations(){
        return new ArrayList<>(locations.values());
    }

    public List<Location> getFavouriteLocations() {
        List<Location> favoriteLocations = new ArrayList<>();
        for (Location location: locations.values()){
            if (location.isFavorite()) favoriteLocations.add(location);
        }
        return favoriteLocations;
    }

    public Location createLocation(String location) throws NotValidCoordinatesException,
            ServiceNotAvailableException, UnrecognizedPlaceNameException {
        return locationCreator.createLocation(location);
    }

    public boolean addLocation(Location location) {
        if (location != null) {
            locations.put(location.getId(), location);
            return true;
        }
       return false;
    }

    public boolean removeLocation(int locationId){
        Location location = locations.get(locationId);
        if (location != null && !location.isActive()){
            locations.remove(locationId);
            return true;
        }
        return false;
    }

    public boolean addToFavorites(int locationId){
        Location location = locations.get(locationId);
        if (location != null && !location.isFavorite()){
            location.setIsFavorite(true);
            return true;
        }
        return false;
    }

    public boolean removeFromFavorites(int locationId){
        Location location = locations.get(locationId);
        if (location != null && location.isFavorite()){
            location.setIsFavorite(false);
            return true;
        }
        return false;
    }

    public boolean setAliasToLocation(String alias, int locationId) throws NoLocationRegisteredException {
        if (checkIfExistAlias(alias)) return false;
        Location location = getLocation(locationId);
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

    public Location getLocation(int idLocation) throws NoLocationRegisteredException {
        Location location = locations.get(idLocation);
        if (location == null) {
            throw new NoLocationRegisteredException();
        }
        return location;
    }

    public boolean activateLocation(int id) throws NoLocationRegisteredException {
        Location location = getLocation(id);
        if (location != null && !location.isActive()) {
            location.setIsActive(true);
            return true;
        }
        return false;
    }

    public boolean deactivateLocation(int id) {
        Location location = locations.get(id);
        if (location != null && location.isActive()){
            location.setIsActive(false);
            location.setIsFavorite(false);
            return true;
        }
        return false;
    }


    public void setLocations(Map<Integer, Location> locations) {
        this.locations = locations;
    }

    public int getLocationCounter(){
        return locationCreator.getIdLocationCounter();
    }

    public void setLocationCounter(int locationCounter){
        locationCreator.setIdLocationCounter(locationCounter);
    }
}

