package es.uji.geonews.controller.fragments;

import androidx.fragment.app.Fragment;

public class ServiceFragmentFactory {

    public static Fragment createServiceFragment(int position, int locationId) {
        switch (position) {
            case 0:
                return new OpenWeatherFragment(locationId);
            case 1:
                return new AirVisualFragment();
            case 2:
                return new CurrentsFragment();
            default:
                return null;

        }
    }
}
