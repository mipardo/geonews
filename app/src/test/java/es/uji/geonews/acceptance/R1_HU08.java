package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
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
import org.junit.runners.MethodSorters;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R1_HU08 {

    private static LocationManager locationManager;

    @Test
    public void getPlaceName_E1KnownCoords_nearestPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service geocode = new CoordsSearchService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        // When
        GeographCoords coords = new GeographCoords(39.98920,-0.03621);
        String placeName = ((CoordsSearchService) locationManager.getService("Geocode")).
                getPlaceNameFromCoords(coords);

        // Then
        assertEquals("Castelló de la Plana", placeName);
    }

    @Test
    public void getPlaceName_E2UnknownCoords_nearestPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        serviceManager.addService(GeoCode);
        locationManager = new LocationManager(serviceManager);
        // When
        GeographCoords coords = new GeographCoords(33.6500,-41.1900);
        String placeName = ((CoordsSearchService) locationManager.
                getService("Geocode")).getPlaceNameFromCoords(coords);

        // Then
        assertNull(placeName);
    }

    @Test(expected = NotValidCoordinatesException.class)
    public void getPlaceName_E3InvalidCoords_NotValidCoordinatesException()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        serviceManager.addService(GeoCode);
        locationManager = new LocationManager(serviceManager);
        // When
        GeographCoords coords = new GeographCoords(100.0000,-41.1900);

        ((CoordsSearchService) locationManager.
                getService("Geocode")).getPlaceNameFromCoords(coords);
    }



}
