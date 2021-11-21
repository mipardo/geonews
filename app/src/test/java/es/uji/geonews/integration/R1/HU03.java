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
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.GpsService;

public class HU03 {
    private GeocodeService geocodeServiceMocked;
    private LocationManager locationManager;
    private GpsService gpsServiceMocked;

    @Before
    public void init(){
        geocodeServiceMocked = mock(GeocodeService.class);
        gpsServiceMocked = mock(GpsService.class);
        locationManager = new LocationManager(geocodeServiceMocked);
    }

    @Test
    public void registerLocationByCurrentPosition_GPSAvailableKnownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException, NoLocationRegisteredException {
        // Arrange
        when(gpsServiceMocked.currentCoords()).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getPlaceName(any())).thenReturn("Castellon de la Plana");
        // Act
        Location newLocation = locationManager.addLocation(gpsServiceMocked.currentCoords().toString());
        //locationManager.addLocationByGPS();
        // Assert
        verify(geocodeServiceMocked, times(1)).isAvailable();
        verify(geocodeServiceMocked, times(1)).getPlaceName(any());
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals(0, locationManager.getActiveLocations().size());
    }

    @Test
    public void registerLocationByCurrentPosition_GPSAvailableUnknownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException, NoLocationRegisteredException {
        // Arrange
        when(gpsServiceMocked.currentCoords()).thenReturn(new GeographCoords(33.65000,-41.19000));
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getPlaceName(any())).thenReturn(null);
        // Act
        Location newLocation = locationManager.addLocation(gpsServiceMocked.currentCoords().toString());
        //locationManager.addLocationByGPS();
        // Assert
        verify(geocodeServiceMocked, times(1)).isAvailable();
        verify(geocodeServiceMocked, times(1)).getPlaceName(any());
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
        when(gpsServiceMocked.currentCoords()).thenThrow(new GPSNotAvailableException());
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getPlaceName(any())).thenReturn(null);
        // Act
        locationManager.addLocation(gpsServiceMocked.currentCoords().toString());

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
        locationManager.addLocation(gpsServiceMocked.currentCoords().toString());

    }
}
