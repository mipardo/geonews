package es.uji.geonews.integration.R4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
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
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU03_6 {
    private GeoNewsManager geoNewsManager;
    private LocalDBManager localDBManagerMocked;
    private RemoteDBManager remoteDBManagerMocked;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.getServiceName()).thenReturn(ServiceName.GEOCODE);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        // when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        // when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        LocationManager locationManager = new LocationManager(geocodeServiceMocked);

        OpenWeatherService openWeatherServiceMocked = mock(OpenWeatherService.class);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);
        when(openWeatherServiceMocked.getServiceName()).thenReturn(ServiceName.OPEN_WEATHER);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
        serviceManager.addService(openWeatherServiceMocked);

        localDBManagerMocked = spy(mock(LocalDBManager.class));
        remoteDBManagerMocked = spy(mock(RemoteDBManager.class));
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);
    }

    @Test
    public void activateLocationService_localAndRemoteDatabasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");

        // When
        boolean result = geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);

        // Then
        assertTrue(result);
        // Se llama dos veces, una por el addLocation y el otro por el addServiceToLocation
        verify(localDBManagerMocked, times(2)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(2)).saveAll(any(), any(), any());
    }

    @Test
    public void activateLocationService_localDBNotAvailableAndRemoteDBAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {

    }

    @Test
    public void activateLocationService_localDBAvailableAndRemoteDBNotAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {

    }

    @Test
    public void activateLocationService_localAndRemoteDBNotAvailable_false()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {

    }

    @Test
    public void activateLocationService_localAndRemoteDatabasesAvailable_false()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);

        // When
        boolean result = geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);

        // Then
        assertFalse(result);
        // Se llama dos veces, una por el addLocation y el otro por el addServiceToLocation del Given
        verify(localDBManagerMocked, times(2)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(2)).saveAll(any(), any(), any());
    }
}
