package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU01 {

    private LocationManager locationManager;

    @Before
    public void init(){
        // Given
        GeocodeService geocodeService = new GeocodeService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocodeService);
        locationManager = new LocationManager(geocodeService);
    }

    @Test
    public void registerLocationByPlaceName_KnownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // When
        Location newLocation = locationManager.addLocation("Castello de la Plana");
        // Then
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals("Castello de la Plana", newLocation.getPlaceName());
        assertEquals(39.98920, newLocation.getGeographCoords().getLatitude(), 0.01);
        assertEquals(-0.03621, newLocation.getGeographCoords().getLongitude(),0.01);
    }


    @Test(expected=UnrecognizedPlaceNameException.class)
    public void registerLocationByPlaceName_UnknownPlaceName_UnrecognizedPlaceNameException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException, NotValidCoordinatesException, NoLocationRegisteredException {
        // Given
        locationManager.addLocation("Castello de la Plana");
        // When
        locationManager.addLocation("asddf");
        // Then
        assertEquals(1, locationManager.getNonActiveLocations().size());
    }
}
