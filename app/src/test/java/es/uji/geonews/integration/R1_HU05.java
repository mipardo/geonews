package es.uji.geonews.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU05 {
    public static final Location castellon = new Location(1, "Castelló de la Plana", new GeographCoords(39.98001, -0.049001), LocalDate.now());
    private static LocationManager locationManager;
    private static Location location;

    @BeforeClass
    public static void init() throws ServiceNotAvailableException, NotValidCoordinatesException, GPSNotAvailableException {
        CoordsSearchService mockCoordsSearchSrv = mock(CoordsSearchService.class);
        when(mockCoordsSearchSrv.isAvailable()).thenReturn(true);
        when(mockCoordsSearchSrv.getPlaceNameFromCoords(any())).thenReturn("Castelló de la Plana");
        when(mockCoordsSearchSrv.validateLocation(castellon)).thenReturn(true);
        OpenWeatherService openWeatherService = mock(OpenWeatherService.class);
        when(openWeatherService.validateLocation(castellon)).thenReturn(true);
        AirVisualService airVisualService = mock(AirVisualService.class);
        when(airVisualService.validateLocation(castellon)).thenReturn(true);

        ServiceManager mockServiceManager = mock(ServiceManager.class);
        when(mockServiceManager.getService("Geocode")).thenReturn(mockCoordsSearchSrv);
        when(mockServiceManager.getService("AirVisual")).thenReturn(airVisualService);
        when(mockServiceManager.getService("OpenWeather")).thenReturn(openWeatherService);

//        locationManager = new LocationManager(mockServiceManager);
//        locationManager.getServiceManager().addService(new CoordsSearchService());
//        locationManager.getServiceManager().addService(new OpenWeatherService());
//        locationManager.getServiceManager().addService(new AirVisualService());
//
//        location = locationManager.addLocationByCoords(new GeographCoords(39.98001, -0.049001));
    }

    @Test
    public void validatePlaceName_PlaceNameRecognized_ListWithTwoActiveServices()
            throws ServiceNotAvailableException, NotValidCoordinatesException, GPSNotAvailableException {
        // When
        List<String> services = locationManager.validateLocation(location.getId());
        // Then

        assertEquals(2, services.size());
        Mockito.verify(locationManager.validateLocation(any()), times(1));
//        assertEquals(2, services.size());
//        assertTrue(services.contains("OpenWeather"));
//        assertTrue(services.contains("AirVisual"));
    }
}
