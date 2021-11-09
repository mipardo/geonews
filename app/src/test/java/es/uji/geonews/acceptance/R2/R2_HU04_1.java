package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU04_1 {

    private LocationManager locationManager;

    @Before
    public void init(){
        // Given
        Service coordsSearchSrv = new GeocodeService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
        locationManager = new LocationManager(serviceManager);
    }

    @Test
    public void getFavoriteLocations_AllAreFavorite_ListWithThreeLocations()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        Location castellon = locationManager.addLocation("39.99207, -0.03621");
        Location valencia = locationManager.addLocation("39.50337, -0.40466");
        Location alicante = locationManager.addLocation("38.53996, -0.50579");
        locationManager.addToFavorites(castellon.getId());
        locationManager.addToFavorites(valencia.getId());
        locationManager.addToFavorites(alicante.getId());
        // When
        List<Location> favouriteLocations = locationManager.getFavouriteLocations();
        // Then
        assertEquals(3, favouriteLocations.size());
    }

    @Test
    public void getFavoriteLocations_SomeAreFavorite_ListWithOneLocation()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        Location castellon = locationManager.addLocation("39.99207, -0.03621");
        Location alicante = locationManager.addLocation("38.53996, -0.50579");
        locationManager.addToFavorites(castellon.getId());
        // When
        List<Location> favouriteLocations = locationManager.getFavouriteLocations();
        // Then
        assertEquals(1, favouriteLocations.size());
        assertEquals(castellon.getPlaceName(), favouriteLocations.get(0).getPlaceName());
    }

    @Test
    public void getFavoriteLocations_AnyAreFavorite_EmtpyList()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        locationManager.addLocation("39.99207, -0.03621");
        // When
        List<Location> favouriteLocations = locationManager.getFavouriteLocations();
        // Then
        assertEquals(0, favouriteLocations.size());
    }

    @Test(expected = NoLocationRegisteredException.class)
    public void getFavoriteLocations_NoLocations_NoLocationRegisteredException()
            throws NoLocationRegisteredException {
        // When
        locationManager.getFavouriteLocations();
    }


}
