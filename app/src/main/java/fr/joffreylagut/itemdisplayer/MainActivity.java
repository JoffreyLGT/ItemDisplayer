package fr.joffreylagut.itemdisplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.joffreylagut.itemdisplayer.utilities.FetchDataFromServer;

/**
 * MainActivity.java
 * Purpose: Main activity of the application.
 * For the moment, display Hello world.
 * It'll display a list of items in the future.
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-24
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch data from server
        FetchDataFromServer.updateInformation(this);
    }
}
