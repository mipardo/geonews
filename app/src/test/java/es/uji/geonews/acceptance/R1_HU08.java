package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU08 {

    private static LocationManager locationManager;

    @Test
    public void getPlaceName_knownCoords_nextPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        serviceManager.addService(GeoCode);
        locationManager = new LocationManager(serviceManager);
        // When
        GeographCoords coords = new GeographCoords(39.98920,-0.03621);
        String placeName = ((CoordsSearchService) locationManager.getServiceManager().
                getService("Geocode")).getPlaceNameFromCoords(coords);

        // Then
        assertEquals("Castelló de la Plana", placeName);
    }

    @Test
    public void getPlaceName_unknownCoords_nextPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        serviceManager.addService(GeoCode);
        locationManager = new LocationManager(serviceManager);
        // When
        GeographCoords coords = new GeographCoords(33.6500,-41.1900);
        String placeName = ((CoordsSearchService) locationManager.getServiceManager().
                getService("Geocode")).getPlaceNameFromCoords(coords);

        // Then
        assertNull(placeName);
    }

    @Test(expected = NotValidCoordinatesException.class)
    public void getPlaceName_knownCoords_nextPlaceName_NotValidCoordinatesException()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        serviceManager.addService(GeoCode);
        locationManager = new LocationManager(serviceManager);
        // When
        GeographCoords coords = new GeographCoords(100.0000,-41.1900);

        String placeName = ((CoordsSearchService) locationManager.getServiceManager().
                getService("Geocode")).getPlaceNameFromCoords(coords);

        // Then
    }

    @Test(expected = ServiceNotAvailableException.class)
    public void getPlaceName_knownCoords_nextPlaceName_ServiceNotAvailableException()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        serviceManager.addService(GeoCode);
        locationManager = new LocationManager(serviceManager);
        // When
        GeographCoords coords = new GeographCoords(39.98920,-0.03621);

        String placeName = ((CoordsSearchService) locationManager.getServiceManager().
                getService("Geocode")).getPlaceNameFromCoords(coords);

    }

}
