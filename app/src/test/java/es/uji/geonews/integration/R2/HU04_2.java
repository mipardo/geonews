package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.GeocodeService;

public class HU04_2 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        DatabaseManager databaseManagerMocked = mock(DatabaseManager.class);

        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));

        LocationManager locationManager = new LocationManager(geocodeServiceMocked);
        ServiceManager serviceManager = new ServiceManager();
        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);
    }


    @Test
    public void removeFavoriteLocation_IsFavorite_True()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        Location valencia = geoNewsManager.addLocation("Valencia");
        Location alicante = geoNewsManager.addLocation("Alicante");
        geoNewsManager.addToFavorites(castellon.getId());
        geoNewsManager.addToFavorites(valencia.getId());
        geoNewsManager.addToFavorites(alicante.getId());
        // Act
        boolean removedCastellonFromFavorites = geoNewsManager.removeFromFavorites(castellon.getId());
        // Assert
        assertTrue(removedCastellonFromFavorites);
        assertEquals(2, geoNewsManager.getFavouriteLocations().size());
    }


    @Test
    public void removeFavoriteLocation_IsNotFavorite_False()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        Location valencia = geoNewsManager.addLocation("Valencia");
        // Act
        boolean removedCastellonFromFavorites = geoNewsManager.removeFromFavorites(castellon.getId());
        // Assert
        assertFalse(removedCastellonFromFavorites);
        assertEquals(0, geoNewsManager.getFavouriteLocations().size());
    }

}
