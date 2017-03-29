package fr.joffreylagut.itemdisplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertTrue;

/**
 * FragmentMainActivityUnitTest.java
 * Purpose: Handle the unit tests of FragmentMainActivity.java
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-26
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Bundle.class})
public class FragmentMainActivityUnitTest {

    @Mock
    private Bundle bundle;

    @Before
    public void setUp() {
        bundle = new Bundle();
    }

    @Test
    public void newInstance_RecyclerViewPhotos() throws Exception {
        // Mock
        PowerMockito.mockStatic(Log.class);

        // Preparation
        int typeRecyclerView = FragmentMainActivity.TYPE_RECYCLERVIEW_PHOTOS;
        int albumId = 0;
        Fragment fragment = null;

        // Run
        fragment = FragmentMainActivity.newInstance(typeRecyclerView, albumId);

        // Result
        assertTrue(fragment != null);
    }

    @Test
    public void newInstance_RecyclerViewAlbums() throws Exception {
        // Mock
        PowerMockito.mockStatic(Log.class);

        // Preparation
        int typeRecyclerView = FragmentMainActivity.TYPE_RECYCLERVIEW_ALBUMS;
        int albumId = 0;
        Fragment fragment = null;

        // Run
        fragment = FragmentMainActivity.newInstance(typeRecyclerView, albumId);

        // Result
        assertTrue(fragment != null);
    }

    @Test
    public void newInstance_RecyclerViewAlbumDetails() throws Exception {
        // Mock
        PowerMockito.mockStatic(Log.class);

        // Preparation
        int typeRecyclerView = FragmentMainActivity.TYPE_RECYCLERVIEW_DETAILS_ALBUM;
        int albumId = 1;
        Fragment fragment = null;

        // Run
        fragment = FragmentMainActivity.newInstance(typeRecyclerView, albumId);

        // Result
        assertTrue(fragment != null);
    }

    @Test
    public void newInstance_RecyclerViewError() throws Exception {
        // Mock
        PowerMockito.mockStatic(Log.class);

        // Preparation
        int typeRecyclerView = 12;
        int albumId = 0;
        Fragment fragment = null;

        // Run
        fragment = FragmentMainActivity.newInstance(typeRecyclerView, albumId);

        // Result
        assertTrue(fragment == null);
    }
}
