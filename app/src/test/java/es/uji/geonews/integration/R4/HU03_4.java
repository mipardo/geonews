package es.uji.geonews.integration.R4;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

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
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU03_4 {
    private GeoNewsManager geoNewsManager;
    LocationManager locationManager;
    ServiceManager serviceManager;
    private LocalDBManager localDBManagerMocked;
    private RemoteDBManager remoteDBManagerMocked;
    GeocodeService geocodeServiceMocked;
    AirVisualService airVisualServiceMocked;
    OpenWeatherService openWeatherServiceMocked;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        geocodeServiceMocked = mock(GeocodeService.class);
        airVisualServiceMocked = mock(AirVisualService.class);
        openWeatherServiceMocked = mock(OpenWeatherService.class);

        when(geocodeServiceMocked.getServiceName()).thenReturn(ServiceName.GEOCODE);
        when(airVisualServiceMocked.getServiceName()).thenReturn(ServiceName.AIR_VISUAL);
        when(openWeatherServiceMocked.getServiceName()).thenReturn(ServiceName.OPEN_WEATHER);

        when(geocodeServiceMocked.validateLocation(any())).thenReturn(true);
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);


        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(airVisualServiceMocked.isAvailable()).thenReturn(true);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);

        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));

        locationManager = new LocationManager(geocodeServiceMocked);

        serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
        serviceManager.addService(airVisualServiceMocked);
        serviceManager.addService(openWeatherServiceMocked);
    }

    @Test
    public void activateLocationService_localAndRemoteDatabasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        localDBManagerMocked = mock(LocalDBManager.class);
        remoteDBManagerMocked = mock(RemoteDBManager.class);
        when(localDBManagerMocked.isAvailable()).thenReturn(true);
        when(remoteDBManagerMocked.isAvailable()).thenReturn(true);
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);

        // Given
        Location Castellon =geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.activateLocation(Castellon.getId());

        // When
        geoNewsManager.deactivateLocation(Castellon.getId());

        // Then

        // Se llama dos veces, una por el addLocation y el otro por el addServiceToLocation
        verify(localDBManagerMocked, times(3)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(3)).saveAll(any(), any(), any());
    }

    @Test
    public void activateLocationService_localDBAvailableAndRemoteDBNotAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        localDBManagerMocked = mock(LocalDBManager.class);
        remoteDBManagerMocked = mock(RemoteDBManager.class);
        when(localDBManagerMocked.isAvailable()).thenReturn(true);
        when(remoteDBManagerMocked.isAvailable()).thenReturn(false);
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);

        // Given
        Location Castellon =geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.activateLocation(Castellon.getId());

        // When
        geoNewsManager.deactivateLocation(Castellon.getId());

        // Then

        // Se llama dos veces, una por el addLocation y el otro por el addServiceToLocation
        verify(localDBManagerMocked, times(3)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(0)).saveAll(any(), any(), any());
    }

    @Test
    public void activateLocationService_localAndRemoteDatabasesAvailable_false()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        localDBManagerMocked = mock(LocalDBManager.class);
        remoteDBManagerMocked = mock(RemoteDBManager.class);
        when(localDBManagerMocked.isAvailable()).thenReturn(true);
        when(remoteDBManagerMocked.isAvailable()).thenReturn(true);
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);
        // Given
        // Given
        Location Castellon =geoNewsManager.addLocation("Castelló de la Plana");

        // When
        geoNewsManager.deactivateLocation(Castellon.getId());

        // Then

        // Se llama dos veces, una por el addLocation y el otro por el addServiceToLocation del Given
        verify(localDBManagerMocked, times(1)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(1)).saveAll(any(), any(), any());
    }
}
