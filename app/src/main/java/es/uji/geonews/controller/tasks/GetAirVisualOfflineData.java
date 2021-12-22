package es.uji.geonews.controller.tasks;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import es.uji.geonews.controller.charts.AirVisualPieChart;
import es.uji.geonews.controller.template.AirTemplate;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.GeoNewsManagerSingleton;
import es.uji.geonews.model.services.ServiceName;

public class GetAirVisualOfflineData extends UserTask {
    private final GeoNewsManager geoNewsManager;
    private final AirTemplate airTemplate;
    private final PieChart pieChart;
    private final int locationId;
    private AirVisualData airVisualData;
    private String error;

    public GetAirVisualOfflineData(int locationId, AirTemplate airTemplate, PieChart pieChart, Context context){
        this.geoNewsManager = GeoNewsManagerSingleton.getInstance(context);
        this.pieChart = pieChart;
        this.locationId = locationId;
        this.airTemplate = airTemplate;
    }

    @Override
    public void execute() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    airVisualData = (AirVisualData) geoNewsManager.getOfflineData(ServiceName.AIR_VISUAL, locationId);
                    if (airVisualData == null) error = "Esta ubicación no esta suscrita al servicio de calidad del aire " +
                            "o no se ha consultado información previamente";
                } catch (NoLocationRegisteredException e) {
                    error = e.getMessage();
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        if (error == null) {
                            AirVisualPieChart airVisualPieChart = new AirVisualPieChart(pieChart);
                            airVisualPieChart.fillChart(airVisualData.getAqiUs(), true);
                            airTemplate.getTempertaureOutput().setText(airVisualData.getTemperature() + "º C");
                            airTemplate.getPreassureOutput().setText(airVisualData.getPressure() + " hPa");
                            airTemplate.getHumidityOutput().setText(airVisualData.getHumidity() + " %");
                            airTemplate.getWindSpeedOutput().setText(airVisualData.getWindSpeed() + " m/s");
                            airTemplate.getWindDirectionOuptut().setRotation(airVisualData.getWindDirection());
                            airTemplate.getAqiMainTextOutput().setText(airVisualData.getAqiMainText());
                            airTemplate.getAqiTextOutput().setText(airVisualData.getAqiText());
                            airTemplate.getAqiImageOutput().setImageResource(airVisualData.getAqiImage());

                        }
                    }
                });
            }
        }).start();
    }
}
