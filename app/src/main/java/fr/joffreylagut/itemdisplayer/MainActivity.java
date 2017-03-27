package fr.joffreylagut.itemdisplayer;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.joffreylagut.itemdisplayer.models.PhotoDbHelper;

/**
 * MainActivity.java
 * Purpose: Main activity of the application.
 * For the moment, display Hello world.
 * It'll display a list of items in the future.
 *
 * @author Joffrey LAGUT
 * @version 1.1 2017-03-27
 */
public class MainActivity extends AppCompatActivity {

    // Variables
    PhotoDbHelper mPhotoDbHelper;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
