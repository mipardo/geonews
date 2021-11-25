package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.ServiceName;

public class HU03 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        DatabaseManager databaseManagerMocked = mock(DatabaseManager.class);
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.getServiceName()).thenReturn(ServiceName.GEOCODE);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));

        AirVisualService airVisualServiceMocked = mock(AirVisualService.class);
        when(airVisualServiceMocked.getServiceName()).thenReturn(ServiceName.AIR_VISUAL);
        when(airVisualServiceMocked.isAvailable()).thenReturn(true);
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);

        LocationManager locationManager = new LocationManager(geocodeServiceMocked);
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
        serviceManager.addService(airVisualServiceMocked);
        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);
    }

    @Test
    public void checkListActiveLocations_threeActiveLocations()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        Location valencia = geoNewsManager.addLocation("Valencia");
        Location alicante = geoNewsManager.addLocation("Alicante");
        geoNewsManager.activateLocation(castellon.getId());
        geoNewsManager.activateLocation(valencia.getId());
        geoNewsManager.activateLocation(alicante.getId());
        // Act
        List<Location> activeLocations = geoNewsManager.getActiveLocations();
        // Assert
        assertEquals(3, activeLocations.size());
    }

    @Test
    public void checkListActiveLocations_oneActiveLocations_OneLocationList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        Location alicante = geoNewsManager.addLocation("Alicante");
        geoNewsManager.activateLocation(castellon.getId());
        // Act
        List<Location> activeLocations = geoNewsManager.getActiveLocations();
        // Assert
        assertEquals(1, activeLocations.size());
    }

    @Test
    public void checkListActiveLocations_noneActiveLocations_EmptyList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        // Act
        List<Location> activeLocations = geoNewsManager.getActiveLocations();
        // Assert
        assertEquals(0, activeLocations.size());
    }

    @Test
    public void checkListActiveLocations_noneLocations_NoLocationRegisteredException()
            throws NoLocationRegisteredException {
        // Act
        List<Location> activeLocations = geoNewsManager.getNonActiveLocations();
        //Assert
        assertEquals(0, activeLocations.size());
    }
}
