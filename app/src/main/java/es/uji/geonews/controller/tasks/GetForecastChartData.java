package es.uji.geonews.controller.tasks;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.data.OpenWeatherForecastData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetForecastChartData extends UserTask {
    private Context context;
    private int locationId;
    private LineChart lineChart;
    private ProgressBar progressBar;
    private List<Data> forecast;
    private String error;

    private static final ValueFormatter celsiusFormatter = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            return (int)value + "º";
        }
    };

    public GetForecastChartData(int locationId, LineChart lineChart, Context context, ProgressBar progressBar) {
        this.locationId = locationId;
        this.lineChart = lineChart;
        this.context = context;
        this.progressBar = progressBar;
    }

    @Override
    public void execute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO borrar addServiceToLocation de aquí
                    GeoNewsManagerSingleton.getInstance(context).addServiceToLocation(ServiceName.OPEN_WEATHER, locationId);
                    forecast = GeoNewsManagerSingleton.getInstance(context).getFutureData(ServiceName.OPEN_WEATHER, locationId);
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();

                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error != null);
                        else
                        {
                            lineChart.setData(generateGraph());
                            lineChart.getAxisRight().setDrawLabels(false);
                            lineChart.getXAxis().setGranularity(1f);
                            lineChart.getAxisLeft().setGranularity(1f);
                            lineChart.getAxisLeft().setValueFormatter(celsiusFormatter);
                            lineChart.getDescription().setEnabled(false);
                            lineChart.invalidate();
                            lineChart.getLineData().setValueFormatter(celsiusFormatter);
                        }
                    }

                });
            }

        }).start();
    }

    private LineData generateGraph() {
//        List<Entry> minTempEntries = new ArrayList<>();
//        List<Entry> maxTempEntries = new ArrayList<>();
        List<Entry> actualTempEntries = new ArrayList<>();

        int lastDay = LocalDateTime.ofInstant(Instant.ofEpochMilli(((OpenWeatherForecastData) forecast.get(0)).getTimestamp() * 1000),
                TimeZone.getDefault().toZoneId()).getDayOfMonth();
//        double minMean = 0;
//        double maxMean = 0;
        double actualMean = 0;
        int recordCounter = 0;
        for (Data windowForecast : forecast) {
            OpenWeatherForecastData forecastSection = (OpenWeatherForecastData) windowForecast;
            int day = LocalDateTime.ofInstant(Instant.ofEpochMilli(forecastSection.getTimestamp() * 1000),
                    TimeZone.getDefault().toZoneId()).getDayOfMonth();

            if (day != lastDay) {
//                minTempEntries.add(new Entry(lastDay, Math.round(minMean / recordCounter)));
//                maxTempEntries.add(new Entry(lastDay, Math.round(maxMean / recordCounter)));
                actualTempEntries.add(new Entry(lastDay, Math.round(actualMean / recordCounter)));
//                minMean = 0;
//                maxMean = 0;
                actualMean = 0;
                recordCounter = 0;
                lastDay = day;
            }
//            minMean += forecastSection.getMinTemp();
//            maxMean += forecastSection.getMaxTemp();
            actualMean += forecastSection.getActTemp();
            recordCounter ++;



        }
        List<ILineDataSet> dataSets = new ArrayList<>();

//        LineDataSet minDataSet = new LineDataSet(minTempEntries, "Min");
//        minDataSet.setCircleColor(Color.BLUE);
//        minDataSet.setColor(Color.BLUE);
//        dataSets.add(minDataSet);
//
//        LineDataSet maxDataSet = new LineDataSet(maxTempEntries, "Max");
//        maxDataSet.setCircleColor(Color.RED);
//        maxDataSet.setColor(Color.RED);
//        dataSets.add(maxDataSet);

        LineDataSet actualDataSet = new LineDataSet(actualTempEntries, "Temperatura");
        actualDataSet.setCircleColor(Color.RED);
        actualDataSet.setColor(Color.RED);
        dataSets.add(actualDataSet);

        return new LineData(dataSets);
    }
}
