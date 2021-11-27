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

public class HU10 {
    private GeoNewsManager geoNewsManager;
    private Context context;
    Location castellon;

    @Before
    public void init()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Given
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(context);
        castellon = geoNewsManager.addLocation("Castello de la plana");
        geoNewsManager.activateLocation(castellon.getId());
    }

    @After
    public void clean() throws InterruptedException {
        AuxiliaryTestClass.cleanDB(geoNewsManager, context);
    }

    @Test
    public void deactivateLocation_ActiveLocation_True() {
        // When
        boolean result = geoNewsManager.deactivateLocation(castellon.getId());

        // Then
        assertTrue(result);
        assertEquals(0, geoNewsManager.getActiveLocations().size());
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
    }

    @Test
    public void deactivateLocation_NonActiveLocation_False() {
        // Given
        geoNewsManager.deactivateLocation(castellon.getId());

        // When
        boolean result = geoNewsManager.deactivateLocation(castellon.getId());

        // Then
        assertFalse(result);
    }
}
