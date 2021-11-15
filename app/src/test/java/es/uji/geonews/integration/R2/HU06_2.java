package es.uji.geonews.integration.R2;

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

public class HU06_2 {
    private GeoNewsManager geoNewsManager;
    private GeocodeService geocodeServiceMocked;
    private AirVisualService airVisualServiceMocked;
    private OpenWeatherService openWeatherServiceMocked;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        geocodeServiceMocked = mock(GeocodeService.class);
        airVisualServiceMocked = mock(AirVisualService.class);
        openWeatherServiceMocked = mock(OpenWeatherService.class);

        when(geocodeServiceMocked.getServiceName()).thenReturn(ServiceName.GEOCODE);
        when(airVisualServiceMocked.getServiceName()).thenReturn(ServiceName.AIR_VISUAL);
        when(openWeatherServiceMocked.getServiceName()).thenReturn(ServiceName.OPEN_WEATHER);

        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(airVisualServiceMocked.isAvailable()).thenReturn(true);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);

        when(geocodeServiceMocked.validateLocation(any())).thenReturn(true);
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);

        when(geocodeServiceMocked.getCoords("Castello de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.4702, -0.03768));

        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
        serviceManager.addService(openWeatherServiceMocked);
        serviceManager.addService(airVisualServiceMocked);
        LocationManager locationManager = new LocationManager(geocodeServiceMocked);
        geoNewsManager = new GeoNewsManager(locationManager, serviceManager);
    }

    @Test
    public void reactivateLocation_availableServices_true()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.activateLocation(valencia.getId());
        geoNewsManager.deactivateLocation(valencia.getId());

        // When
        boolean result = geoNewsManager.activateLocation(valencia.getId());

        // Then
        assertTrue(result);
    }

    @Test
    public void reactivateLocation_anyServices_false()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.activateLocation(castellon.getId());

        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.activateLocation(valencia.getId());
        geoNewsManager.deactivateLocation(valencia.getId());

        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(airVisualServiceMocked.isAvailable()).thenReturn(false);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);

        when(geocodeServiceMocked.validateLocation(any())).thenReturn(false);
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(false);

        // When
        boolean result = geoNewsManager.activateLocation(valencia.getId());

        // Then
        assertFalse(result);
        assertEquals(1, geoNewsManager.getActiveLocations().size());
    }
}
