package es.uji.geonews.integration.R1;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.services.GeocodeService;

public class HU08 {
    private GeocodeService spyGeocodeService;

    @Before
    public void init(){
        // Arrange
        GeocodeService geocodeService = new GeocodeService();
        spyGeocodeService = spy(geocodeService);
    }

    @Test
    public void getPlaceName_KnownCoords_nearestPlaceName()
            throws ServiceNotAvailableException,
            NotValidCoordinatesException {
        doReturn("Castello de la Plana").when(spyGeocodeService).getPlaceName(any());
        // Act
        spyGeocodeService.getPlaceName(new GeographCoords(39.98920, -0.03621));
        // Assert
        verify(spyGeocodeService, times(1)).getPlaceName(any());
    }


    @Test
    public void getPlaceName_UnknownCoords_nearestPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        doReturn(null).when(spyGeocodeService).getPlaceName(any());
        // Act
        spyGeocodeService.getPlaceName(new GeographCoords(33.65001, -41.19001));
        // Assert
        verify(spyGeocodeService, times(1)).getPlaceName(any());

    }

    @Test(expected= ServiceNotAvailableException.class)
    public void getPlaceName_GeocodeNotAvailable_ServiceNotAvailableException()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // Arrange
        doThrow(new ServiceNotAvailableException()).when(spyGeocodeService).getPlaceName(any());
        // Act
        spyGeocodeService.getPlaceName(new GeographCoords(33.65001, -41.19001));
    }

}
