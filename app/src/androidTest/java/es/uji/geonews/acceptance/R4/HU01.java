package es.uji.geonews.acceptance.R4;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.exceptions.DatabaseNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU01 {
    private GeoNewsManager geoNewsManagerNew;
    private Context context;
    private GeoNewsManager geoNewsManagerOld;
    private String userId;

    @Before
    public void init() {
        // Given
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManagerNew = new GeoNewsManager(context);
    }

    @After
    public void clean() throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(1);
        geoNewsManagerNew.removeUserConfiguration(geoNewsManagerNew.loadUserId(context));
        geoNewsManagerNew.removeUserConfiguration(userId);
        lock.await(2000, TimeUnit.MILLISECONDS);
    }


    @Test
    public void loadRemoteState_fromEmptyToNonEmpty_userDao() throws NotValidCoordinatesException,
            ServiceNotAvailableException, UnrecognizedPlaceNameException, InterruptedException, DatabaseNotAvailableException {
        //Given
        userId = "e10a28n11v09";
        geoNewsManagerOld = createGeoNewsManager(userId);
        geoNewsManagerOld.activateService(ServiceName.AIR_VISUAL);
        geoNewsManagerOld.activateService(ServiceName.OPEN_WEATHER);
        Location valencia = geoNewsManagerOld.addLocation("Valencia");
        geoNewsManagerOld.addServiceToLocation(ServiceName.AIR_VISUAL, valencia);
        geoNewsManagerOld.addServiceToLocation(ServiceName.OPEN_WEATHER, valencia);
        geoNewsManagerOld.addToFavorites(valencia.getId());
        //When
        CountDownLatch lock = new CountDownLatch(1);
        geoNewsManagerNew.loadRemoteState(userId);
        lock.await(2000, TimeUnit.MILLISECONDS);

        //Then
        assertEquals(1, geoNewsManagerNew.getFavouriteLocations().size());
        assertEquals("Valencia", geoNewsManagerNew.getFavouriteLocations().get(0).getPlaceName());

    }

    @Test
    public void loadRemoteState_fromNonEmptyToNonEmpty_userDao() throws NotValidCoordinatesException,
            ServiceNotAvailableException, UnrecognizedPlaceNameException, InterruptedException, DatabaseNotAvailableException {
        //Given
        userId = "e10a28n11v10";
        geoNewsManagerOld = createGeoNewsManager(userId);
        geoNewsManagerOld.activateService(ServiceName.AIR_VISUAL);
        geoNewsManagerOld.activateService(ServiceName.OPEN_WEATHER);
        Location valencia = geoNewsManagerOld.addLocation("Alicante");
        geoNewsManagerOld.addServiceToLocation(ServiceName.AIR_VISUAL, valencia);
        geoNewsManagerOld.addServiceToLocation(ServiceName.OPEN_WEATHER, valencia);
        geoNewsManagerOld.addToFavorites(valencia.getId());

        //When
        CountDownLatch lock = new CountDownLatch(1);
        geoNewsManagerNew.addLocation("Bilbao");
        geoNewsManagerNew.loadRemoteState(userId);
        lock.await(2000, TimeUnit.MILLISECONDS);

        //Then
        assertEquals(1, geoNewsManagerNew.getFavouriteLocations().size());
        assertEquals(0, geoNewsManagerNew.getNonActiveLocations().size());
        assertEquals("Alicante", geoNewsManagerNew.getFavouriteLocations().get(0).getPlaceName());
    }

    @Test
    public void loadRemoteState_unrecognizedIdentifier_naif()
            throws InterruptedException, DatabaseNotAvailableException {
        //When
        CountDownLatch lock = new CountDownLatch(1);
        geoNewsManagerNew.loadRemoteState("0a0a0a");
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    private static GeoNewsManager createGeoNewsManager(String userId){
        LocationManager locationManager = new LocationManager(new GeocodeService());
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());
        serviceManager.addService(new GeocodeService());
        DatabaseManager databaseManager = new DatabaseManager();
        return new GeoNewsManager(locationManager, serviceManager, databaseManager, userId);
    }


}
