package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class HU05_2 {
    private GeoNewsManager geoNewsManager;
    private Context context;

    @Before
    public void init(){
        // Given
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(context);
    }

    @After
    public void clean() throws InterruptedException {
        AuxiliaryTestClass.cleanDB(geoNewsManager, context);
    }

    @Test
    public void checkWeatherData_activeAndAvailable_OpenWeatherLocationData()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon.getId());

        // When
        OpenWeatherData serviceData = (OpenWeatherData) geoNewsManager.getData(ServiceName.OPEN_WEATHER, castellon.getId());

        // Then
        assertTrue(serviceData.getDailyWeatherList().get(0).getTempMax() > - 10 && serviceData.getDailyWeatherList().get(0).getTempMax() < 50);
        assertTrue(serviceData.getDailyWeatherList().get(0).getTempMin() > - 10 && serviceData.getDailyWeatherList().get(0).getTempMin() < 50);
        assertTrue(serviceData.getCurrentWeather().getCurrentTemp() > - 10 && serviceData.getCurrentWeather().getCurrentTemp() < 50);
        assertNotNull(serviceData.getDailyWeatherList().get(0).getMain());
        assertNotNull(serviceData.getDailyWeatherList().get(0).getDescription());
        assertNotNull(serviceData.getDailyWeatherList().get(0).getIcon());
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void checkWeatherData_notActiveInLocation_ServiceNotAvailableException()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon.getId());
        geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);
        // When
        geoNewsManager.getData(ServiceName.OPEN_WEATHER, castellon.getId());
    }

    @Test
    public void checkWeatherData_deactivatedServiceGenerally_null() throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        // When
        ServiceData serviceData = geoNewsManager.getData(ServiceName.OPEN_WEATHER, castellon.getId());

        // Then
        assertNull(serviceData);
    }


}
