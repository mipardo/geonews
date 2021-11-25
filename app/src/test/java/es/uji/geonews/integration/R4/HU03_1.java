package es.uji.geonews.integration.R4;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
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
import es.uji.geonews.model.database.RemoteDBManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU03_1 {
    GeocodeService geocodeServiceMocked;
    OpenWeatherService openWeatherServiceMocked;
    GeoNewsManager geoNewsManager;
    DatabaseManager databaseManagerMocked;
    GeoNewsManager spyGeoNewsManager;
    GeocodeService spyGeocodeService;
    RemoteDBManager remoteDBManagerMocked;
    String userId;
    RemoteDBManager spyRemoteDBManager;
    ServiceManager serviceManager;
    LocationManager locationManager;

    @Before
    public void init() {
        geocodeServiceMocked = mock(GeocodeService.class);
        serviceManager = new ServiceManager();
        locationManager = new LocationManager(geocodeServiceMocked);

        databaseManagerMocked = mock(DatabaseManager.class);
        remoteDBManagerMocked = mock(RemoteDBManager.class);
        userId= String.valueOf(0);

        GeocodeService geocodeService = new GeocodeService();
        spyGeocodeService = spy(geocodeService);
    }

    @Test(expected = ServiceNotAvailableException.class)
    public void getData_ServiceNotAvailabe_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        //when(remoteDBManagerMocked.saveAll(any()).thenReturn);
        geoNewsManager.addLocation("Alicante");
        geoNewsManager.addLocation("Valencia");
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");


        // Act
        geoNewsManager.getData(ServiceName.OPEN_WEATHER, castellon);
    }
    @Test
    public void getPlaceName_KnownCoords_nearestPlaceName()
            throws ServiceNotAvailableException,
            NotValidCoordinatesException, UnrecognizedPlaceNameException {
        doReturn(false).when(spyRemoteDBManager).saveAll(any(),any(),any());
        // Act
        spyRemoteDBManager.saveAll(userId,locationManager,serviceManager);
        // Assert
        verify(spyGeoNewsManager, times(1)).saveAll();
    }
}
