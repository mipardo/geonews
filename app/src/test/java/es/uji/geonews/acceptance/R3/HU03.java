package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU03 {
    private ServiceManager serviceManager;

    @Before
    public void init(){
        serviceManager = new ServiceManager();
    }

    @Test
    public void getServicesDescription_availableServices_servicesDescriptionMap()
            throws ServiceNotAvailableException {
        // Given
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());

        // When
        Map<String, String> availableServices = serviceManager.getServices();
        // Then
        assertEquals(2, availableServices.size());
        assertTrue(availableServices.containsKey(ServiceName.OPEN_WEATHER.name));
        assertTrue(availableServices.containsKey(ServiceName.AIR_VISUAL.name));
        assertTrue(availableServices.get(ServiceName.OPEN_WEATHER.name).length() > 0);
        assertTrue(availableServices.get(ServiceName.AIR_VISUAL.name).length() > 0);
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void getServicesDescription_nonAvailableServices_ServiceNotAvailableException()
            throws ServiceNotAvailableException {
        // When
        serviceManager.getServices();
    }
}
