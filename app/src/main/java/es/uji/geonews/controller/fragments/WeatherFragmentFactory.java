package es.uji.geonews.controller.fragments;

import androidx.fragment.app.Fragment;

public class WeatherFragmentFactory {

    public static Fragment createWeatherFragment(int position, int locationId) {
        switch (position) {
            case 0:
                return new TodayWeatherFragment(locationId);
            case 1:
                return new TomorrowWeatherFragment(locationId);
            case 2:
                return new FiveDaysWeatherFragment(locationId);
            default:
                return null;

        }
    }
}
