package es.uji.geonews.controller.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import java.time.LocalDate;

import es.uji.geonews.R;
import es.uji.geonews.controller.tasks.GetForecastChartData;
import es.uji.geonews.controller.tasks.GetTomorrowWeatherData;
import es.uji.geonews.controller.template.WeatherTemplate;


public class TomorrowWeatherFragment extends Fragment {
    private int locationId;

    public TomorrowWeatherFragment(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tomorrow_weather, container, false);

        ConstraintLayout layout = view.findViewById(R.id.tomorrowWeatherLayout);
        layout.setBackground(getSeasonBackground());
        layout.setAlpha(0.8f);
        LineChart lineChart = view.findViewById(R.id.tomorrowChart);
        WeatherTemplate weatherTemplate = new WeatherTemplate();
        weatherTemplate.setDateTextview(view.findViewById(R.id.dateTextview));
        weatherTemplate.setMaxTempTextview(view.findViewById(R.id.maxTempTextview));
        weatherTemplate.setMinTempTextview(view.findViewById(R.id.minTempTextview));
        weatherTemplate.setActualTempTextview(view.findViewById(R.id.actualTempTextview));
        weatherTemplate.setWeatherDescriptionTextview(view.findViewById(R.id.actualWeatherDescriptionTextview));
        weatherTemplate.setWeatherIcon(view.findViewById(R.id.actualWeatherIconTextview));
        weatherTemplate.setProgressBar(view.findViewById(R.id.tomorrow_progress_bar));

        new GetTomorrowWeatherData(locationId, weatherTemplate, getContext()).execute();
        new GetForecastChartData(locationId, lineChart, getContext(), weatherTemplate.getProgressBar()).execute();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private Drawable getSeasonBackground() {
        final int DAY_SPRING_MIN = 80;
        final int DAY_SPRING_MAX = 172;
        final int DAY_SUMMER_MIN = DAY_SPRING_MAX;
        final int DAY_SUMMER_MAX = 264;
        final int DAY_FALL_MIN = DAY_SUMMER_MAX;
        final int DAY_FALL_MAX = 355;

        int dayOfYear = LocalDate.now().getDayOfYear();

        if (dayOfYear > DAY_SPRING_MIN && dayOfYear <= DAY_SPRING_MAX) {
            return AppCompatResources.getDrawable(getContext(), R.mipmap.spring);
        } else if (dayOfYear > DAY_SUMMER_MIN && dayOfYear <= DAY_SUMMER_MAX) {
            return AppCompatResources.getDrawable(getContext(), R.mipmap.summer);
        } else if (dayOfYear > DAY_FALL_MIN && dayOfYear <= DAY_FALL_MAX) {
            return AppCompatResources.getDrawable(getContext(), R.mipmap.autumn);
        }
        return AppCompatResources.getDrawable(getContext(), R.mipmap.winter);
    }
}