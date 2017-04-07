package com.afrometal.radoslaw.bicycleviewer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RatingBar;

/**
 * Created by radoslaw on 06.04.17.
 */

public class PhotoActivity extends AppCompatActivity {
    // The fragment where the article is displayed (null if absent)
    private PhotoFragment mPhotoFragment;
    // The news category index and the article index for the article we are to display
    private int mPhotoIndex;
    private String mPhotoTitle;
    private float mPhotoRating;

    /**
     * Sets up the activity.
     *
     * Setting up the activity means reading the category/article index from the Intent that
     * fired this Activity and loading it onto the UI. We also detect if there has been a
     * screen configuration change (in particular, a rotation) that makes this activity
     * unnecessary, in which case we do the honorable thing and get out of the way.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getBoolean(R.bool.has_two_panes)) {
            finish();
            return;
        }

        setContentView(R.layout.one_pane_photo);

        Intent intent = getIntent();
        Bundle args = intent.getExtras();

        mPhotoIndex = args.getInt("photoIndex", -1);
        mPhotoTitle = args.getString("photoTitle", "");
        mPhotoRating = args.getFloat("photoRating", 0);

        mPhotoFragment = (PhotoFragment) getSupportFragmentManager().findFragmentById(
                R.id.photo_fragment);
        // Place an ArticleFragment as our content pane
//        PhotoFragment f = new PhotoFragment();
//        getSupportFragmentManager().beginTransaction().add(android.R.id.content, f).commit();

        // Display the correct news article on the fragment

        mPhotoFragment.updatePhotoView(mPhotoIndex, mPhotoTitle, mPhotoRating);
        RatingBar mRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mPhotoRating = rating;
            }
        });
    }

    @Override
    protected void onStop() {
        sendResult();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        sendResult();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            sendResult();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void sendResult() {
        Intent intent = new Intent((String) null);
        intent.putExtra("position", mPhotoIndex);
        intent.putExtra("rating", mPhotoRating);
        setResult(RESULT_OK, intent);
        finish();
    }
}
