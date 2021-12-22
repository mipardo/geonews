package es.uji.geonews.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.GetOpenWeatherTodayData;
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

        new GetOpenWeatherTodayOfflineData(locationId, weatherTemplate, getContext()).execute();
        new GetOpenWeatherTodayData(locationId, weatherTemplate, getContext()).execute();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}