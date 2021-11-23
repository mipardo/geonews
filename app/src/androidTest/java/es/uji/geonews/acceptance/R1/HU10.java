package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.managers.ServiceManager;

public class HU10 {
    private GeoNewsManager geoNewsManager;
    Location castellon;

    @Before
    public void init()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
        castellon = geoNewsManager.addLocation("Castello de la plana");
        geoNewsManager.activateLocation(castellon.getId());
    }

    @Test
    public void deactivateLocation_ActiveLocation_True()
            throws NoLocationRegisteredException {
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
