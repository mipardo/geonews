package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import es.uji.geonews.R;

public class LocationServicesFragment extends Fragment {
    private static final int NUM_PAGES = 3;
    private int locationId;
    private ViewPager2 mPager;
    private ScreenSlidePagerAdapter pagerAdapter;

    public LocationServicesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments().getInt("locationId");

        RelativeLayout settings =  getActivity().findViewById(R.id.settings);
        settings.setVisibility(View.VISIBLE);

        mPager = view.findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getActivity());
        mPager.setAdapter(pagerAdapter);
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return ServiceFragmentFactory.createServiceFragment(position, locationId);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}