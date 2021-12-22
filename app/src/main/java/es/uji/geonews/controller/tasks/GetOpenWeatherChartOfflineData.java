package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;

import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetOpenWeatherChartOfflineData extends UserTask {
    private final Context context;
    private final int locationId;
    private final LineChart lineChart;
    private OpenWeatherData data;
    private String error;

    private static final ValueFormatter celsiusFormatter = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            return (int) value + "º";
        }
    };

    public GetOpenWeatherChartOfflineData(int locationId, LineChart lineChart, Context context) {
        this.locationId = locationId;
        this.lineChart = lineChart;
        this.context = context;
    }
    @Override
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data = (OpenWeatherData) GeoNewsManagerSingleton.getInstance(context).getOfflineData(ServiceName.OPEN_WEATHER, locationId);
                } catch (NoLocationRegisteredException e) {
                    error = e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error != null) showAlertError();
                        else if (data != null){
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


        return new LineData();
    }

    private void showAlertError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error al obtener la predicción del tiempo");
        builder.setMessage("Pruebe más tarde");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
