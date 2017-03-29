package fr.joffreylagut.itemdisplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ImageDialog.java
 * Purpose: Activity called when we want to display an image.
 * This activity have a special theme that make it displayed like a popup.
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-28
 */

public class ImageDialog extends AppCompatActivity {

    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.iv_close)
    ImageView mIvExit;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dialog);

        // We bind the views
        ButterKnife.bind(this);

        // We retrieve the information in the intent
        Intent openingIntent = getIntent();
        String title = openingIntent.getStringExtra("title");
        String url = openingIntent.getStringExtra("url");

        // We assign the values in views
        if (BuildConfig.DEBUG) {
            Picasso.with(mIvPhoto.getContext()).setIndicatorsEnabled(true);
            Picasso.with(mIvPhoto.getContext()).setLoggingEnabled(true);
        }
        Picasso.with(mIvPhoto.getContext()).load(url).centerCrop().fit().into(mIvPhoto);
        mTvTitle.setText(title);


        // We set the exit ImageView clickable and set a new onClickListener
        mIvExit.setClickable(true);
        mIvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the user click on the cross, we finish the activity
                finish();
            }
        });
    }
}
