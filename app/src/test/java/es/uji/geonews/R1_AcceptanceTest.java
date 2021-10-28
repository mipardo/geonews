package es.uji.geonews;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;

public class R1_AcceptanceTest {
    @Test
    public void registerLocationByPlaceName_knownPlaceName_Location(){
        // Given
        Service coordsSearchSrv = new CoordsSearchService();
        LocationManager locationManager = new LocationManager(coordsSearchSrv);
        // When
        Location newLocation = locationManager.addLocationByPlaceName("Bilbao");
        // Then
        assertEquals(1, locationManager.getNonActiveLocations().size());
        //assertEquals("Bilbao", newLocation.getPlaceName());
        //Comprobar coordenadas
        //assertEquals(GeographCoords, newLocation.getPlaceName());

    }
}
