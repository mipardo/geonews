package es.uji.geonews.acceptance.R4;


import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class HU03_1 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void registerLocationByPlaceName_KnownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException {
        // When
        CountDownLatch lock = new CountDownLatch(1);
        geoNewsManager.addLocation("Bilbao");
        lock.await(5000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void saveAllData()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException {
        // When
        CountDownLatch lock = new CountDownLatch(1);
        Location bilbao = geoNewsManager.addLocation("Bilbao");
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.deactivateService(ServiceName.CURRENTS);
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, bilbao);
        geoNewsManager.saveAllData();
        lock.await(5000, TimeUnit.MILLISECONDS);
        geoNewsManager.loadData();
        lock.await(5000, TimeUnit.MILLISECONDS);
        assertEquals(geoNewsManager.getLocation(1).getPlaceName(), "Bilbao");
    }


}
