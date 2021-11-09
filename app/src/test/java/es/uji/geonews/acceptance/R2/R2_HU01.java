package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU01 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;

    @Before
    public void init(){
        // Given
        Service coordsSearchSrv = new GeocodeService();
        serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
        OpenWeatherService openWeatherService = new OpenWeatherService();
        serviceManager.addService(openWeatherService);
        locationManager = new LocationManager(serviceManager);
    }

    @Test
    public void checkServiceData_activeAndAvailable_OpenWeatherLocationData()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        locationManager.addLocation("Valencia");
        locationManager.addLocation("Alicante");
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        locationManager.addServiceToLocation("OpenWeather", castellon.getId());

        // When
        OpenWeatherData serviceData = (OpenWeatherData) locationManager.getData("OpenWeather", castellon.getId());

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
        locationManager.addLocation("Valencia");
        locationManager.addLocation("Alicante");
        Location castellon = locationManager.addLocation("Castelló de la Plana");

        // When
        Data serviceData = locationManager.getData("OpenWeather", castellon.getId());

        // Then
        assertNull(serviceData);
    }
}
