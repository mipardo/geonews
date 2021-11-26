package es.uji.geonews.acceptance;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class AuxiliaryTestClass {

    public static void loadAll(GeoNewsManager geoNewsManager) throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(1);
        geoNewsManager.loadAll();
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    public static void deactivateAllServices(GeoNewsManager geoNewsManager){
        geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);
        geoNewsManager.deactivateService(ServiceName.GEOCODE);
        geoNewsManager.deactivateService(ServiceName.CURRENTS);

    }
}
