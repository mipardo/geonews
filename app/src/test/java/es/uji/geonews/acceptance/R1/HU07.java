package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;

public class HU07 {
    private GeocodeService geocode;

    @Before
    public void init(){
        // Given
        geocode = new GeocodeService();
    }

    @Test
    public void getCoords_KnownPlaceName_validCoords()
    throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // When
        GeographCoords coords = geocode.getCoords("Castello de la Plana");
        // Then
        assertEquals(39.98920, coords.getLatitude(), 0.01);
        assertEquals(-0.03621, coords.getLongitude(), 0.01);
    }

    @Test (expected = UnrecognizedPlaceNameException.class)
    public void getCoords_UnknownPlaceName_UnrecognizedPlaceNameException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // When
        geocode.getCoords("asdfxxrtg");
    }

}
