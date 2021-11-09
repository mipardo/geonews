package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU04_1 {
    private LocationManager locationManager;
    private GeocodeService geocodeServiceMocked;
    private ServiceManager serviceManagerMocked;

    @Before
    public void init(){
        geocodeServiceMocked = mock(GeocodeService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        when(serviceManagerMocked.getService("Geocode")).thenReturn(geocodeServiceMocked);
        locationManager = new LocationManager(serviceManagerMocked);
    }


    @Test
    public void getFavoriteLocations_AllAreFavorite_ListWithThreeLocations()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        Location valencia = locationManager.addLocation("Valencia");
        Location alicante = locationManager.addLocation("Alicante");
        locationManager.addToFavorites(castellon.getId());
        locationManager.addToFavorites(valencia.getId());
        locationManager.addToFavorites(alicante.getId());
        // Act
        List<Location> favorites = locationManager.getFavouriteLocations();
        // Assert
        assertEquals(3, favorites.size());
    }

    @Test
    public void getFavoriteLocations_SomeAreFavorite_ListWithOneLocation()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        Location alicante = locationManager.addLocation("Alicante");
        locationManager.addToFavorites(castellon.getId());
        // Act
        List<Location> favorites = locationManager.getFavouriteLocations();
        // Assert
        assertEquals(1, favorites.size());
        assertEquals("Castelló de la Plana", favorites.get(0).getPlaceName());
    }

    @Test
    public void getFavoriteLocations_AnyAreFavorite_EmtpyList()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        // Act
        List<Location> favorites = locationManager.getFavouriteLocations();
        // Assert
        assertEquals(0, favorites.size());
    }

    @Test(expected = NoLocationRegisteredException.class)
    public void getFavoriteLocations_NoLocations_NoLocationRegisteredException()
            throws NoLocationRegisteredException {
            // Act
            List<Location> favorites = locationManager.getFavouriteLocations();
            // Assert
            assertEquals(0, favorites.size());
    }

}
