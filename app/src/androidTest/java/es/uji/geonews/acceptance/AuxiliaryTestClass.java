package es.uji.geonews.acceptance;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import es.uji.geonews.model.managers.GeoNewsManager;

public class AuxiliaryTestClass {
    public static GeoNewsManager resetGeoNewsManager(GeoNewsManager geoNewsManager, Context appContext){
        appContext = null;
        geoNewsManager = null;
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        return geoNewsManager = new GeoNewsManager(appContext);
    }

    public static void loadAll(GeoNewsManager geoNewsManager) throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(1);
        geoNewsManager.loadAll();
        lock.await(5000, TimeUnit.MILLISECONDS);
    }
}
