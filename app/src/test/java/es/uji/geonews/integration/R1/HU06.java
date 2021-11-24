package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU06 {
    GeocodeService geocodeServiceMocked;
    AirVisualService airVisualServiceMocked;
    OpenWeatherService openWeatherServiceMocked;
    private GeoNewsManager geoNewsManager;

    @Before
    public void init() throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        geocodeServiceMocked = mock(GeocodeService.class);
        airVisualServiceMocked = mock(AirVisualService.class);
        openWeatherServiceMocked = mock(OpenWeatherService.class);
        DatabaseManager databaseManagerMocked = mock(DatabaseManager.class);

        when(geocodeServiceMocked.getServiceName()).thenReturn(ServiceName.GEOCODE);
        when(airVisualServiceMocked.getServiceName()).thenReturn(ServiceName.AIR_VISUAL);
        when(openWeatherServiceMocked.getServiceName()).thenReturn(ServiceName.OPEN_WEATHER);

        when(geocodeServiceMocked.validateLocation(any())).thenReturn(true);
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);

        when(geocodeServiceMocked.getCoords("Castello de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.4702, -0.03768));

        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(airVisualServiceMocked.isAvailable()).thenReturn(true);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);

        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
        serviceManager.addService(airVisualServiceMocked);
        serviceManager.addService(openWeatherServiceMocked);
        LocationManager locationManager = new LocationManager(geocodeServiceMocked);
        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);
    }

    @Test
    public void activateLocation_KnownPlaceName_true()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        Location newLocation = geoNewsManager.addLocation("Castello de la Plana");
        // When
        boolean result = geoNewsManager.activateLocation(newLocation.getId());
        // Then
        assertTrue(result);
        assertTrue(geoNewsManager.getLocation(newLocation.getId()).isActive());
    }

    @Test
    public void activateLocation_UnKnownPlaceName_true()
            throws ServiceNotAvailableException, NotValidCoordinatesException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        when(geocodeServiceMocked.getPlaceName(any())).thenReturn(null);
        Location castellon = geoNewsManager.addLocation("Castello de la Plana");
        geoNewsManager.activateLocation(castellon.getId());

        GeographCoords coords = new GeographCoords(33.65001, -41.19001);
        Location newLocation = geoNewsManager.addLocation(coords.toString());
        // When
        boolean result = geoNewsManager.activateLocation(newLocation.getId());
        // Then
        assertTrue(result);
        assertTrue(geoNewsManager.getLocation(newLocation.getId()).isActive());
    }

    @Test
    public void activateLocation_LocationAlreadyActive_false()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castello de la Plana");
        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.activateLocation(castellon.getId());
        geoNewsManager.activateLocation(valencia.getId());

        // When
        boolean result = geoNewsManager.activateLocation(castellon.getId());
        // Then
        assertFalse(result);
        assertEquals(2, geoNewsManager.getActiveLocations().size());
    }
}
