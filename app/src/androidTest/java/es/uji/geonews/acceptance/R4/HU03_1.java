package es.uji.geonews.acceptance.R4;


import static org.junit.Assert.assertEquals;

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

public class HU03_1 {
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
    public void saveLocation_AllDataBasesAvailable_true()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException, NoLocationRegisteredException, DatabaseNotAvailableException {
        // When
        CountDownLatch lock = new CountDownLatch(1);
        Location bilbao = geoNewsManager.addLocation("Bilbao");
        lock.await(2000, TimeUnit.MILLISECONDS);

        // Then
        GeoNewsManager loadedGeoNewsManager = new GeoNewsManager(context);
        AuxiliaryTestClass.loadAll(loadedGeoNewsManager);

        assertEquals(1, loadedGeoNewsManager.getNonActiveLocations().size());
        assertEquals("Bilbao", loadedGeoNewsManager.getLocation(bilbao.getId()).getPlaceName());
    }

    @Test(expected = UnrecognizedPlaceNameException.class)
    public void saveLocation_NoDataBasesAvailable_false()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, InterruptedException {
        // When
        CountDownLatch lock = new CountDownLatch(1);
        Location bilbao = geoNewsManager.addLocation("asfgg");
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

}
