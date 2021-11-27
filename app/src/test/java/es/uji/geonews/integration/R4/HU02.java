package es.uji.geonews.integration.R4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.database.LocalDBManager;
import es.uji.geonews.model.database.RemoteDBManager;
import es.uji.geonews.model.exceptions.DatabaseNotAvailableException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.ServiceName;

public class HU02 {
    private GeoNewsManager geoNewsManager;
    private RemoteDBManager remoteDBManagerMocked;
    private LocalDBManager localDBManagerMocked;

    @Before
    public void init() throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.getServiceName()).thenReturn(ServiceName.GEOCODE);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords(any())).thenReturn(new GeographCoords(39.98920, -0.03621));
        LocationManager locationManager = new LocationManager(geocodeServiceMocked);
        ServiceManager serviceManager = new ServiceManager();

        localDBManagerMocked = mock(LocalDBManager.class);
        remoteDBManagerMocked = mock(RemoteDBManager.class);

        DatabaseManager databaseManagerMocked = new DatabaseManager(localDBManagerMocked, remoteDBManagerMocked);

        geoNewsManager =  new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);
    }


    @Test(expected = DatabaseNotAvailableException.class)
    public void loadData_noDbAvailable_DataBaseNotAvailableException() throws DatabaseNotAvailableException {
        // Given
        when(localDBManagerMocked.isAvailable()).thenReturn(false);
        when(remoteDBManagerMocked.isAvailable()).thenReturn(false);
        // When
        geoNewsManager.loadAll();
        //Then
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
        assertEquals("Valencia", geoNewsManager.getNonActiveLocations().get(0).getPlaceName());

    }
}
