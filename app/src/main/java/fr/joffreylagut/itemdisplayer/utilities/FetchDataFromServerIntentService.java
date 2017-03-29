package fr.joffreylagut.itemdisplayer.utilities;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import fr.joffreylagut.itemdisplayer.FragmentMainActivity;
import fr.joffreylagut.itemdisplayer.LauncherActivity;
import fr.joffreylagut.itemdisplayer.models.Photo;
import fr.joffreylagut.itemdisplayer.models.PhotoDbHelper;

/**
 * FetchDataFromServerIntentService.java
 * Purpose: Download a JSON from the server and insert its content in database.
 *
 * @author Joffrey LAGUT
 * @version 1.4 2017-03-27
 */

public class FetchDataFromServerIntentService extends IntentService {

    // Constant used to log events
    private final static String TAG = "FetchDataFromServerIS";

    // URL of the files to fetch
    private final static String ENDPOINT = "http://jsonplaceholder.typicode.com/photos";

    // Number of Photo to load from server before showing MainActivity
    private final static int NUMBER_OF_PHOTOS_TO_LOAD_AT_START =
            FragmentMainActivity.PHOTOS_PER_PAGE;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FetchDataFromServerIntentService() {
        super("FetchDataFromServerIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // We prepare an intent to broadcast to the launcher activity
        // The launcher activity will know that we have updated enough photos in db
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(LauncherActivity.PhotoDataReceiver.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);

        // Variable to handle the JSON file
        String response = "";

        // We try to download the JSON file from server
        try {
            URL urlToFetch = new URL(ENDPOINT);
            HttpURLConnection urlConnection = (HttpURLConnection) urlToFetch.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                response = stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            // We can't access to the JSON on the server.
            Log.e("ERROR", e.getMessage(), e);
            // We display the main activity.
            broadcastIntent.putExtra("isReady", false);
            sendBroadcast(broadcastIntent);
            // And stop this method.
            return;
        }


        // We have to convert the response into Photo objects.
        List<Photo> photos = convertJSONResponseInPhoto(response);

        // We have to get the instance of the DbHelper.
        PhotoDbHelper photoDbHelper = PhotoDbHelper.getInstance(this);
        SQLiteDatabase db = photoDbHelper.getReadableDatabase();

        // We declare a variable to trigger MainActivity's launch
        int position = 0;

        // Now we parse all the photos
        for (Photo photo : photos) {
            // We check if the photo is already in db.
            if (photoDbHelper.getPhotoById(db, photo.getId()) != null) {
                // We update the photo in db.
                Log.i(TAG, "Update in db " + photo.getId() + " | " + photo.getTitle());
                photoDbHelper.updatePhoto(db, photo);
            } else {
                // We insert the photo in db.
                Log.i(TAG, "Insert in db " + photo.getId() + " | " + photo.getTitle());
                photoDbHelper.addNewPhoto(db, photo);
            }

            // We increment the counter
            position++;
            // And check if we have parsed enough photos to show the activity
            if (position == NUMBER_OF_PHOTOS_TO_LOAD_AT_START) {
                // We display the main activity.
                broadcastIntent.putExtra("isReady", true);
                sendBroadcast(broadcastIntent);
            }
        }
        this.stopSelf();
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