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
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class HU03_6 {
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
    public void activateLocationService_localAndRemoteDatabasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, DatabaseNotAvailableException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");

        // When
        CountDownLatch lock = new CountDownLatch(1);
        boolean result = geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);
        lock.await(2000, TimeUnit.MILLISECONDS);

        // Then
        GeoNewsManager loadedGeoNewsManager = new GeoNewsManager(context);
        AuxiliaryTestClass.loadAll(loadedGeoNewsManager);

        assertTrue(result);
        assertEquals(1, loadedGeoNewsManager.getServicesOfLocation(castellon.getId()).size());
        assertEquals(loadedGeoNewsManager.getServicesOfLocation(castellon.getId()).get(0), ServiceName.OPEN_WEATHER);
    }

    @Test
    public void activateLocationService_localAndRemoteDatabasesAvailable_false()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, DatabaseNotAvailableException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);

        // When
        CountDownLatch lock = new CountDownLatch(1);
        boolean result = geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);
        lock.await(2000, TimeUnit.MILLISECONDS);

        // Then
        GeoNewsManager loadedGeoNewsManager = new GeoNewsManager(context);
        AuxiliaryTestClass.loadAll(loadedGeoNewsManager);

        assertFalse(result);
        assertEquals(1, loadedGeoNewsManager.getServicesOfLocation(castellon.getId()).size());
        assertEquals(loadedGeoNewsManager.getServicesOfLocation(castellon.getId()).get(0), ServiceName.OPEN_WEATHER);
    }
}
