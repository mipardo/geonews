package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;

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

public class HU06_1 {
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
    public void getNonActiveLocations_allLocationNotActive_LocationsList()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        geoNewsManager.addLocation("Castelló de la plana");
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");

        // When
        List<Location> locationList = geoNewsManager.getNonActiveLocations();

        // Then
        assertEquals(3, locationList.size());
    }

    @Test
    public void getNonActiveLocations_oneLocationNotActive_LocationsList()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.activateLocation(castellon.getId());

        // When
        List<Location> locationList = geoNewsManager.getNonActiveLocations();

        // Then
        assertEquals(1, locationList.size());
    }

    @Test
    public void getNonActiveLocations_AnyLocationNotActive_LocationsList()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.activateLocation(castellon.getId());
        geoNewsManager.activateLocation(valencia.getId());

        // When
        List<Location> locationList = geoNewsManager.getNonActiveLocations();

        // Then
        assertEquals(0, locationList.size());
    }

    @Test
    public void getNonActiveLocations_anyLocationRegistered_NoLocationRegisteredException() {
        // When
        List<Location> nonActiveLocations =  geoNewsManager.getNonActiveLocations();

        // Then
        assertEquals(0, nonActiveLocations.size());
    }
}
