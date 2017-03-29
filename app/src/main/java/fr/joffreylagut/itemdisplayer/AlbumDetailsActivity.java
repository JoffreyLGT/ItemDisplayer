package fr.joffreylagut.itemdisplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * AlbumDetailsActivity.java
 * Purpose: Activity that display the pictures included in an album.
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-29
 */

public class AlbumDetailsActivity extends AppCompatActivity {

    // Constant used to define the name of the attribute inside of the opening intent
    public static final String ALBUM_ID = "albumId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        int albumId;

        // We retrieve AlbumID from bundle or savedInstanceState
        if (savedInstanceState == null) {
            albumId = getIntent().getIntExtra(ALBUM_ID, -1);
        } else {
            albumId = savedInstanceState.getInt(ALBUM_ID);
        }

        setTitle("List of photos in album no " + String.valueOf(albumId));

        // We create a new fragment variable.
        android.support.v4.app.Fragment frag = null;

        // We get the FragmentTransaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        frag = FragmentMainActivity.newInstance(
                FragmentMainActivity.TYPE_RECYCLERVIEW_DETAILS_ALBUM, albumId);

        // We replace the fragment
        if (frag != null) {
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // We want to display an animation to go back on the previous activity
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // There is only the button to come back in the menu so we call onBackPressed
        // To display the animation and finish the activity
        this.onBackPressed();
        return true;
    }
}
