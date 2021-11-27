package es.uji.geonews.acceptance.R4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.DatabaseNotAvailableException;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;

public class HU03_5 {
    private GeoNewsManager geoNewsManager;
    private Context appContext;

    @Before
    public void init(){
        // Given
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);

    }

    @After
    public void clean() throws InterruptedException {
        AuxiliaryTestClass.cleanDB(geoNewsManager, appContext);
    }

    @Test
    public void saveLocationAlias_AllDataBasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException, DatabaseNotAvailableException {
        // When
        CountDownLatch lock = new CountDownLatch(1);
        Location castellonDeLaPlana = geoNewsManager.addLocation("Castellon de la Plana");
        boolean confirmacion = geoNewsManager.setAliasToLocation("Casa",castellonDeLaPlana.getId());
        lock.await(2000, TimeUnit.MILLISECONDS);

        // Then
        GeoNewsManager loadedGeoNewsManager = new GeoNewsManager(appContext);
        AuxiliaryTestClass.loadAll(loadedGeoNewsManager);

        assertEquals(1, loadedGeoNewsManager.getNonActiveLocations().size());
        assertEquals("Casa", loadedGeoNewsManager.getLocation(castellonDeLaPlana.getId()).getAlias());
        assertTrue(confirmacion);
    }

    @Test
    public void saveLocationAlias_NoDataBasesAvailable_false()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException, DatabaseNotAvailableException {
        // When
        CountDownLatch lock = new CountDownLatch(1);
        Location castellonDeLaPlana = geoNewsManager.addLocation("Castellon de la Plana");
        boolean confirmacion = geoNewsManager.setAliasToLocation("",castellonDeLaPlana.getId());
        lock.await(2000, TimeUnit.MILLISECONDS);

        // Then
        GeoNewsManager loadedGeoNewsManager = new GeoNewsManager(appContext);
        AuxiliaryTestClass.loadAll(loadedGeoNewsManager);

        assertEquals(1, loadedGeoNewsManager.getNonActiveLocations().size());

        assertFalse(confirmacion);
    }
}
