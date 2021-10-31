package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import es.uji.geonews.model.GPSManager;
import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU03 {
    private static LocationManager locationManager;

    @BeforeClass
    public static void init(){
        // Given
        Service coordsSearchSrv = new CoordsSearchService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
        locationManager = new LocationManager(serviceManager);
    }

    @Test
    public void registerLocationByCurrentPosition_GPSAvailableKnownPlaceName_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException {
        GeographCoords coords = GPSManager.getMyCoords();
        Location newLocation = locationManager.addLocationByCoords(coords);
        // Then
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals("Castelló de la Plana", newLocation.getPlaceName());
    }
}
