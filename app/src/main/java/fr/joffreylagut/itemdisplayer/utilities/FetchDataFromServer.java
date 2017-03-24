package fr.joffreylagut.itemdisplayer.utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

import fr.joffreylagut.itemdisplayer.models.Photo;

/**
 * FetchDataFromServer.java
 * Purpose: Download a JSON from the server and parse it to create a List<Photo.java>
 * The download is made using Volley.
 *
 * @author Joffrey LAGUT
 * @version 1.1 2017-03-26
 */

public abstract class FetchDataFromServer {

    // Constant used to log events
    private static final String TAG = "FetchDataFromServer";

    // URL of the files to fetch
    private static final String ENDPOINT = "http://jsonplaceholder.typicode.com/photos";

    // Variables
    private static RequestQueue requestQueue;

    /**
     * Response ErrorListener triggered when the app is unable to download the JSON from server.
     * It display the error in the logs.
     */
    private static final Response.ErrorListener onPhotosError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(TAG, error.toString());
    }
    };

    /**
     * Response listener triggered when the JSON is loaded from the server.
     * It create a lit of Photo using GSON and display their information in the logs.
     */
    private static final Response.Listener<String> onPhotosLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            // We have to convert the response into Photo objects.
            List<Photo> photos = convertJSONResponseInPhoto(response);

            // For information, we display all the photos information in the log.
            for (Photo photo : photos) {
                Log.i(TAG, photo.getId() + " | " + photo.getTitle()
                        + " | " + photo.getUrl() + " | " + photo.getThumbnailUrl());
            }
        }
    };

    /**
     * Fetch the photos on the server and display them in the logs.
     *
     * @param context Context of the application.
     */
    public static void updateInformation(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        fetchPhotos();

    }

    /**
     * Create and execute a Request with Volley.
     */
    private static void fetchPhotos() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT,
                onPhotosLoaded, onPhotosError);
        requestQueue.add(request);
    }

    /**
     * This function convert the JSON string into a List<Photo>
     *
     * @param json String version of the json file to convert into objects.
     * @return the list of photos included in the JSON.
     */
    static List<Photo> convertJSONResponseInPhoto(String json) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        List<Photo> photos = Arrays.asList(gson.fromJson(json, Photo[].class));
        Log.i(TAG, photos.size() + " photos loaded.");

        return photos;
    }
}