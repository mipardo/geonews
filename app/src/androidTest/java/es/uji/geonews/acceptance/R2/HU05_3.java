package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class HU05_3 {
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
    public void checkAirData_activeAndAvailable_AirVisualLocationData()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        geoNewsManager.addServiceToLocation(ServiceName.AIR_VISUAL, castellon.getId());

        // When
        AirVisualData serviceData = (AirVisualData) geoNewsManager.getData(ServiceName.AIR_VISUAL, castellon.getId());

        // Then
        assertNotNull(serviceData.getWeatherIcon());
        assertNotNull(serviceData.getMainCn());
        assertNotNull(serviceData.getMainUs());
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void checkAirData_notActiveInLocation_ServiceNotAvailableException()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        geoNewsManager.addServiceToLocation(ServiceName.AIR_VISUAL, castellon.getId());
        geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        // When
        geoNewsManager.getData(ServiceName.AIR_VISUAL, castellon.getId());
    }

    @Test
    public void checkAirData_deactivatedServiceGenerally_null() throws NotValidCoordinatesException,
            ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        // When
        Data serviceData = geoNewsManager.getData(ServiceName.AIR_VISUAL, castellon.getId());

        // Then
        assertNull(serviceData);
    }


}
