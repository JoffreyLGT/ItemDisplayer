package fr.joffreylagut.itemdisplayer;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.joffreylagut.itemdisplayer.models.Album;
import fr.joffreylagut.itemdisplayer.models.Photo;
import fr.joffreylagut.itemdisplayer.models.PhotoDbHelper;
import fr.joffreylagut.itemdisplayer.utilities.EndlessRecyclerOnScrollListener;

/**
 * FragmentMainActivity.java
 * Purpose: Manage a fragment instance on the MainActivity.
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-28
 */

public class FragmentMainActivity extends android.support.v4.app.Fragment {

    // Constant used to put the value into the bundle
    private static final String TYPE_RECYCLER_VIEW = "typeRecyclerView";
    public static final int PHOTOS_PER_PAGE = 27;
    public static final int ALBUMS_PER_PAGE = 5;

    // Constant that helper the user to choose which RecyclerView will be displayed in the fragment
    public static final int TYPE_RECYCLERVIEW_PHOTOS = 1;
    public static final int TYPE_RECYCLERVIEW_ALBUMS = 2;

    // TAG that is displayed in the logs
    private static String TAG = "FragmentMainActivity";

    // Global variable
    private int typeRecyclerView;
    private PhotoDbHelper mPhotoDbHelper;
    private SQLiteDatabase mDb;
    private List<Photo> photos;
    private List<Album> albums;

    // Views
    @BindView(R.id.fragment_content)
    View mContent;
    @BindView(R.id.fragment_recyclerview)
    RecyclerView mRecyclerView;

    /**
     * Function that create a new MainActivity fragment.
     *
     * @param typeRecyclerView that indicate if we want to display photos or albums in the
     *                         RecyclerView
     * @return a MainActivity fragment.
     */
    public static android.support.v4.app.Fragment newInstance(int typeRecyclerView) {
        // We have to be sure that the parameter is good
        if (typeRecyclerView != TYPE_RECYCLERVIEW_PHOTOS &&
                typeRecyclerView != TYPE_RECYCLERVIEW_ALBUMS) {
            // It's not the case. We log an error and return null
            Log.e(TAG, "newInstance: The specified type of RecyclerView isn't recognize.");
            return null;
        }
        // Now we create a new instance
        android.support.v4.app.Fragment frag = new FragmentMainActivity();
        // And put the value if TypeRecyclerView into a Bundle
        Bundle args = new Bundle();
        args.putInt(TYPE_RECYCLER_VIEW, typeRecyclerView);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lists, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve typeRecyclerView from bundle or savedInstanceState
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            typeRecyclerView = args.getInt(TYPE_RECYCLER_VIEW);
        } else {
            typeRecyclerView = savedInstanceState.getInt(TYPE_RECYCLER_VIEW);
        }

        // We initialize the views
        ButterKnife.bind(this, view);

        // We have to get the instance of the DbHelper.
        mPhotoDbHelper = PhotoDbHelper.getInstance(view.getContext());
        mDb = mPhotoDbHelper.getReadableDatabase();

        // We know have to display the right RecyclerView
        if (typeRecyclerView == TYPE_RECYCLERVIEW_PHOTOS) {
            // We initialize the list
            photos = new ArrayList<>();
            // We retrieve the photos
            retrievePhotos(1);

            // And set the RecyclerView
            GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.setAdapter(new PhotoListAdapter(photos));

            // Retain an instance so that you can call `resetState()` for fresh searches
            mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(
                    gridLayoutManager, 12) {
                @Override
                public void onLoadMore(int current_page) {
                    retrievePhotos(current_page);
                }
            });
        } else {

            // We initialize the list
            albums = new ArrayList<>();
            // We retrieve the photos
            retrieveAlbums(1);

            // And set the RecyclerView
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(new AlbumListAdapter(albums));

            // Retain an instance so that you can call `resetState()` for fresh searches
            mRecyclerView.addOnScrollListener(
                    new EndlessRecyclerOnScrollListener(linearLayoutManager, 2) {
                        @Override
                        public void onLoadMore(int current_page) {
                            retrieveAlbums(current_page);
                        }
                    });
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(TYPE_RECYCLER_VIEW, typeRecyclerView);
        super.onSaveInstanceState(outState);
    }

    /**
     * This method is retrieving photos from the database depending on the page number.
     *
     * @param pageNumber number of the page that we wants to display.
     */
    private void retrievePhotos(int pageNumber) {
        photos.addAll(mPhotoDbHelper.getAllPhotos(mDb, pageNumber, PHOTOS_PER_PAGE));
    }

    /**
     * This method is retrieving albums from the database depending on the page number.
     *
     * @param pageNumber number of the page that we wants to display.
     */
    private void retrieveAlbums(int pageNumber) {
        albums.addAll(mPhotoDbHelper.getAllAlbums(mDb, pageNumber, ALBUMS_PER_PAGE));
    }
}
