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
import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.GeocodeService;

public class HU01 {
    private GeocodeService geocodeServiceMocked;
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        geocodeServiceMocked = mock(GeocodeService.class);
        DatabaseManager databaseManagerMocked = mock(DatabaseManager.class);
        LocationManager locationManager = new LocationManager(geocodeServiceMocked);
        ServiceManager serviceManager = new ServiceManager();
        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);
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
        Location location = geoNewsManager.addLocation("Castello de la plana");
        // Assert
        verify(geocodeServiceMocked, times(1)).isAvailable();
        verify(geocodeServiceMocked, times(1)).getCoords("Castello de la plana");
        assertEquals(0, geoNewsManager.getActiveLocations().size());
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
        assertEquals("Castello de la plana", geoNewsManager.getLocation(location.getId()).getPlaceName());
        assertEquals(39.98920, geoNewsManager.getLocation(location.getId()).getGeographCoords().getLatitude(), 0.01);
        assertEquals(-0.03621, geoNewsManager.getLocation(location.getId()).getGeographCoords().getLongitude(), 0.01);
    }


    @Test(expected=UnrecognizedPlaceNameException.class)
    public void registerLocationByPlaceName_unknownPlaceName_UnrecognizedPlaceNameException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords(anyString())).thenThrow(new UnrecognizedPlaceNameException());
        // Act
        geoNewsManager.addLocation("asddf");
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
        geoNewsManager.addLocation("Bilbao");
    }

}
