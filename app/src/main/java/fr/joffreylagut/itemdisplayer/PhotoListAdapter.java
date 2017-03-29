package fr.joffreylagut.itemdisplayer;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.joffreylagut.itemdisplayer.models.Photo;

/**
 * PhotoListAdapter.java
 * Purpose: Adapter for the RecyclerView that is showing photos.
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-27
 */

class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder> {

    // List of photos that we want to show
    private List<Photo> listPhotos;

    // Constructor
    PhotoListAdapter(List<Photo> listPhotos) {
        this.listPhotos = listPhotos;
    }

    // This is where we create an element of the RecyclerView
    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.cell_photo, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    // This is where we insert the data inside of the newly created element
    @Override
    public void onBindViewHolder(PhotoViewHolder myViewHolder, int position) {
        Photo photo = listPhotos.get(position);
        myViewHolder.bind(photo);

        // In DEBUG, we log the new bind in the console
        if (BuildConfig.DEBUG) {
            Log.i("PhotoListAdapter", "onBindViewHolder: " + photo.getId());
        }

    }

    @Override
    public int getItemCount() {
        return listPhotos.size();
    }

    // We define here what to do with our ViewHolder
    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_thumbnail)
        ImageView ivThumbnail;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            // We set the listener
            itemView.setOnClickListener(this);
        }

        void bind(Photo photo) {
            if (BuildConfig.DEBUG) {
                Picasso.with(ivThumbnail.getContext()).setIndicatorsEnabled(true);
                Picasso.with(ivThumbnail.getContext()).setLoggingEnabled(true);
            }

            Picasso.with(ivThumbnail.getContext()).load(
                    photo.getThumbnailUrl().toString()).centerCrop().fit().into(ivThumbnail);
        }

        @Override
        public void onClick(View v) {
            // When the user click on the image, we start a new ImageDialog activity
            Intent showImageIntent = new Intent(v.getContext(), ImageDialog.class);
            // We already have all the information here so we put only the one thta interest us
            // in the intent
            Photo photoClicked = listPhotos.get(getAdapterPosition());
            showImageIntent.putExtra("title", photoClicked.getTitle());
            showImageIntent.putExtra("url", photoClicked.getUrl().toString());

            // We start the new activity
            v.getContext().startActivity(showImageIntent);
        }
    }
}
