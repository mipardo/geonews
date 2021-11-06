package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.OpenWeatherLocationData;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU01 {
    private LocationManager locationManager;

    @Before
    public void init(){
        // Given
        Service coordsSearchSrv = new CoordsSearchService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
        locationManager = new LocationManager(serviceManager);
    }

    @Test
    public void checkServiceData_activeAndAvailable_OpenWeatherLocationData()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        OpenWeatherService openWeatherService = new OpenWeatherService();
        Service coordsSearchSrv = new CoordsSearchService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
        serviceManager.addService(openWeatherService);
        locationManager = new LocationManager(serviceManager);
        locationManager.addLocation("Valencia");
        locationManager.addLocation("Alicante");
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        locationManager.addLocationService("OpenWeather", castellon.getId());

        // When
        OpenWeatherLocationData serviceData = locationManager.getServiceData("OpenWeather", castellon.getId());

        // Then
        assertNotNull(serviceData.getMaxTemp());
        assertNotNull(serviceData.getMinTemp());
        assertNotNull(serviceData.getActTemp());
        assertNotNull(serviceData.getMain());
        assertNotNull(serviceData.getDescription());
        assertNotNull(serviceData.getIcon());
    }

    @Test
    public void checkServiceData_activeAndAvailable_null() throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        OpenWeatherService openWeatherService = new OpenWeatherService();
        Service coordsSearchSrv = new CoordsSearchService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
        serviceManager.addService(openWeatherService);
        locationManager = new LocationManager(serviceManager);
        locationManager.addLocation("Valencia");
        locationManager.addLocation("Alicante");
        Location castellon = locationManager.addLocation("Castelló de la Plana");

        // When
        OpenWeatherLocationData serviceData = locationManager.getServiceData("OpenWeather", castellon.getId());

        // Then
        assertNull(serviceData);
    }
}
