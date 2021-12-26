package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import es.uji.geonews.R;

public class OpenWeatherFragment extends Fragment {
    private static final int NUM_PAGES = 3;
    private final int locationId;
    private ViewPager2 weatherPager;
    private ScreenSlideWeatherPagerAdapter weatherPagerAdapter;

    public OpenWeatherFragment(int locationId) {
        // Required empty public constructor
        this.locationId = locationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_open_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weatherPager = view.findViewById(R.id.weatherPager);
        weatherPagerAdapter = new OpenWeatherFragment.ScreenSlideWeatherPagerAdapter(getActivity());
        weatherPager.setAdapter(weatherPagerAdapter);
    }

    private class ScreenSlideWeatherPagerAdapter extends FragmentStateAdapter {
        public ScreenSlideWeatherPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return WeatherFragmentFactory.createWeatherFragment(position, locationId);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}