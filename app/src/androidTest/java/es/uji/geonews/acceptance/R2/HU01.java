package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.data.CurrentsData;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.ServiceName;

public class HU01 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void checkServiceData_activeAndAvailable_OpenWeatherLocationData()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);

        // When
        OpenWeatherData serviceData = (OpenWeatherData) geoNewsManager.getData(ServiceName.OPEN_WEATHER, castellon);

        // Then
        assertNotNull(serviceData.getMaxTemp());
        assertNotNull(serviceData.getMinTemp());
        assertNotNull(serviceData.getActTemp());
        assertNotNull(serviceData.getMain());
        assertNotNull(serviceData.getDescription());
        assertNotNull(serviceData.getIcon());
    }

    // Pruebas de Airvisual //
    @Test
    public void checkServiceData_activeAndAvailable_AirVisualLocationData()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.addServiceToLocation(ServiceName.AIR_VISUAL, castellon);

        // When
        AirVisualData serviceData = (AirVisualData) geoNewsManager.getData(ServiceName.AIR_VISUAL, castellon);

        // Then
        assertNotNull(serviceData.getWeatherIcon());
        assertNotNull(serviceData.getMainCn());
        assertNotNull(serviceData.getMainUs());
    }

    // Prueba de Currents //
    @Test
    public void checkServiceData_activeAndAvailable_CurrentsLocationData()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.addServiceToLocation(ServiceName.CURRENTS, valencia);

        // When
        CurrentsData serviceData = (CurrentsData) geoNewsManager.getData(ServiceName.CURRENTS, valencia);

        // Then
        assertNotNull(serviceData.getNewsList());
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void checkServiceData_notActiveAndAvailable_ServiceNotAvailableException()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);
        geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);
        // When
        geoNewsManager.getData(ServiceName.OPEN_WEATHER, castellon);
    }

    @Test
    public void checkServiceData_activeAndAvailable_null() throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        // When
        Data serviceData = geoNewsManager.getData(ServiceName.OPEN_WEATHER, castellon);

        // Then
        assertNull(serviceData);
    }
}
