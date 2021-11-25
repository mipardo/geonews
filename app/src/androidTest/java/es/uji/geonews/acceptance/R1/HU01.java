package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;

public class HU01 {

    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void registerLocationByPlaceName_KnownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // When
        Location newLocation = geoNewsManager.addLocation("Castello de la plana");
        // Then
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
        assertEquals("Castello de la plana", newLocation.getPlaceName());
        assertEquals(39.98920, newLocation.getGeographCoords().getLatitude(), 0.01);
        assertEquals(-0.03621, newLocation.getGeographCoords().getLongitude(),0.01);
    }


    @Test(expected=UnrecognizedPlaceNameException.class)
    public void registerLocationByPlaceName_UnknownPlaceName_UnrecognizedPlaceNameException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException, NotValidCoordinatesException {
        // Given
        geoNewsManager.addLocation("Castello de la plana");
        // When
        geoNewsManager.addLocation("asddf");
    }
}
