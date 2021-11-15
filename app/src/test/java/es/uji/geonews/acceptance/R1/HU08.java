package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.services.GeocodeService;

import static org.junit.Assert.assertNull;

public class HU08 {
    private GeocodeService geocode;

    @Before
    public void init(){
        // Given
        geocode = new GeocodeService();
    }

    @Test
    public void getPlaceName_KnownCoords_nearestPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        GeographCoords coords = new GeographCoords(39.98920,-0.03621);
        // When
        String placeName = geocode.getPlaceName(coords);
        // Then
        assertEquals("Castelló de la Plana", placeName);
    }

    @Test
    public void getPlaceName_UnknownCoords_nearestPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        GeographCoords coords = new GeographCoords(33.6500,-41.1900);
        // When
        String placeName = geocode.getPlaceName(coords);
        // Then
        assertNull(placeName);
    }

    @Test(expected = NotValidCoordinatesException.class)
    public void getPlaceName_InvalidCoords_NotValidCoordinatesException()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        GeographCoords coords = new GeographCoords(100.0000,-41.1900);
        // When
        geocode.getPlaceName(coords);
    }
}
