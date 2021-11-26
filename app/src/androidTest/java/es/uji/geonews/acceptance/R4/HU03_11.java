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
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class HU03_11 {
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
    public void activateService_localAndRemoteDatabasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException {
        // Given
        geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);

        // When
        CountDownLatch lock = new CountDownLatch(1);
        boolean result = geoNewsManager.activateService(ServiceName.OPEN_WEATHER);
        lock.await(2000, TimeUnit.MILLISECONDS);

        // Then
        GeoNewsManager loadedGeoNewsManager = new GeoNewsManager(context);
        AuxiliaryTestClass.loadAll(loadedGeoNewsManager);

        assertTrue(result);
        assertTrue(loadedGeoNewsManager.getService(ServiceName.OPEN_WEATHER).isActive());
        assertTrue(loadedGeoNewsManager.getService(ServiceName.AIR_VISUAL).isActive());
    }

    @Test
    public void activateService_localAndRemoteDatabasesAvailable_false()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException {
        // Given
        geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);

        // When
        CountDownLatch lock = new CountDownLatch(1);
        boolean result = geoNewsManager.activateService(ServiceName.AIR_VISUAL);
        lock.await(2000, TimeUnit.MILLISECONDS);

        // Then
        GeoNewsManager loadedGeoNewsManager = new GeoNewsManager(context);
        AuxiliaryTestClass.loadAll(loadedGeoNewsManager);

        assertFalse(result);
        assertFalse(loadedGeoNewsManager.getService(ServiceName.OPEN_WEATHER).isActive());
        assertTrue(loadedGeoNewsManager.getService(ServiceName.AIR_VISUAL).isActive());
    }
}
