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
 * @version 1.0 2017-03-24
 */

public abstract class FetchDataFromServer {

    // Constant used to log events
    private static final String TAG = "FetchDataFromServer";

    // URL of the files to fetch
    private static final String ENDPOINT = "http://jsonplaceholder.typicode.com/photos";
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
    // Variables
    private static RequestQueue requestQueue;
    private static Gson gson;
    /**
     * Response listener triggered when the JSON is loaded from the server.
     * It create a lit of Photo using GSON and display their information in the logs.
     */
    private static final Response.Listener<String> onPhotosLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            List<Photo> photos = Arrays.asList(gson.fromJson(response, Photo[].class));

            Log.i(TAG, photos.size() + " photos loaded.");

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

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

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
}