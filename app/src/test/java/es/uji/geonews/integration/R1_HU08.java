package es.uji.geonews.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import es.uji.geonews.model.LocationFactory;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU08 {

    @Test
    public void getPlaceName_E1KnownCoords_nearestPlaceName()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        CoordsSearchService mockCoordsSearchService = mock(CoordsSearchService.class);
        when(mockCoordsSearchService.isAvailable()).thenReturn(true);
        when(mockCoordsSearchService.getPlaceNameFromCoords(any())).thenReturn("Castellon de la Plana");
        LocationFactory locationFactory = new LocationFactory(mockCoordsSearchService);
        // Act
        //TODO: el metodo getPlaceFromCoords es privado y se usa al hacer el createLocation, ¿es correcto?
        locationFactory.createLocation("39.98920, -0.03621");
        // Assert
        verify(mockCoordsSearchService, times(1)).isAvailable();
        verify(mockCoordsSearchService, times(1)).getPlaceNameFromCoords(any());
    }


    @Test
    public void getPlaceName_E2UnknownCoords_nearestPlaceName()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        CoordsSearchService mockCoordsSearchService = mock(CoordsSearchService.class);
        when(mockCoordsSearchService.isAvailable()).thenReturn(true);
        when(mockCoordsSearchService.getPlaceNameFromCoords(any())).thenReturn(null);
        LocationFactory locationFactory = new LocationFactory(mockCoordsSearchService);
        // Act
        //TODO: el metodo getPlaceFromCoords es privado y se usa al hacer el createLocation, ¿es correcto?
        locationFactory.createLocation("33.65001, -41.19001");
        // Assert
        verify(mockCoordsSearchService, times(1)).isAvailable();
        verify(mockCoordsSearchService, times(1)).getPlaceNameFromCoords(any());
    }

    @Test(expected= ServiceNotAvailableException.class)
    public void getPlaceName_E4GeocodeNotAvailable_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        CoordsSearchService mockCoordsSearchService = mock(CoordsSearchService.class);
        when(mockCoordsSearchService.isAvailable()).thenReturn(true);
        when(mockCoordsSearchService.getPlaceNameFromCoords(any())).thenThrow(new ServiceNotAvailableException());
        LocationFactory locationFactory = new LocationFactory(mockCoordsSearchService);
        // Act
        //TODO: el metodo getPlaceFromCoords es privado y se usa al hacer el createLocation, ¿es correcto?
        locationFactory.createLocation("33.65001, -41.19001");
        // Assert
        verify(mockCoordsSearchService, times(1)).isAvailable();
        verify(mockCoordsSearchService, times(1)).getPlaceNameFromCoords(any());
    }

}
