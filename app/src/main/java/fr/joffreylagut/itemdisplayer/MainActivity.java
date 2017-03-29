package fr.joffreylagut.itemdisplayer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * MainActivity.java
 * Purpose: Main activity of the application.
 * For the moment, display Hello world.
 * It'll display a list of items in the future.
 *
 * @author Joffrey LAGUT
 * @version 1.2 2017-03-27
 */
public class MainActivity extends AppCompatActivity {

    // Constants used to know which fragment is currently displayed
    private static final int FRAGMENT_PHOTO = 1;
    private static final int FRAGMENT_ALBUM = 2;
    private static final String CURRENT_FRAGMENT = "currentFragment";

    // Binds
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    // By default, we display the fragment containing all the photos
    private int currentTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // We bind the activity views
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            // By default, we set the fragment displaying all the photos
            currentTab = FRAGMENT_PHOTO;
            setFragment(currentTab);
        } else {
            currentTab = savedInstanceState.getInt(CURRENT_FRAGMENT);
            if (currentTab == FRAGMENT_PHOTO) {
                setTitle(getResources().getString(R.string.title_all_photos));
            } else {
                setTitle(getResources().getString(R.string.title_all_albums));
            }
        }

        // We create a new listener for the menu
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_albums:
                                // We are setting a new fragment only if the current one is
                                // different
                                if (currentTab != 2) {
                                    setFragment(FragmentMainActivity.TYPE_RECYCLERVIEW_ALBUMS);
                                    currentTab = 2;
                                }
                                break;
                            case R.id.action_photos:
                                // We are setting a new fragment only if the current one is
                                // different
                                if (currentTab != 1) {
                                    setFragment(FragmentMainActivity.TYPE_RECYCLERVIEW_PHOTOS);
                                    currentTab = 1;
                                }
                                break;
                        }
                        return true;
                    }
                });
    }

    /**
     * This method create and display the FragmentMainActivity depending of the type in parameter.
     *
     * @param typeRecyclerView RecyclerView that we wants to show. The list of possibilities is
     *                         available in FragmentMainActivity.java.
     */
    private void setFragment(int typeRecyclerView) {

        // We create a new fragment variable.
        android.support.v4.app.Fragment frag = null;

        // We get the FragmentTransaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // We know create a new instance of FragmentMainActivity depending on the typeRecyclerView.
        switch (typeRecyclerView) {
            case FragmentMainActivity.TYPE_RECYCLERVIEW_ALBUMS:
                setTitle(R.string.title_all_albums);
                frag = FragmentMainActivity.newInstance(
                        FragmentMainActivity.TYPE_RECYCLERVIEW_ALBUMS, -1);
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case FragmentMainActivity.TYPE_RECYCLERVIEW_PHOTOS:
                setTitle(R.string.title_all_photos);
                frag = FragmentMainActivity.newInstance(
                        FragmentMainActivity.TYPE_RECYCLERVIEW_PHOTOS, -1);
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }

        // We replace the fragment
        if (frag != null) {
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // When we have the rotation, we have to save the current fragment displayed
        outState.putInt(CURRENT_FRAGMENT, currentTab);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //currentTab = savedInstanceState.getInt(CURRENT_FRAGMENT);
    }
}
