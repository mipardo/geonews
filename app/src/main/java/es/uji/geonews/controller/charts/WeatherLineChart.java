package es.uji.geonews.controller.charts;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.data.DailyWeather;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.data.Weather;

public class WeatherLineChart {
    private final LineChart lineChart;

    public WeatherLineChart(LineChart lineChart){
        this.lineChart = lineChart;
    }

    public void fillChart(OpenWeatherData openWeatherData){
        List<DailyWeather> forecast = openWeatherData.getDailyWeatherList();
        ArrayList<Entry> dataValues = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();
        for (int i = 0; i < 7; i++) {
            dataValues.add(new Entry(
                    today.plusDays(i).getDayOfMonth(),
                    (float) (forecast.get(i).getPop() * 100)));
        }

        LineDataSet lineDataSet = new LineDataSet(dataValues, "");
        lineDataSet.setDrawFilled(true);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(false);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxis(YAxis.AxisDependency.LEFT).setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxis(YAxis.AxisDependency.LEFT).setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        lineChart.getAxis(YAxis.AxisDependency.LEFT).setAxisMaximum(100);
        lineChart.getAxis(YAxis.AxisDependency.LEFT).setAxisMinimum(0);
        lineChart.getAxis(YAxis.AxisDependency.RIGHT).setEnabled(false);
        lineChart.animateX(2000);
        lineChart.invalidate();
    }


}
