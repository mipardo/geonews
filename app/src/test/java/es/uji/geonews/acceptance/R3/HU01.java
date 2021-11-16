package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU01 {
    ServiceManager serviceManager;

    @Before
    public void init() {
        serviceManager = new ServiceManager();
    }

    @Test
    public void checkAvailableServices_areAvailableServices_ListWithTwoServices() {
        // Given
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());
        // When
        List<ServiceName> services = serviceManager.getAvailableServices();
        // Then
        assertEquals(2, services.size());
        assertTrue(services.contains(ServiceName.OPEN_WEATHER));
        assertTrue(services.contains(ServiceName.AIR_VISUAL));
    }

    @Test
    public void checkAvailableServices_noAvailableServices_EmtpyList() {
        // Given
        // When
        List<ServiceName> services = serviceManager.getAvailableServices();

        // Then
        assertEquals(0, services.size());
    }
}