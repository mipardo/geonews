package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;

public class HU01 {
    private GeocodeService geocodeServiceMocked;
    private LocationManager locationManager;

    @Before
    public void init(){
        geocodeServiceMocked = mock(GeocodeService.class);
        locationManager = new LocationManager(geocodeServiceMocked);
    }

    @Test
    public void registerLocationByPlaceName_knownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords(anyString()))
                .thenReturn(new GeographCoords(39.98920, -0.03621));
        // Act
        Location location = locationManager.addLocation("Castello de la Plana");
        // Assert
        verify(geocodeServiceMocked, times(1)).isAvailable();
        verify(geocodeServiceMocked, times(1)).getCoords("Castello de la Plana");
        assertEquals(0, locationManager.getActiveLocations().size());
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals("Castello de la Plana", locationManager.getLocation(location.getId()).getPlaceName());
        assertEquals(39.98920, locationManager.getLocation(location.getId()).getGeographCoords().getLatitude(), 0.01);
        assertEquals(-0.03621, locationManager.getLocation(location.getId()).getGeographCoords().getLongitude(), 0.01);
    }


    @Test(expected=UnrecognizedPlaceNameException.class)
    public void registerLocationByPlaceName_unknownPlaceName_UnrecognizedPlaceNameException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords(anyString())).thenThrow(new UnrecognizedPlaceNameException());
        // Act
        locationManager.addLocation("asddf");
    }

    @Test(expected= ServiceNotAvailableException.class)
    public void registerLocationByPlaceName_withoutConnection_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords(anyString()))
                .thenThrow(new ServiceNotAvailableException());
        // Act
        locationManager.addLocation("Bilbao");
    }

}
