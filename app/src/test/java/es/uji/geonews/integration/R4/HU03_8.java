package es.uji.geonews.integration.R4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.ServiceName;

public class HU03_8 {
    private GeoNewsManager geoNewsManager;
    LocationManager locationManager;
    ServiceManager serviceManager;
    private LocalDBManager localDBManagerMocked;
    private RemoteDBManager remoteDBManagerMocked;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.getServiceName()).thenReturn(ServiceName.GEOCODE);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        locationManager = new LocationManager(geocodeServiceMocked);

        serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
    }

    @Test
    public void removeLocationTFromFavorite_localAndRemoteDatabasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Given
        localDBManagerMocked = spy(mock(LocalDBManager.class));
        remoteDBManagerMocked = spy(mock(RemoteDBManager.class));
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);

        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        Location valencia = geoNewsManager.addLocation("Valencia");
        Location alicante = geoNewsManager.addLocation("Alicante");
        geoNewsManager.addToFavorites(valencia.getId());
        geoNewsManager.addToFavorites(alicante.getId());
        geoNewsManager.addToFavorites(castellon.getId());

        // When
        boolean result = geoNewsManager.removeFromFavorites(castellon.getId());

        // Then
        assertTrue(result);
        verify(localDBManagerMocked, times(7)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(7)).saveAll(any(), any(), any());
    }

    @Test
    public void removeLocationTFromFavorite_localDBNotAvailableAndRemoteDBAvailable_true() {

    }

    @Test
    public void removeLocationTFromFavorite_localDBAvailableAndRemoteDBNotAvailable_true() {

    }

    @Test
    public void removeLocationTFromFavorite_localAndRemoteDBNotAvailable_false() {

    }

    @Test
    public void removeLocationTFromFavorite_localAndRemoteDatabasesAvailable_false()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException {
        // Given
        localDBManagerMocked = spy(mock(LocalDBManager.class));
        remoteDBManagerMocked = spy(mock(RemoteDBManager.class));
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);

        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.addLocation("Valencia");

        // When
        boolean result = geoNewsManager.removeFromFavorites(castellon.getId());

        // Then
        assertFalse(result);
        verify(localDBManagerMocked, times(2)).saveAll(any(), any(), any());
        verify(remoteDBManagerMocked, times(2)).saveAll(any(), any(), any());
    }
}
