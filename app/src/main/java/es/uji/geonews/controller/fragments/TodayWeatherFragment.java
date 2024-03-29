package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.uji.geonews.R;
import es.uji.geonews.controller.adapters.PrecipitationListAdapter;
import es.uji.geonews.controller.tasks.GetOpenWeatherData;
import es.uji.geonews.controller.tasks.GetOpenWeatherTodayOfflineData;
import es.uji.geonews.controller.template.WeatherTemplate;

public class TodayWeatherFragment extends Fragment {
    private final int locationId;
    private WeatherTemplate weatherTemplate;

    public TodayWeatherFragment(int locationId) {
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
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);

        ViewGroup loadingLayout = view.findViewById(R.id.greyServiceLayout);
        TextView weatherTitleOutput = view.findViewById(R.id.current_weather_title);
        TextView currentTempOutput = view.findViewById(R.id.current_temp_output);
        TextView minTempOutput = view.findViewById(R.id.min_temp_output);
        TextView maxTempOutput = view.findViewById(R.id.max_temp_output);
        ImageView iconOutput = view.findViewById(R.id.weather_icon_output);
        TextView descriptionOutput = view.findViewById(R.id.description_output);
        TextView sunriseOutput = view.findViewById(R.id.sunrise_output);
        TextView sunsetOuptut = view.findViewById(R.id.sunset_output);
        TextView uvOutput = view.findViewById(R.id.uv_output);
        TextView visibilityOutput = view.findViewById(R.id.visibility_output);
        TextView moonriseOutput = view.findViewById(R.id.moonrise_output);
        TextView moonsetOutput = view.findViewById(R.id.moonset_output);
        TextView feelsLikeOutput = view.findViewById(R.id.feels_like_output);
        TextView cloudsPercentageOutput = view.findViewById(R.id.clouds_percentage_output);

        RecyclerView recyclerView = view.findViewById(R.id.precipitation_recycler_view);
        recyclerView.setAdapter(new PrecipitationListAdapter(new ArrayList<>()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        WeatherTemplate weatherTemplate = new WeatherTemplate();
        weatherTemplate.setWeatherTitleOutput(weatherTitleOutput);
        weatherTemplate.setLoadingLayout(loadingLayout);
        weatherTemplate.setCurrentTempOutput(currentTempOutput);
        weatherTemplate.setMinTempOutput(minTempOutput);
        weatherTemplate.setMaxTempOutput(maxTempOutput);
        weatherTemplate.setIconOutput(iconOutput);
        weatherTemplate.setDescriptionOutput(descriptionOutput);
        weatherTemplate.setSunriseOutput(sunriseOutput);
        weatherTemplate.setSunsetOuptut(sunsetOuptut);
        weatherTemplate.setUvOutput(uvOutput);
        weatherTemplate.setVisibilityOutput(visibilityOutput);
        weatherTemplate.setPrecipitationsOutput(recyclerView);
        weatherTemplate.setMoonriseOutput(moonriseOutput);
        weatherTemplate.setMoonsetOutput(moonsetOutput);
        weatherTemplate.setFeelsLikeOutput(feelsLikeOutput);
        weatherTemplate.setCloudsPercentageOutput(cloudsPercentageOutput);

        new GetOpenWeatherTodayOfflineData(locationId, weatherTemplate, getContext()).execute();
        new GetOpenWeatherData(locationId, weatherTemplate, getContext()).execute();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}