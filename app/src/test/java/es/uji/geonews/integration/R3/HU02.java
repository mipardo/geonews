package es.uji.geonews.integration.R3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;

import es.uji.geonews.model.services.ServiceName;

public class HU02 {
    private GeoNewsManager geoNewsManager;
    private AirVisualService airVisualServiceMocked;
    private GeocodeService geocodeService;

    @Before
    public void init(){
        // Given
        DatabaseManager databaseManagerMocked = mock(DatabaseManager.class);
        airVisualServiceMocked = mock(AirVisualService.class);
        when(airVisualServiceMocked.getServiceName()).thenReturn(ServiceName.AIR_VISUAL);
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(airVisualServiceMocked);
        LocationManager locationManager = new LocationManager(geocodeService);
        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);
    }

    @Test
    public void activeServices_deActiveAndAvailable_True(){
        // Given
        when(airVisualServiceMocked.isActive()).thenReturn(false);
        when(airVisualServiceMocked.checkConnection()).thenReturn(true);
        // When
        boolean result = geoNewsManager.activateService(airVisualServiceMocked.getServiceName());

        // Then
        assertTrue(result);

    }

    @Test
    public void activeServices_activeAndAvailable_False() {
        // Given
        when(airVisualServiceMocked.isActive()).thenReturn(true);
        when(airVisualServiceMocked.checkConnection()).thenReturn(true);
        // When
        boolean result = geoNewsManager.activateService(airVisualServiceMocked.getServiceName());

        // Then
        assertFalse(result);
    }

    @Test
    public void activeServices_noAvailable_False() {
        // Given
        when(airVisualServiceMocked.isActive()).thenReturn(false);
        when(airVisualServiceMocked.checkConnection()).thenReturn(false);
        // When
        boolean result = geoNewsManager.activateService(airVisualServiceMocked.getServiceName());

        // Then
        assertFalse(result);
        
    }
}

