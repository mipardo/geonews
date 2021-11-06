package es.uji.geonews.model.services;


import java.io.IOException;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.OpenWeatherLocationData;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import es.uji.geonews.model.GeographCoords;

public class CoordsSearchService extends ServiceHttp  {

    public CoordsSearchService() {
        super("Geocode", "Coordinates Search Service");
        apiKey = "375488105344812237611x41680";
    }


    public GeographCoords getCoordsFrom(String placeName)
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        String url = "https://geocode.xyz/"+ placeName +"?json=1" +
                "&auth=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;
        GeographCoords geographCoords;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.has("error")){
               throw new UnrecognizedPlaceNameException();
            }
            double longt = jsonObject.getDouble("longt");
            double latt = jsonObject.getDouble("latt");
            geographCoords = new GeographCoords(latt, longt);
        } catch (IOException | JSONException exception){
            throw new ServiceNotAvailableException();
        }
        return geographCoords;
    }

    public String getPlaceNameFromCoords(GeographCoords coords)
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        String url = "https://geocode.xyz/"+ coords.toString() +"?json=1" +
        "&auth=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;
        String placeName;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.has("error")){
                if (jsonObject.getJSONObject("error").get("code").equals("008")){
                    // If error code == 008 => place name not found
                    return null;
                }
                else throw new NotValidCoordinatesException();
            }
            placeName = jsonObject.getJSONObject("osmtags").getString("name");
        } catch (IOException | JSONException exception){
            throw new ServiceNotAvailableException();
        }
        return placeName;
    }

    @Override
    public boolean validateLocation(Location location){
        return true;
    }

    @Override
    public void checkConnection() {
        //TODO: This method should connect to the API to check if it is possible to connect
        isActive = true;
    }

    @Override
    public OpenWeatherLocationData getDataFrom(String placeName) {
        return null;
    }
}
