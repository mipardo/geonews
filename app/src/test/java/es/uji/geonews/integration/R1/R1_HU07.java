package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU07 {
    private LocationManager locationManager;
    private CoordsSearchService coordsSearchServiceMocked;
    private ServiceManager serviceManagerMocked;

    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        when(serviceManagerMocked.getService("Geocode")).thenReturn(coordsSearchServiceMocked);
        locationManager = new LocationManager(serviceManagerMocked);
    }


    @Test
    public void getCoords_KnownPlaceName_validCoords()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        when(coordsSearchServiceMocked.getCoordsFrom("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        // Act
        GeographCoords coords = ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("Castelló de la Plana");
        // Assert
        assertEquals(39.98920, coords.getLatitude(), 0.01);
        assertEquals(-0.03621, coords.getLongitude(), 0.01);
        // verify
    }

    @Test (expected = UnrecognizedPlaceNameException.class)
    public void getCoords_UnknownPlaceName_UnrecognizedPlaceNameException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        when(coordsSearchServiceMocked.getCoordsFrom(anyString())).thenThrow(new UnrecognizedPlaceNameException());
        // Act
        ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("asdfxxrtg");
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void getCoords_GeocodeNotAvailable_ServiceNotAvailableException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        when(coordsSearchServiceMocked.getCoordsFrom(anyString())).thenThrow(new ServiceNotAvailableException());
        // Act
        ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("Castellón de la Plana");
    }
}
