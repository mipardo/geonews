package es.uji.geonews.acceptance.R4;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;

public class HU02 {
    private GeoNewsManager geoNewsManager;
    private Context context;

    @Before
    public void init() throws NotValidCoordinatesException,
            ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(context);

        geoNewsManager.addLocation("Valencia");
    }

    @After
    public void clean() throws InterruptedException {
        AuxiliaryTestClass.cleanDB(geoNewsManager, context);
    }

    @Test
    public void loadData_localDbAvailable_userDao() {
        //When
        geoNewsManager = null;
        geoNewsManager = new GeoNewsManager(InstrumentationRegistry.getInstrumentation().getTargetContext());

        //Then
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
        assertEquals("Valencia", geoNewsManager.getNonActiveLocations().get(0).getPlaceName());
    }



}
