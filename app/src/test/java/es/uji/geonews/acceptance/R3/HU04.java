package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU04 {
    private ServiceManager serviceManager;

    @Before
    public void init(){
        serviceManager = new ServiceManager();
    }

    @Test
    public void deactivateService_availableServices_true() {
        // Given
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());
        serviceManager.deactivateService(ServiceName.AIR_VISUAL);
        // When
        boolean result = serviceManager.deactivateService(ServiceName.OPEN_WEATHER);
        // Then
        assertTrue(result);
        assertEquals(0, serviceManager.getActiveServices().size());
    }

    @Test
    public void deactivateService_nonAvailableServices_false() {
        // Given
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());
        serviceManager.deactivateService(ServiceName.AIR_VISUAL);
        // When
        boolean result = serviceManager.deactivateService(ServiceName.AIR_VISUAL);
        // Then
        assertFalse(result);
        assertEquals(1, serviceManager.getActiveServices().size());
    }
}
