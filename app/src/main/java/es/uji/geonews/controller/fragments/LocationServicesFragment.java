package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.uji.geonews.R;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class LocationServicesFragment extends Fragment {
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

        GeoNewsManager geoNewsManager = GeoNewsManagerSingleton.getInstance(getContext());
        Set<ServiceName> activeServices =  new HashSet<>(geoNewsManager.getServicesOfLocation(locationId));
        Set<ServiceName> generalActiveServices = new HashSet<>(geoNewsManager.getActiveServices());
        activeServices.retainAll(generalActiveServices);

        mPager = view.findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getActivity(), new ArrayList<>(activeServices));
        mPager.setAdapter(pagerAdapter);
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Toolbar toolbar = getActivity().findViewById(R.id.my_toolbar);
                toolbar.setTitle(pagerAdapter.getFragmentLabel(position));
            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private final List<ServiceName> locationServices;
        private final int num_pages;

        public ScreenSlidePagerAdapter(FragmentActivity fa, List<ServiceName> locationServices) {
            super(fa);
            this.locationServices = locationServices;
            Collections.sort(this.locationServices);

            this.num_pages = locationServices.size() > 0 ? locationServices.size() : 1;
        }

        @Override
        public Fragment createFragment(int position) {
            return locationServices.size() > 0 ?
                    ServiceFragmentFactory.createServiceFragment(locationServices.get(position), locationId) :
                    new NoServiceAvailableFragment();
        }

        @Override
        public int getItemCount() {
            return num_pages;
        }

        public String getFragmentLabel(int position) {
            return locationServices.size() > 0 ?
                    locationServices.get(position).label :
                    "Servicios";
        }
    }
}