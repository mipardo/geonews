package es.uji.geonews;

import org.junit.Test;
import static org.junit.Assert.*;

public class R1_AcceptanceTest {
    @Test
    public void registerLocationByPlaceName_knownPlaceName_Location(){
        // Given
        
        CoordinatesSearchService coordsSearchSrv = new CoordinatesSearchService();
        LocationManager locations = new LocationManager(coordsSearchSrv);
        GeographCoord coordinates = new GeographCoord(39.9866666, 0.038);
        // When
        locations.addByCoordinates(coordinates);
        // Then
        assertEquals(1, locations.getNumberOfLocations());
    }
}
