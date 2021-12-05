package es.uji.geonews.model.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.data.Data;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import okhttp3.Request;
import okhttp3.Response;

public class AirVisualService extends ServiceHttp implements DataGetterStrategy {

    public AirVisualService() {
        super(ServiceName.AIR_VISUAL, "Air cuality descritption");
        apiKey = "bd76ecb7-90bb-4f78-ad71-8107453e8890";
        url = "api.airvisual.org";
    }

    @Override
    public boolean validateLocation(Location location) {
        String url = "https://api.airvisual.com/v2/nearest_city?"
                + "lat=" + location.getGeographCoords().getLatitude()
                + "&lon=" + location.getGeographCoords().getLongitude()
                + "&key=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            return jsonObject.getString("status").equals("success");

        } catch (IOException | JSONException exception){
            //TODO: Al validar la ubicacion, si no tenemos conexion no es
            // mejor lanzar ServiceNotAvailableException ??
            return false;
        }
    }

    @Override
    public Data getData(Location location) throws ServiceNotAvailableException {
        String url = "https://api.airvisual.com/v2/nearest_city?"
                + "lat=" + location.getGeographCoords().getLatitude()
                + "&lon=" + location.getGeographCoords().getLongitude()
                + "&key=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.getString("status").equals("success")){
               return convertToAirVisualData(jsonObject);
            }
            return null;

        } catch (IOException | JSONException exception){
            throw new ServiceNotAvailableException();
        }
    }

    @Override
    public List<Data> getFutureData(Location location) throws ServiceNotAvailableException {
        return null;
    }

    private AirVisualData convertToAirVisualData(JSONObject jsonObject) throws JSONException {
        AirVisualData airVisualData = new AirVisualData();
        airVisualData.setTemperature(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("tp"));
        airVisualData.setPressure(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("pr"));
        airVisualData.setHumidity(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("hu"));
        airVisualData.setWindSpeed(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getDouble("ws"));
        airVisualData.setWindDirection(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("wd"));
        airVisualData.setWeatherIcon(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getString("ic"));
        airVisualData.setAqiUs(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution").getInt("aqius"));
        airVisualData.setMainUs(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution").getString("mainus"));
        airVisualData.setAqiCn(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution").getInt("aqicn"));
        airVisualData.setMainCn(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution").getString("maincn"));
        return airVisualData;
    }
}
