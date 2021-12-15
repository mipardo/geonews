package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import es.uji.geonews.model.data.OpenWeatherForecastData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetOpenWeatherChartData extends UserTask {
    private final Context context;
    private final int locationId;
    private final LineChart lineChart;
    private final ProgressBar progressBar;
    private List<ServiceData> forecast;
    private String error;

    private static final ValueFormatter celsiusFormatter = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            return (int)value + "º";
        }
    };

    public GetOpenWeatherChartData(int locationId, LineChart lineChart, Context context, ProgressBar progressBar) {
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
                    forecast = GeoNewsManagerSingleton.getInstance(context).getFutureData(ServiceName.OPEN_WEATHER, locationId);
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error != null) showAlertError();
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
        List<Entry> actualTempEntries = new ArrayList<>();

        int lastDay = LocalDateTime.ofInstant(Instant.ofEpochMilli(((OpenWeatherForecastData) forecast.get(0)).getTimestamp() * 1000),
                TimeZone.getDefault().toZoneId()).getDayOfMonth();
        double actualMean = 0;
        int recordCounter = 0;
        for (ServiceData windowForecast : forecast) {
            OpenWeatherForecastData forecastSection = (OpenWeatherForecastData) windowForecast;
            int day = LocalDateTime.ofInstant(Instant.ofEpochMilli(forecastSection.getTimestamp() * 1000),
                    TimeZone.getDefault().toZoneId()).getDayOfMonth();

            if (day != lastDay) {
                actualTempEntries.add(new Entry(lastDay, Math.round(actualMean / recordCounter)));
                actualMean = 0;
                recordCounter = 0;
                lastDay = day;
            }

            actualMean += forecastSection.getActTemp();
            recordCounter ++;



        }
        List<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet actualDataSet = new LineDataSet(actualTempEntries, "Temperatura");
        actualDataSet.setCircleColor(Color.RED);
        actualDataSet.setColor(Color.RED);
        dataSets.add(actualDataSet);

        return new LineData(dataSets);
    }

    private void showAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error al obtener la predicción del tiempo");
        builder.setMessage("Pruebe más tarde");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
