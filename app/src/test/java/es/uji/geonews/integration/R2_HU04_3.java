package es.uji.geonews.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU04_3 {
    private LocationManager locationManager;
    private CoordsSearchService coordsSearchServiceMocked;
    private ServiceManager serviceManagerMocked;

    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        when(serviceManagerMocked.getService("Geocode")).thenReturn(coordsSearchServiceMocked);
        locationManager = new LocationManager(serviceManagerMocked);
    }


    @Test
    public void removeFavoriteLocation_IsFavorite_True()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(coordsSearchServiceMocked.getCoordsFrom("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(coordsSearchServiceMocked.getCoordsFrom("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        Location valencia = locationManager.addLocation("Valencia");
        Location alicante = locationManager.addLocation("Alicante");
        locationManager.addToFavorites(valencia.getId());
        locationManager.addToFavorites(alicante.getId());
        // Act
        boolean addedCastellonToFavorites = locationManager.addToFavorites(castellon.getId());
        // Assert
        assertTrue(addedCastellonToFavorites);
        assertEquals(3, locationManager.getFavouriteLocations().size());
    }


    @Test
    public void removeFavoriteLocation_IsNotFavorite_False()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(coordsSearchServiceMocked.getCoordsFrom("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(coordsSearchServiceMocked.getCoordsFrom("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        Location valencia = locationManager.addLocation("Valencia");
        locationManager.addToFavorites(castellon.getId());
        locationManager.addToFavorites(valencia.getId());
        // Act
        boolean addedCastellonToFavorites = locationManager.addToFavorites(castellon.getId());
        // Assert
        assertFalse(addedCastellonToFavorites);
        assertEquals(2, locationManager.getFavouriteLocations().size());
    }

}
