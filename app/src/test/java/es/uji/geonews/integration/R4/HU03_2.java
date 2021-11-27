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
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.ServiceName;

public class HU03_2 {
    private GeoNewsManager geoNewsManager;
    LocationManager locationManager;
    ServiceManager serviceManager;
    private LocalDBManager localDBManagerMocked;
    private RemoteDBManager remoteDBManagerMocked;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.getServiceName()).thenReturn(ServiceName.GEOCODE);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        locationManager = new LocationManager(geocodeServiceMocked);

        serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
    }

    @Test
    public void activateLocationService_localAndRemoteDatabasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        localDBManagerMocked = mock(LocalDBManager.class);
        remoteDBManagerMocked = mock(RemoteDBManager.class);
        when(localDBManagerMocked.isAvailable()).thenReturn(true);
        when(remoteDBManagerMocked.isAvailable()).thenReturn(true);
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);

        // Given
        Location Castellon =geoNewsManager.addLocation("Castelló de la Plana");

        // When
        geoNewsManager.removeLocation(Castellon.getId());

        // Then

        // Se llama dos veces, una por el addLocation y el otro por el addServiceToLocation
        verify(localDBManagerMocked, times(2)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(2)).saveAll(any(), any(), any());
    }

    @Test
    public void activateLocationService_localDBAvailableAndRemoteDBNotAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        localDBManagerMocked = mock(LocalDBManager.class);
        remoteDBManagerMocked = mock(RemoteDBManager.class);
        when(localDBManagerMocked.isAvailable()).thenReturn(true);
        when(remoteDBManagerMocked.isAvailable()).thenReturn(false);
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);

        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");

        // When
        geoNewsManager.removeLocation(castellon.getId());

        // Then

        // Se llama dos veces, una por el addLocation y el otro por el addServiceToLocation
        verify(localDBManagerMocked, times(2)).saveAll(any(), any(), any());
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
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");

        geoNewsManager.removeLocation(castellon.getId());

        // When
        geoNewsManager.removeLocation(castellon.getId());

        // Then

        // Se llama dos veces, una por el addLocation y el otro por el addServiceToLocation del Given
        verify(localDBManagerMocked, times(2)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(2)).saveAll(any(), any(), any());
    }
}
