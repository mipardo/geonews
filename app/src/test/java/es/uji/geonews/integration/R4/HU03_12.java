package es.uji.geonews.integration.R4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.database.LocalDBManager;
import es.uji.geonews.model.database.RemoteDBManager;
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

public class HU03_12 {
    private GeoNewsManager geoNewsManager;
    LocationManager locationManager;
    ServiceManager serviceManager;
    private LocalDBManager localDBManagerMocked;
    private RemoteDBManager remoteDBManagerMocked;
    private AirVisualService airVisualServiceMocked;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.getServiceName()).thenReturn(ServiceName.GEOCODE);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        locationManager = new LocationManager(geocodeServiceMocked);

        OpenWeatherService openWeatherServiceMocked = mock(OpenWeatherService.class);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);
        when(openWeatherServiceMocked.getServiceName()).thenReturn(ServiceName.OPEN_WEATHER);
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        when(openWeatherServiceMocked.checkConnection()).thenReturn(true);
        airVisualServiceMocked = mock(AirVisualService.class);
        when(airVisualServiceMocked.isAvailable()).thenReturn(true);
        when(airVisualServiceMocked.getServiceName()).thenReturn(ServiceName.AIR_VISUAL);
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);
        when(airVisualServiceMocked.isActive()).thenReturn(false);
        serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
        serviceManager.addService(airVisualServiceMocked);
        serviceManager.addService(openWeatherServiceMocked);
        serviceManager.activateService(ServiceName.AIR_VISUAL);
    }

    @Test
    public void deactivateService_localAndRemoteDatabasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException {
        // Given
        localDBManagerMocked = mock(LocalDBManager.class);
        when(localDBManagerMocked.isAvailable()).thenReturn(true);
        remoteDBManagerMocked = mock(RemoteDBManager.class);
        when(remoteDBManagerMocked.isAvailable()).thenReturn(true);
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);

        geoNewsManager.addLocation("Castelló de la Plana");
        when(airVisualServiceMocked.isActive()).thenReturn(true);
        // When
        boolean result = geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);

        // Then
        assertTrue(result);
        verify(localDBManagerMocked, times(2)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(2)).saveAll(any(), any(), any());
    }

    @Test
    public void deactivateService_localDBAvailableAndRemoteDBNotAvailable_true() throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        localDBManagerMocked = mock(LocalDBManager.class);
        when(localDBManagerMocked.isAvailable()).thenReturn(true);
        remoteDBManagerMocked = mock(RemoteDBManager.class);
        when(remoteDBManagerMocked.isAvailable()).thenReturn(false);
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);

        geoNewsManager.addLocation("Castelló de la Plana");
        when(airVisualServiceMocked.isActive()).thenReturn(true);
        // When
        boolean result = geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);

        // Then
        assertTrue(result);
        verify(localDBManagerMocked, times(2)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(0)).saveAll(any(), any(), any());
    }

    @Test
    public void deactivateService_localAndRemoteDatabasesAvailable_false()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException {
        // Given
        localDBManagerMocked = mock(LocalDBManager.class);
        when(localDBManagerMocked.isAvailable()).thenReturn(true);
        remoteDBManagerMocked = mock(RemoteDBManager.class);
        when(remoteDBManagerMocked.isAvailable()).thenReturn(true);
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);

        geoNewsManager.addLocation("Castelló de la Plana");

        // When
        boolean result = geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);

        // Then
        assertFalse(result);
        verify(localDBManagerMocked, times(1)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(1)).saveAll(any(), any(), any());
    }
}
