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
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;

public class HU11 {
    private GeoNewsManager geoNewsManager;
    private Context context;
    private Location castellon;
    private Location valencia;

    @Before
    public void init()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException {
        // Given
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(context);
        castellon = geoNewsManager.addLocation("Castello de la plana");
        valencia = geoNewsManager.addLocation("Valencia");
    }

    @After
    public void clean() throws InterruptedException {
        AuxiliaryTestClass.cleanDB(geoNewsManager, context);
    }

    @Test
    public void removeLocation_LocationNotInActiveLocations_True() {
        // When
        boolean result = geoNewsManager.removeLocation(castellon.getId());

        // Then
        assertTrue(result);
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
    }

    @Test
    public void removeLocation_LocationInActiveLocations_False() throws NoLocationRegisteredException {
        //Given
        geoNewsManager.activateLocation(valencia.getId());
        // When
        boolean result = geoNewsManager.removeLocation(valencia.getId());

        // Then
        assertFalse(result);
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
        assertEquals(1, geoNewsManager.getActiveLocations().size());
    }
}
