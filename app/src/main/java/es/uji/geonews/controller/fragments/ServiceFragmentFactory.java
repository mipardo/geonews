package es.uji.geonews.controller.fragments;

import androidx.fragment.app.Fragment;

public class ServiceFragmentFactory {

    public static Fragment createServiceFragment(int position) {
        switch (position) {
            case 0:
                return new OpenWeatherFragment();
            case 1:
                return new AirVisualFragment();
            case 2:
                return new CurrentsFragment();
            default:
                return null;

        }
    }
}
