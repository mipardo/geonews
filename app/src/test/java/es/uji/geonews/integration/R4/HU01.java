package es.uji.geonews.integration.R4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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
import es.uji.geonews.model.exceptions.DatabaseNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.ServiceName;

public class HU01 {

    private GeoNewsManager geoNewsManagerNew;
    private GeoNewsManager geoNewsManagerOld;
    private RemoteDBManager remoteDBManagerMocked;
    private String userId;

    @Before
    public void init() throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        geoNewsManagerNew = createGeoNewsManagerWithDBMocked(null);
    }

    @Test(expected = DatabaseNotAvailableException.class)
    public void loadRemoteState_dataBaseNotAvailable_DatabaseNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, DatabaseNotAvailableException {
        //Given
        userId = "e10a28n11v09";
        geoNewsManagerOld = createGeoNewsManagerWithDBMocked(userId);
        geoNewsManagerOld.activateService(ServiceName.AIR_VISUAL);
        geoNewsManagerOld.activateService(ServiceName.OPEN_WEATHER);
        Location valencia = geoNewsManagerOld.addLocation("Valencia");
        geoNewsManagerOld.addServiceToLocation(ServiceName.AIR_VISUAL, valencia);
        geoNewsManagerOld.addServiceToLocation(ServiceName.OPEN_WEATHER, valencia);
        geoNewsManagerOld.addToFavorites(valencia.getId());
        //When
        CountDownLatch lock = new CountDownLatch(1);
        geoNewsManagerNew.loadRemoteState(userId);
        lock.await(2000, TimeUnit.MILLISECONDS);

        //Then
        assertEquals(1, geoNewsManagerNew.getFavouriteLocations().size());
        assertEquals("Valencia", geoNewsManagerNew.getFavouriteLocations().get(0).getPlaceName());

    }

    private GeoNewsManager createGeoNewsManagerWithDBMocked(String userId) throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.getServiceName()).thenReturn(ServiceName.GEOCODE);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        LocationManager locationManager = new LocationManager(geocodeServiceMocked);
        ServiceManager serviceManager = new ServiceManager();

        LocalDBManager localDBManagerMocked = mock(LocalDBManager.class);
        remoteDBManagerMocked = mock(RemoteDBManager.class);
        when(localDBManagerMocked.isAvailable()).thenReturn(false);
        when(remoteDBManagerMocked.isAvailable()).thenReturn(false);
        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        return new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);

    }
}
