package es.uji.geonews.acceptance.R4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

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

public class HU03_10 {
    private GeoNewsManager geoNewsManager;
    private Context appContext;

    @Before
    public void init(){
        // Given
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void reactivateLocation_localAndRemoteDatabasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException {
        // Given
        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, valencia);
        geoNewsManager.addServiceToLocation(ServiceName.AIR_VISUAL, valencia);
        geoNewsManager.activateLocation(valencia.getId());
        geoNewsManager.deactivateLocation(valencia.getId());

        // When
        CountDownLatch lock = new CountDownLatch(1);
        boolean result = geoNewsManager.activateLocation(valencia.getId());
        lock.await(2000, TimeUnit.MILLISECONDS);

        // Then
        GeoNewsManager loadedGeoNewsManager = new GeoNewsManager(appContext);
        AuxiliaryTestClass.loadAll(loadedGeoNewsManager);

        assertTrue(result);
        assertEquals(1, loadedGeoNewsManager.getActiveLocations().size());
    }

    @Test
    public void reactivateLocation_localAndRemoteDatabasesAvailable_false()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.activateLocation(castellon.getId());

        geoNewsManager.activateLocation(valencia.getId());
        geoNewsManager.deactivateLocation(valencia.getId());
        geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);
        geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        geoNewsManager.deactivateService(ServiceName.GPS);
        geoNewsManager.deactivateService(ServiceName.GEOCODE);
        geoNewsManager.deactivateService(ServiceName.CURRENTS);

        // When
        CountDownLatch lock = new CountDownLatch(1);
        boolean result = geoNewsManager.activateLocation(valencia.getId());
        lock.await(2000, TimeUnit.MILLISECONDS);

        // Then
        GeoNewsManager loadedGeoNewsManager = new GeoNewsManager(appContext);
        AuxiliaryTestClass.loadAll(loadedGeoNewsManager);

        assertFalse(result);
        assertEquals(1, loadedGeoNewsManager.getActiveLocations().size());
    }
}
