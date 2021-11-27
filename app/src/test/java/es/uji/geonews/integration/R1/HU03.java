package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.GpsService;
import es.uji.geonews.model.services.ServiceName;

public class HU03 {
    private GpsService gpsServiceMocked;
    private GeocodeService geocodeServiceMocked;
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        DatabaseManager databaseManagerMocked = mock(DatabaseManager.class);
        geocodeServiceMocked = mock(GeocodeService.class);
        gpsServiceMocked = mock(GpsService.class);
        when(gpsServiceMocked.getServiceName()).thenReturn(ServiceName.GPS);
        LocationManager locationManager = new LocationManager(geocodeServiceMocked);
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(gpsServiceMocked);
        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);
    }

    @Test
    public void registerLocationByCurrentPosition_GPSAvailableKnownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException {
        // Arrange
        when(gpsServiceMocked.currentCoords()).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getPlaceName(any())).thenReturn("Castellon de la Plana");

        // Act
        Location newLocation = geoNewsManager.addLocationByGps();

        // Assert
        verify(geocodeServiceMocked, times(1)).isAvailable();
        verify(geocodeServiceMocked, times(1)).getPlaceName(any());
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
        assertEquals(0, geoNewsManager.getActiveLocations().size());
    }

    @Test
    public void registerLocationByCurrentPosition_GPSAvailableUnknownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException {
        // Arrange
        when(gpsServiceMocked.currentCoords()).thenReturn(new GeographCoords(33.65000,-41.19000));
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getPlaceName(any())).thenReturn(null);
        // Act
        Location newLocation = geoNewsManager.addLocationByGps();

        // Assert
        verify(geocodeServiceMocked, times(1)).isAvailable();
        verify(geocodeServiceMocked, times(1)).getPlaceName(any());
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
        assertEquals(0, geoNewsManager.getActiveLocations().size());
        assertEquals(33.65000, newLocation.getGeographCoords().getLatitude(), 0.01);
        assertEquals(-41.19000, newLocation.getGeographCoords().getLongitude(),0.01);
        assertNull(newLocation.getPlaceName());
    }


    @Test(expected= GPSNotAvailableException.class)
    public void registerLocationByCurrentPosition_GPSNotAvailable_GPSNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException {
        // Arrange
        when(gpsServiceMocked.currentCoords()).thenThrow(new GPSNotAvailableException());
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getPlaceName(any())).thenReturn(null);
        // Act
        geoNewsManager.addLocationByGps();

    }

    @Test(expected= ServiceNotAvailableException.class)
    public void registerLocationByCurrentPosition_KnownPlaceName_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException {
        // Arrange
        when(gpsServiceMocked.currentCoords()).thenReturn(new GeographCoords(33.65000,-41.19000));
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getPlaceName(any())).thenThrow(new ServiceNotAvailableException());
        // Act
        geoNewsManager.addLocationByGps();

    }
}
