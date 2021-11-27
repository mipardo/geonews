package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;

public class HU06 {
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
    public void activateLocation_KnownPlaceName_true()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException, NotValidCoordinatesException, NoLocationRegisteredException {

        Location newLocation = geoNewsManager.addLocation("Castello de la plana");
        // When
        boolean result = geoNewsManager.activateLocation(newLocation.getId());
        // Then
        assertTrue(result);
        assertTrue(geoNewsManager.getLocation(newLocation.getId()).isActive());
    }

    @Test
    public void activateLocation_UnKnownPlaceName_true()
            throws ServiceNotAvailableException, NotValidCoordinatesException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        Location castellon = geoNewsManager.addLocation("Castello de la plana");
        geoNewsManager.activateLocation(castellon.getId());

        GeographCoords coords = new GeographCoords(33.65001, -41.19001);
        Location newLocation = geoNewsManager.addLocation(coords.toString());
        // When
        boolean result = geoNewsManager.activateLocation(newLocation.getId());
        // Then
        assertTrue(result);
        assertTrue(geoNewsManager.getLocation(newLocation.getId()).isActive());
    }

    @Test
    public void activateLocation_LocationAlreadyActive_false()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castello de la plana");
        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.activateLocation(castellon.getId());
        geoNewsManager.activateLocation(valencia.getId());
        // When
        boolean result = geoNewsManager.activateLocation(castellon.getId());
        // Then
        assertFalse(result);
        assertEquals(2, geoNewsManager.getActiveLocations().size());
    }
}
