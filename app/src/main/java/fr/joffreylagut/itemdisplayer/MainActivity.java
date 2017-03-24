package fr.joffreylagut.itemdisplayer;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;

import fr.joffreylagut.itemdisplayer.models.PhotoDbHelper;
import fr.joffreylagut.itemdisplayer.utilities.FetchDataFromServerIntentService;

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

    // Variables
    PhotoDbHelper mPhotoDbHelper;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Stetho initialization
        Stetho.initializeWithDefaults(this);

        // We are declaring a new UserDbHelper to access to the db.
        mPhotoDbHelper = PhotoDbHelper.getInstance(this);
        mDb = mPhotoDbHelper.getWritableDatabase();

        // Fetch data from server
        Intent fetchDataFromServer = new Intent(this, FetchDataFromServerIntentService.class);
        startService(fetchDataFromServer);
    }
}
