package fr.joffreylagut.itemdisplayer;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import fr.joffreylagut.itemdisplayer.models.Album;
import fr.joffreylagut.itemdisplayer.models.Photo;

/**
 * AlbumListAdapter.java
 * Purpose: Adapter for the RecyclerView that is showing albums.
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-27
 */

class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder> {

    // List of photos that we want to show
    private List<Album> listAlbum;

    // Constructor
    AlbumListAdapter(List<Album> listAlbum) {
        this.listAlbum = listAlbum;
    }

    // This is where we create an element of the RecyclerView
    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.cell_album, viewGroup, false);
        return new AlbumViewHolder(view);
    }

    // This is where we bind the data inside of the newly created element
    @Override
    public void onBindViewHolder(AlbumViewHolder myViewHolder, int position) {
        Album album = listAlbum.get(position);
        myViewHolder.bind(album);

        // In DEBUG, we log the new bind in the console
        if (BuildConfig.DEBUG) {
            Log.i("AlbumListAdapter", "onBindViewHolder: " + album.getId());
        }

    }

    @Override
    public int getItemCount() {
        return listAlbum.size();
    }

    // We define here what to do with our ViewHolder
    class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindViews({R.id.iv_thumbnail1, R.id.iv_thumbnail2, R.id.iv_thumbnail3,
                R.id.iv_thumbnail4, R.id.iv_thumbnail5, R.id.iv_thumbnail6,
                R.id.iv_thumbnail7})
        List<ImageView> ivThumbnails;

        AlbumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            // We set the listener
            itemView.setOnClickListener(this);
        }

        void bind(Album album) {
            // We activate the Picasso flags only if we are in DEBUG
            if (BuildConfig.DEBUG) {
                for (ImageView currentIvThumbnail : ivThumbnails) {
                    Picasso.with(currentIvThumbnail.getContext()).setIndicatorsEnabled(true);
                }
            }
            int i = 0;
            List<Photo> albumPhotos = album.getPhotos();
            while (i < ivThumbnails.size()) {
                ImageView currentIvThumbnail = ivThumbnails.get(i);
                Photo photoToShow = albumPhotos.get(i);
                Picasso.with(currentIvThumbnail.getContext()).load(
                        photoToShow.getThumbnailUrl().toString())
                        .centerCrop().fit().into(currentIvThumbnail);
                i++;
            }
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
