package es.uji.geonews.acceptance.R4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class HU03_12 {
    private GeoNewsManager geoNewsManager;
    private Context appContext;

    @Before
    public void init(){
        // Given
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void deactivateService_localAndRemoteDatabasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException {
        // Given
        geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);

        // When
        CountDownLatch lock = new CountDownLatch(1);
        boolean result = geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        lock.await(5000, TimeUnit.MILLISECONDS);

        // Then
        GeoNewsManager loadedGeoNewsManager = new GeoNewsManager(appContext);
        AuxiliaryTestClass.loadAll(loadedGeoNewsManager);

        assertTrue(result);
        assertFalse(loadedGeoNewsManager.getService(ServiceName.OPEN_WEATHER).isActive());
        assertFalse(loadedGeoNewsManager.getService(ServiceName.AIR_VISUAL).isActive());
    }
}
