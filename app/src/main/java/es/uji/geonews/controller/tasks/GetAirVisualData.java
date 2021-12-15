package es.uji.geonews.controller.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import es.uji.geonews.controller.template.AirTemplate;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetAirVisualData extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final ProgressBar progressBar;
    private final Context context;
    private final AirTemplate airTemplate;
    private final PieChart pieChart;
    private final int locationId;
    private AirVisualData airVisualData;
    private String error;

    public GetAirVisualData(int locationId, AirTemplate airTemplate, ProgressBar progressBar, PieChart pieChart, Context context){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.progressBar = progressBar;
        this.pieChart = pieChart;
        this.context = context;
        this.locationId = locationId;
        this.airTemplate = airTemplate;
    }

    @Override
    public void execute() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    airVisualData = (AirVisualData) geoNewsManager.getData(ServiceName.AIR_VISUAL, locationId);
                    if (airVisualData == null) error = "Esta ubicación no esta suscrita al servicio de calidad del aire";
                } catch (ServiceNotAvailableException | NoLocationRegisteredException e) {
                    error = e.getMessage();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error != null) showAlertError();
                        else {
                            fillChart(airVisualData.getAqiUs());
                            airTemplate.getTempertaureOutput().setText(airVisualData.getTemperature() + "º C");
                            airTemplate.getPreassureOutput().setText(airVisualData.getPressure() + " hPa");
                            airTemplate.getHumidityOutput().setText(airVisualData.getHumidity() + " %");
                            airTemplate.getWindSpeedOutput().setText(airVisualData.getWindSpeed() + " m/s");
                            airTemplate.getWindDirectionOuptut().setRotation(airVisualData.getWindDirection());
                        }
                    }
                });
            }
        }).start();
    }

    private void showAlertError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(error);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void fillChart(int aqiUs){
        // Generate dataset
        ArrayList<PieEntry> data = new ArrayList<>();
        data.add(new PieEntry(aqiUs, ""));
        data.add(new PieEntry(300 - aqiUs , ""));

        // Assign colors:
        int[] colorData = new int[] {
                getAqiColor(aqiUs),
                Color.rgb(143, 180, 189)
        };

        PieDataSet pieDataSet = new PieDataSet(data, "");
        pieDataSet.setColors(colorData);
        pieDataSet.setValueTextSize(15);


        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.setCenterText(getAqiInfo(aqiUs));
        pieChart.setSaveEnabled(true);
        pieChart.setCenterTextSize(20);
        pieChart.setHoleRadius(70);
        pieChart.setTransparentCircleRadius(60);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setEnabled(false);
        pieChart.invalidate();
    }

    private String getAqiInfo(int aqiUs){
        String info = "";
        if (aqiUs < 51) info = "Calidad del aire muy buena";
        else if (aqiUs < 101) info = "Calidad del aire aceptable";
        else if (aqiUs < 151) info = "El puede ser dañino para grupos sensibles";
        else if (aqiUs < 201) info = "El aire puede ser dañino para gran parte de la población";
        else if (aqiUs < 251) info = "El aire puede ser muy dañino";
        else info = "El aire  en esta ubicación es muy peligroso";
        return info;
    }

    private int getAqiColor(int aqiUs){
        int color;
        if (aqiUs < 51) color = Color.rgb(98, 231, 19);
        else if (aqiUs < 101) color = Color.rgb(179, 231, 19);
        else if (aqiUs < 151) color = Color.rgb(245, 226, 0);
        else if (aqiUs < 201) color = Color.rgb(231, 168, 19);
        else if (aqiUs < 251) color = Color.rgb(231, 119, 19);
        else color = Color.rgb(231, 19, 19);
        return color;
    }
}
