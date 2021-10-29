package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;


public class R1_AcceptanceTest {
    @Test
    public void registerLocationByPlaceName_knownPlaceName_Location(){
        // Given
        Service coordsSearchSrv = new CoordsSearchService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
        LocationManager locationManager = new LocationManager(serviceManager);
        // When
        Location newLocation = locationManager.addLocationByPlaceName("Castellon de la Plana");
        // Then
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals("Castellon de la Plana", newLocation.getPlaceName());
        assertEquals(39.98920, newLocation.getGeographCoords().getLatitude(), 0.01);
        assertEquals(-0.03621, newLocation.getGeographCoords().getLongitude(),0.01);
    }
}
