package fr.joffreylagut.itemdisplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.stetho.Stetho;

import fr.joffreylagut.itemdisplayer.utilities.FetchDataFromServerIntentService;

/**
 * LauncherActivity.java
 * Purpose: Launcher activity of the app.
 * This activity is waiting for a response from the service to show the main activity.
 * For the first launch, the user need an internet connexion to download data.
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-27
 */
public class LauncherActivity extends AppCompatActivity {

    private boolean firstLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        // Stetho initialization
        Stetho.initializeWithDefaults(this);

        // We have to initialize the IntentFilter
        IntentFilter filter = new IntentFilter(PhotoDataReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        PhotoDataReceiver receiver = new PhotoDataReceiver();
        registerReceiver(receiver, filter);

        // We check if this is the first launch
        SharedPreferences preferences = getSharedPreferences("fr.joffreylagut.itemdisplayer"
                , MODE_PRIVATE);
        firstLaunch = preferences.getBoolean("firstrun", true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // We hide the error message
        findViewById(R.id.tv_launch_error).setVisibility(View.INVISIBLE);

        // Fetch data from server
        Intent fetchDataFromServer = new Intent(this, FetchDataFromServerIntentService.class);
        startService(fetchDataFromServer);
    }

    /**
     * This class is created to receive the response from the service responsible to load the data.
     */
    public class PhotoDataReceiver extends BroadcastReceiver {

        public static final String PROCESS_RESPONSE =
                "fr.joffreylagut.itemdisplayer.intent.action.PROCESS_RESPONSE";

        @Override
        public void onReceive(Context context, Intent intent) {
            // We get the variable from the service that indicate if a download occurred or not
            Boolean isReady = intent.getBooleanExtra("isReady", false);

            // If it's the first launch and there is no internet, the user don't have any data so
            // we can't display the MainActivity.
            if (firstLaunch && !isReady) {
                // We show the error message to the user
                findViewById(R.id.tv_launch_error).setVisibility(View.VISIBLE);
            } else {
                // This is not the first launch anymore, we write the information in the preferences
                SharedPreferences preferences = getSharedPreferences("fr.joffreylagut.itemdisplayer"
                        , MODE_PRIVATE);
                preferences.edit().putBoolean("firstrun", false).apply();

                // We launch the main activity
                Intent mainActivity = new Intent(context, MainActivity.class);
                context.startActivity(mainActivity);
            }
        }
    }
}
