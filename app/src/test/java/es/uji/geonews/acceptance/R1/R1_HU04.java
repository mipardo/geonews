package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU04 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;

    @Before
    public void init(){
        // Given
        serviceManager = new ServiceManager();
        GeocodeService geocode = new GeocodeService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(geocode);
    }

    @Test(timeout = 100000)
    public void validatePlaceName_PlaceNameRecognized_ListWithOneServiceActive()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException {
        // Given
        Service currents = new CurrentsService();
        serviceManager.addService(currents);
        Location newLocation = locationManager.addLocation("Valencia");
        serviceManager.addServiceToLocation("Currents", newLocation);

        // When
        List<String> services = serviceManager.validateLocation(newLocation);

        // Then
        assertEquals(1, services.size());
        assertTrue(services.contains("Currents"));
    }

    @Test
    public void validatePlaceName_NoApiAvailable_EmptyList()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException {
        // When
        Location newLocation = locationManager.addLocation("Valencia");
        List<String> services = serviceManager.validateLocation(newLocation);
        // Then
        assertEquals(0, services.size());
    }

}
