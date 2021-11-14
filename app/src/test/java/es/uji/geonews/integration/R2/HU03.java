package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;

public class HU03 {
    private LocationManager locationManager;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        locationManager = new LocationManager(geocodeServiceMocked);
    }

    @Test
    public void checkListActiveLocations_threeActiveLocations()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        Location valencia = locationManager.addLocation("Valencia");
        Location alicante = locationManager.addLocation("Alicante");
        locationManager.activateLocation(castellon.getId());
        locationManager.activateLocation(valencia.getId());
        locationManager.activateLocation(alicante.getId());
        // Act
        List<Location> activeLocations = locationManager.getActiveLocations();
        // Assert
        assertEquals(3, activeLocations.size());
    }

    @Test
    public void checkListActiveLocations_oneActiveLocations_OneLocationList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        Location alicante = locationManager.addLocation("Alicante");
        locationManager.activateLocation(castellon.getId());
        // Act
        List<Location> activeLocations = locationManager.getActiveLocations();
        // Assert
        assertEquals(1, activeLocations.size());
    }

    @Test
    public void checkListActiveLocations_noneActiveLocations_EmptyList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        // Act
        List<Location> activeLocations = locationManager.getActiveLocations();
        // Assert
        assertEquals(0, activeLocations.size());
    }

    @Test(expected = NoLocationRegisteredException.class)
    public void checkListActiveLocations_noneLocations_NoLocationRegisteredException()
            throws NoLocationRegisteredException {
        // Act
        List<Location> activeLocations = locationManager.getNonActiveLocations();
    }
}
