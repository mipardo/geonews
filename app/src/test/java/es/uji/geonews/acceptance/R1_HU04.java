package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU04 {
    private static LocationManager locationManager;

    @Test
    public void validatePlaceName_PlaceNameRecognized_ListWithOneServiceActive()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException {
        ServiceManager serviceManager = new ServiceManager();
        Service geocode = new CoordsSearchService();
        Service currents = new CurrentsService();
        serviceManager.addService(geocode);
        serviceManager.addService(currents);
        locationManager = new LocationManager(serviceManager);
        // When
        Location newLocation = locationManager.addLocation("Valencia");
        List<String> services = locationManager.validateLocation(newLocation.getId());
        // Then
        assertEquals(1, services.size());
        assertTrue(services.contains("Currents"));
    }

    @Test
    public void validatePlaceName_NoApiAvailable_EmptyList()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException {
        ServiceManager serviceManager = new ServiceManager();
        Service geocode = new CoordsSearchService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        // When
        Location newLocation = locationManager.addLocation("Valencia");
        List<String> services = locationManager.validateLocation(newLocation.getId());
        // Then
        assertEquals(0, services.size());
    }

}
