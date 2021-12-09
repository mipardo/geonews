package es.uji.geonews.controller.fragments;

import androidx.fragment.app.Fragment;

import es.uji.geonews.model.services.ServiceName;

public class ServiceFragmentFactory {

    public static Fragment createServiceFragment(ServiceName serviceName, int locationId) {
        switch (serviceName) {
            case OPEN_WEATHER:
                return new OpenWeatherFragment(locationId);
            case AIR_VISUAL:
                return new AirVisualFragment(locationId);
            case CURRENTS:
                return new CurrentsFragment(locationId);
            default:
                return new NoServiceAvailableFragment();

        }
    }
}
