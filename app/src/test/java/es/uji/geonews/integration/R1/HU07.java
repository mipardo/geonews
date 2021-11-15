package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class HU07 {
    private GeocodeService geocodeServiceMocked;
    private ServiceManager serviceManagerMocked;

    @Before
    public void init(){
        geocodeServiceMocked = mock(GeocodeService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        when(serviceManagerMocked.getService(ServiceName.GEOCODE)).thenReturn(geocodeServiceMocked);
    }


    @Test
    public void getCoords_KnownPlaceName_validCoords()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        // Act
        GeographCoords coords = ((GeocodeService) serviceManagerMocked.getService(ServiceName.GEOCODE))
                .getCoords("Castelló de la Plana");
        // Assert
        assertEquals(39.98920, coords.getLatitude(), 0.01);
        assertEquals(-0.03621, coords.getLongitude(), 0.01);
    }

    @Test (expected = UnrecognizedPlaceNameException.class)
    public void getCoords_UnknownPlaceName_UnrecognizedPlaceNameException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        when(geocodeServiceMocked.getCoords(anyString())).thenThrow(new UnrecognizedPlaceNameException());
        // Act
        ((GeocodeService) serviceManagerMocked.getService(ServiceName.GEOCODE))
                .getCoords("asdfxxrtg");
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void getCoords_GeocodeNotAvailable_ServiceNotAvailableException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        when(geocodeServiceMocked.getCoords(anyString())).thenThrow(new ServiceNotAvailableException());
        // Act
        ((GeocodeService) serviceManagerMocked.getService(ServiceName.GEOCODE))
                .getCoords("Castellón de la Plana");
    }
}