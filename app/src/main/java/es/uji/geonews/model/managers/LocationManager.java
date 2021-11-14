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
    private final Map<Integer, Location> locations;
    private final Map<Integer, Location> favoriteLocations;
    private final LocationCreator locationCreator;

    public LocationManager(GeocodeService geocodeService) {
        this.locations = new HashMap<>();
        this.favoriteLocations = new HashMap<>();
        this.locationCreator = new LocationCreator(geocodeService);
    }

    public List<Location> getActiveLocations() {
        List<Location> activeLocations = new ArrayList<>();
        for (Location location: locations.values()){
            if (location.isActive()) activeLocations.add(location);
        }
        return activeLocations;
    }

    public List<Location> getNonActiveLocations() throws NoLocationRegisteredException {
        if (locations.isEmpty() && favoriteLocations.isEmpty()) throw new NoLocationRegisteredException();

        List<Location> nonActiveLocations = new ArrayList<>();
        for (Location location: locations.values()){
            if (!location.isActive()) nonActiveLocations.add(location);
        }
        return nonActiveLocations;
    }

    public List<Location> getFavouriteLocations() throws NoLocationRegisteredException {
        if(favoriteLocations.isEmpty() && locations.isEmpty()) throw new NoLocationRegisteredException();
        return new ArrayList<>(favoriteLocations.values());
    }

    public Location createLocation(String location)
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        return locationCreator.createLocation(location);
    }

    public boolean addLocation(Location location) {
        if (location != null) {
            locations.put(location.getId(), location);
            return true;
        }
       return false;
    }

    //TODO: CUIDADO PREGUNTAR
    public Location addLocation(String location) throws UnrecognizedPlaceNameException,
            ServiceNotAvailableException, NotValidCoordinatesException {
        Location newLocation = createLocation(location);
        if (location!= null) {
            locations.put(newLocation.getId(), newLocation);
            return newLocation;
        }
        return null;
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
        if (location != null){
            favoriteLocations.put(locationId, location);
            locations.remove(locationId);
            return true;
        }
        return false;
    }

    public boolean removeFromFavorites(int locationId){
        Location location = favoriteLocations.get(locationId);
        if (location != null){
            locations.put(locationId, location);
            favoriteLocations.remove(locationId);
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
            getLocation(id).activate();
            return true;
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
}

