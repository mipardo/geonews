package es.uji.geonews.controller.fragments;

import androidx.fragment.app.Fragment;

public class WeatherFragmentFactory {

    public static Fragment createWeatherFragment(int position) {
        switch (position) {
            case 0:
                return new TodayWeatherFragment();
            case 1:
                return new TomorrowWeatherFragment();
            case 2:
                return new FiveDaysWeatherFragment();
            default:
                return null;

        }
    }
}
