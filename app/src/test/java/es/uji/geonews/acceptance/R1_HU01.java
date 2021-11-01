package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;


public class R1_HU01 {
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
    public void registerLocationByPlaceName_knownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        // When
        Location newLocation = locationManager.addLocationByPlaceName("Castellon de la Plana");
        // Then
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals("Castellon de la Plana", newLocation.getPlaceName());
        assertEquals(39.98920, newLocation.getGeographCoords().getLatitude(), 0.01);
        assertEquals(-0.03621, newLocation.getGeographCoords().getLongitude(),0.01);
    }


    @Test(expected=UnrecognizedPlaceNameException.class)
    public void registerLocationByPlaceName_unknownPlaceName_UnrecognizedPlaceNameException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        // When
        Location newLocation = locationManager.addLocationByPlaceName("asddf");
        // Then
        assertEquals(1, locationManager.getNonActiveLocations().size());
    }

    @Test(expected=ServiceNotAvailableException.class)
    public void registerLocationByPlaceName_withoutConnection_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        // When
        Location newLocation = locationManager.addLocationByPlaceName("Bilbao");
        // Then
        //assertEquals(1, locationManager.getNonActiveLocations().size());
    }

}