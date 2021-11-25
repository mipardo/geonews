package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;

public class HU07 {
    private GeocodeService spyGeocodeService;

    @Before
    public void init(){
        // Arrange
        GeocodeService geocodeService = new GeocodeService();
        spyGeocodeService = spy(geocodeService);
    }


    @Test
    public void getCoords_KnownPlaceName_validCoords()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        doReturn(new GeographCoords(39.98920, -0.03621)).when(spyGeocodeService).getCoords("Castelló de la Plana");
        // Act
        GeographCoords coords = spyGeocodeService.getCoords("Castelló de la Plana");
        // Assert
        verify(spyGeocodeService, times(1)).getCoords(any());
        assertEquals(39.98920, coords.getLatitude(), 0.01);
        assertEquals(-0.03621, coords.getLongitude(), 0.01);
    }

    @Test (expected = UnrecognizedPlaceNameException.class)
    public void getCoords_UnknownPlaceName_UnrecognizedPlaceNameException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        doThrow(new UnrecognizedPlaceNameException()).when(spyGeocodeService).getCoords("asdfxxrtg");

        // Act
        spyGeocodeService.getCoords("asdfxxrtg");
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void getCoords_GeocodeNotAvailable_ServiceNotAvailableException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        doThrow(new ServiceNotAvailableException()).when(spyGeocodeService).getCoords("Castellón de la Plana");

        // Act
        spyGeocodeService.getCoords("Castellón de la Plana");
    }
}
