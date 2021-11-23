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
import es.uji.geonews.model.services.GeocodeService;

public class HU11 {
    private GeoNewsManager geoNewsManager;
    private Location castellon;
    private Location valencia;

    @Before
    public void init()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
        castellon = geoNewsManager.addLocation("Castello de la plana");
        valencia = geoNewsManager.addLocation("Valencia");
    }

    @Test
    public void removeLocation_LocationNotInActiveLocations_True() throws NoLocationRegisteredException {
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
