package com.cs360.williambingham.bingham_william_c360_final_project.locator;

import android.location.Location;
import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GoogleFetcher {
    private static final String TAG = "GoogleFetcher";

    //Google Maps API Key
    private static final String API_KEY = "AjyHfx3hKDl9RH0cfGvh28VMn3CHQHNdlxeLxYjFjy9L4dk2kqApzcgSS5t_ryhR";

    private static final Uri ENDPOINT = Uri
            .parse("https://dev.virtualearth.net/REST/v1/LocalSearch/")
            .buildUpon()
            .appendQueryParameter("key", API_KEY)
            .appendQueryParameter("type", "Parks")
            .build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> searchPhotos(Location location) {
        String url = buildUrl(location);
        return downloadGalleryItems(url);
    }

    private List<GalleryItem> downloadGalleryItems(String url) {
        List<GalleryItem> items = new ArrayList<>();

        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    private String buildUrl(Location location) {
        return ENDPOINT.buildUpon()
                .appendQueryParameter("userLocation", "" + location.getLatitude() + "," + location.getLongitude())
                .build().toString();
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray resourceSetsJsonArray = jsonBody.getJSONArray("resourceSets");
        for (int i = 0; i < resourceSetsJsonArray.length(); i++) {
            JSONObject resourceSetJsonArray = resourceSetsJsonArray.getJSONObject(i);
            JSONArray resourceJsonArray = resourceSetJsonArray.getJSONArray("resources");
            for (int j = 0; j < resourceJsonArray.length(); j++) {
                JSONObject resourceJsonObject = resourceJsonArray.getJSONObject(j);

                GalleryItem item = new GalleryItem();
                item.setId(resourceJsonObject.getString("name"));
                item.setCaption(resourceJsonObject.getString("name"));

                JSONObject pointObject = resourceJsonObject.getJSONObject("point");
                JSONArray coordinatesArray = pointObject.getJSONArray("coordinates");
                double latitude = coordinatesArray.getDouble(0);
                double longitude = coordinatesArray.getDouble(1);
                item.setLat(latitude);
                item.setLon(longitude);
                items.add(item);
            }
        }
    }
}
