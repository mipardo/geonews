package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R1_HU02 {
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
    public void registerLocationByCoords_E1KnownPlaceName_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            GPSNotAvailableException, UnrecognizedPlaceNameException {
        //Given
        GeographCoords coords = new GeographCoords(39.98920, -0.03621);
        // When
        Location newLocation = locationManager.addLocation(coords.toString());
        // Then
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals("Castelló de la Plana", newLocation.getPlaceName());
    }
    @Test
    public void registerLocationByCoords_E2UnknownPlaceName_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            GPSNotAvailableException, UnrecognizedPlaceNameException {
        //Given
        GeographCoords coords = new GeographCoords(33.65001, -41.19001);
        // When
        Location newLocation = locationManager.addLocation(coords.toString());
        // Then
        assertEquals(2, locationManager.getNonActiveLocations().size());
        assertEquals(33.65, newLocation.getGeographCoords().getLatitude(), 0.01);
        assertEquals(-41.19, newLocation.getGeographCoords().getLongitude(),0.01);
        assertNull( newLocation.getPlaceName());
    }

    @Test(expected = NotValidCoordinatesException.class)
    public void registerLocationByCoords_E3InvalidCoords_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            GPSNotAvailableException, UnrecognizedPlaceNameException {
        //Given
        GeographCoords coords = new GeographCoords(100.00001, -41.19001);
        // When
        Location newLocation = locationManager.addLocation(coords.toString());
        // Then
        //assertEquals(2, locationManager.getNonActiveLocations().size());
    }

    @Test
    public void registerLocationByCoords_E4NotAccurateCoords_null()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            GPSNotAvailableException, UnrecognizedPlaceNameException {
        //Given
        GeographCoords coords = new GeographCoords(39, 0.0);
        // When
        Location newLocation = locationManager.addLocation(coords.toString());
        // Then
        assertNull(newLocation);
    }

    @Test(expected=ServiceNotAvailableException.class)
    public void registerLocationByCoords_E5WithoutConnection_ServiceNotAvailableException()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            GPSNotAvailableException, UnrecognizedPlaceNameException {
        //Given
        GeographCoords coords = new GeographCoords(39.98920, -0.03621);
        // When
        locationManager.addLocation(coords.toString());
        // Then
        //assertEquals(2, locationManager.getNonActiveLocations().size());
    }

}
