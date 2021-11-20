package es.uji.geonews.acceptance.R4;


import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;

public class HU03_1 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        geoNewsManager = new GeoNewsManager();
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

}
