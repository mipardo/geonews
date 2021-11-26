package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;

public class HU03 {
    private List<Location> list;
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
    public void checkListActiveLocations_threeActiveLocations_listWithThreeLocations() throws NotValidCoordinatesException,
            ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location valencia = geoNewsManager.addLocation("Valencia");
        Location alicante = geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");

        geoNewsManager.activateLocation(valencia.getId());
        geoNewsManager.activateLocation(alicante.getId());
        geoNewsManager.activateLocation(castellon.getId());

        // When
        list = geoNewsManager.getActiveLocations();

        // Then
        assertEquals(3, list.size());
        assertTrue( list.contains(valencia));
        assertTrue( list.contains(alicante));
        assertTrue( list.contains(castellon));
    }
    @Test
    public void checkListActiveLocations_oneActiveLocations_listWithOneLocation() throws NotValidCoordinatesException,
            ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location alicante = geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");

        geoNewsManager.activateLocation(castellon.getId());

        // When
        list = geoNewsManager.getActiveLocations();

        // Then
        assertEquals(1, list.size());
        assertFalse( list.contains(alicante));
        assertTrue( list.contains(castellon));
    }

    @Test
    public void checkListActiveLocations_noActiveLocations_emtpyList()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        // When
        list = geoNewsManager.getActiveLocations();

        // Then
        assertEquals(0, list.size());
        assertFalse(list.contains(castellon));

    }

    @Test
    public void checkListActiveLocations_noLocations_NoLocationRegisteredException()
            throws NoLocationRegisteredException {
        // Given

        // When
        List<Location> activeLocations = geoNewsManager.getActiveLocations();

        // Then
        assertEquals(0, activeLocations.size());
    }
}
