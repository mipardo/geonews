package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class HU02 {
    private GeoNewsManager geoNewsManager;
    private Context context;

    @Before
    public void init(){
        // Given
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(context);
        AuxiliaryTestClass.deactivateAllServices(geoNewsManager);
    }

    @After
    public void clean() throws InterruptedException {
        AuxiliaryTestClass.cleanDB(geoNewsManager, context);
    }

    @Test
    public void activateService_deActiveAndAvailable_True(){
        // When
        boolean result = geoNewsManager.activateService(ServiceName.AIR_VISUAL);

        // Then
        assertTrue(result);
        assertTrue(geoNewsManager.getService(ServiceName.AIR_VISUAL).isActive());
    }

    @Test
    public void activateService_activeAndAvailable_False() {
        // Given
        geoNewsManager.activateService(ServiceName.AIR_VISUAL);
        // When
        boolean result = geoNewsManager.activateService(ServiceName.AIR_VISUAL);

        // Then
        assertFalse(result);
        assertTrue(geoNewsManager.getService(ServiceName.AIR_VISUAL).isActive());
    }

}
