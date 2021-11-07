package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
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
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU01 {
    private CoordsSearchService coordsSearchServiceMocked;
    private ServiceManager serviceManagerSpied;
    private LocationManager locationManager;

    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        ServiceManager serviceManager = new ServiceManager();
        serviceManagerSpied = spy(serviceManager);
        doReturn(coordsSearchServiceMocked).when(serviceManagerSpied).getService("Geocode");
        locationManager = new LocationManager(serviceManagerSpied);
    }

    @Test
    public void registerLocationByPlaceName_knownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom(anyString()))
                .thenReturn(new GeographCoords(39.98920, -0.03621));
        // Act
        Location location = locationManager.addLocation("Castello de la Plana");
        // Assert
        verify(coordsSearchServiceMocked, times(1)).isAvailable();
        verify(coordsSearchServiceMocked, times(1)).getCoordsFrom("Castello de la Plana");
        assertEquals(0, locationManager.getActiveLocations().size());
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals("Castello de la Plana", locationManager.getLocaton(location.getId()).getPlaceName());
        assertEquals(39.98920, locationManager.getLocaton(location.getId()).getGeographCoords().getLatitude(), 0.01);
        assertEquals(-0.03621, locationManager.getLocaton(location.getId()).getGeographCoords().getLongitude(), 0.01);
    }


    @Test(expected=UnrecognizedPlaceNameException.class)
    public void registerLocationByPlaceName_unknownPlaceName_UnrecognizedPlaceNameException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, GPSNotAvailableException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom(anyString())).thenThrow(new UnrecognizedPlaceNameException());
        // Act
        locationManager.addLocation("asddf");
    }

    @Test(expected= ServiceNotAvailableException.class)
    public void registerLocationByPlaceName_withoutConnection_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom(anyString()))
                .thenThrow(new ServiceNotAvailableException());
        // Act
        locationManager.addLocation("Bilbao");
    }

}
