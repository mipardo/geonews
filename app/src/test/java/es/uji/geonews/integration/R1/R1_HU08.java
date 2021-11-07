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
import es.uji.geonews.model.services.CoordsSearchService;

public class R1_HU08 {

    private CoordsSearchService spyCoordsSearchService;

    @Before
    public void init(){
        // Arrange
        CoordsSearchService coordsSearchService = new CoordsSearchService();
        spyCoordsSearchService = spy(coordsSearchService);
    }

    @Test
    public void getPlaceName_KnownCoords_nearestPlaceName()
            throws ServiceNotAvailableException,
            NotValidCoordinatesException {
        doReturn("Castello de la Plana").when(spyCoordsSearchService).getPlaceNameFromCoords(any());
        // Act
        spyCoordsSearchService.getPlaceNameFromCoords(new GeographCoords(39.98920, -0.03621));
        // Assert
        verify(spyCoordsSearchService, times(1)).getPlaceNameFromCoords(any());
    }


    @Test
    public void getPlaceName_UnknownCoords_nearestPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        doReturn(null).when(spyCoordsSearchService).getPlaceNameFromCoords(any());
        // Act
        spyCoordsSearchService.getPlaceNameFromCoords(new GeographCoords(33.65001, -41.19001));
        // Assert
        verify(spyCoordsSearchService, times(1)).getPlaceNameFromCoords(any());

    }

    @Test(expected= ServiceNotAvailableException.class)
    public void getPlaceName_GeocodeNotAvailable_ServiceNotAvailableException()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // Arrange
        doThrow(new ServiceNotAvailableException()).when(spyCoordsSearchService).getPlaceNameFromCoords(any());
        // Act
        spyCoordsSearchService.getPlaceNameFromCoords(new GeographCoords(33.65001, -41.19001));
    }

}
