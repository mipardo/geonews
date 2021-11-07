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
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.GpsService;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU03 {
    private CoordsSearchService coordsSearchServiceMocked;
    private ServiceManager serviceManagerMocked;
    private LocationManager locationManager;
    private GpsService gpsServiceMocked;

    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        gpsServiceMocked = mock(GpsService.class);
        when(serviceManagerMocked.getService("Geocode")).thenReturn(coordsSearchServiceMocked);
        locationManager = new LocationManager(serviceManagerMocked);
    }

    @Test
    public void registerLocationByCurrentPosition_GPSAvailableKnownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException {
        // Arrange
        when(gpsServiceMocked.getMyCoords()).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getPlaceNameFromCoords(any())).thenReturn("Castellon de la Plana");
        // Act
        Location newLocation = locationManager.addLocation(gpsServiceMocked.getMyCoords().toString());
        //locationManager.addLocationByGPS();
        // Assert
        verify(coordsSearchServiceMocked, times(1)).isAvailable();
        verify(coordsSearchServiceMocked, times(1)).getPlaceNameFromCoords(any());
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals(0, locationManager.getActiveLocations().size());
    }

    @Test
    public void registerLocationByCurrentPosition_GPSAvailableUnknownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException {
        // Arrange
        when(gpsServiceMocked.getMyCoords()).thenReturn(new GeographCoords(33.65000,-41.19000));
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getPlaceNameFromCoords(any())).thenReturn(null);
        // Act
        Location newLocation = locationManager.addLocation(gpsServiceMocked.getMyCoords().toString());
        //locationManager.addLocationByGPS();
        // Assert
        verify(coordsSearchServiceMocked, times(1)).isAvailable();
        verify(coordsSearchServiceMocked, times(1)).getPlaceNameFromCoords(any());
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals(0, locationManager.getActiveLocations().size());
        assertEquals(33.65000, newLocation.getGeographCoords().getLatitude(), 0.01);
        assertEquals(-41.19000, newLocation.getGeographCoords().getLongitude(),0.01);
        assertNull(newLocation.getPlaceName());
    }


    @Test(expected= GPSNotAvailableException.class)
    public void registerLocationByCurrentPosition_GPSNotAvailable_GPSNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException {
        // Arrange
        when(gpsServiceMocked.getMyCoords()).thenThrow(new GPSNotAvailableException());
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getPlaceNameFromCoords(any())).thenReturn(null);
        // Act
        locationManager.addLocation(gpsServiceMocked.getMyCoords().toString());

    }

    @Test(expected= ServiceNotAvailableException.class)
    public void registerLocationByCurrentPosition_KnownPlaceName_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException {
        // Arrange
        when(gpsServiceMocked.getMyCoords()).thenReturn(new GeographCoords(33.65000,-41.19000));
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getPlaceNameFromCoords(any())).thenThrow(new ServiceNotAvailableException());
        // Act
        locationManager.addLocation(gpsServiceMocked.getMyCoords().toString());

    }
}
