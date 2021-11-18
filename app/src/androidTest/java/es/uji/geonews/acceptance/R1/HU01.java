package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;

public class HU01 {

    private LocationManager locationManager;

    @Before
    public void init(){
        // Given
        locationManager = new LocationManager(new GeocodeService());
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
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException, NotValidCoordinatesException {
        // Given
        locationManager.addLocation("Castello de la Plana");
        // When
        locationManager.addLocation("asddf");
    }
}
