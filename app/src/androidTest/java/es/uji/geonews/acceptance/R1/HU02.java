package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;

public class HU02 {
    private GeoNewsManager geoNewsManager;
    private Context context;

    @Before
    public void init(){
        // Given
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(context);
    }

    @After
    public void clean() throws InterruptedException {
        AuxiliaryTestClass.cleanDB(geoNewsManager, context);
    }

    @Test
    public void registerLocationByCoords_KnownPlaceName_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException
            , UnrecognizedPlaceNameException {
        // When
        Location newLocation = geoNewsManager.addLocation("39.98920, -0.03621");
        // Then
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
        assertEquals("Castelló de la plana", newLocation.getPlaceName());
    }
    @Test
    public void registerLocationByCoords_UnknownPlaceName_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException {
        //Given
        geoNewsManager.addLocation("Castello de la plana");
        // When
        Location newLocation = geoNewsManager.addLocation("33.6500, -41.1900");
        // Then
        assertEquals(2, geoNewsManager.getNonActiveLocations().size());
        assertEquals(33.65, newLocation.getGeographCoords().getLatitude(), 0.01);
        assertEquals(-41.19, newLocation.getGeographCoords().getLongitude(),0.01);
        assertNull(newLocation.getPlaceName());
    }

    @Test(expected = NotValidCoordinatesException.class)
    public void registerLocationByCoords_InvalidCoords_NotValidCoordinatesException()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException {
        //Given
        geoNewsManager.addLocation("Castello de la plana");
        geoNewsManager.addLocation("Valencia");
        // When
        geoNewsManager.addLocation("100.00001, -41.19001");
        // Then
    }
}
