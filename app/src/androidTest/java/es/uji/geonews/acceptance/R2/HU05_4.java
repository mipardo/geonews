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
import es.uji.geonews.model.data.CurrentsData;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class HU05_4 {
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
    public void checkCurrentsData_activeAndAvailable_CurrentsLocationData()
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
    public void checkCurrentsData_notActiveInLocation_ServiceNotAvailableException()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        geoNewsManager.addServiceToLocation(ServiceName.CURRENTS, castellon);
        geoNewsManager.deactivateService(ServiceName.CURRENTS);
        // When
        geoNewsManager.getData(ServiceName.CURRENTS, castellon);
    }

    @Test
    public void checkCurrentsData_deactivatedServiceGenerally_null() throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        // When
        Data serviceData = geoNewsManager.getData(ServiceName.CURRENTS, castellon);

        // Then
        assertNull(serviceData);
    }


}
