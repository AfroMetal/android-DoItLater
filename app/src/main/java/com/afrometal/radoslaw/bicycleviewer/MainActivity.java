package com.afrometal.radoslaw.bicycleviewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;

public class MainActivity extends AppCompatActivity implements BicyclesListFragment.OnBicycleSelectedListener {

    // Whether or not we are in dual-pane mode
    boolean mIsDualPane = false;

    // The fragment where the headlines are displayed
    BicyclesListFragment mBicyclesListFragment;

    // The fragment where the article is displayed (null if absent)
    PhotoFragment mPhotoFragment;

    int mPhotoIndex = 0;
    String mPhotoTitle = "";
    float mPhotoRating = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // find our fragments
        mBicyclesListFragment = (BicyclesListFragment) getSupportFragmentManager().findFragmentById(
                R.id.list_fragment);
        mPhotoFragment = (PhotoFragment) getSupportFragmentManager().findFragmentById(
                R.id.photo_fragment);

        // Determine whether we are in single-pane or dual-pane mode by testing the visibility
        // of the article view.
        View photoView = findViewById(R.id.photo_fragment);
        mIsDualPane = photoView != null && photoView.getVisibility() == View.VISIBLE;

        // Register ourselves as the listener for the headlines fragment events.
        mBicyclesListFragment.setOnBicycleSelectedListener(this);

        // Set up headlines fragment
        mBicyclesListFragment.setSelectable(mIsDualPane);
//        restoreSelection(savedInstanceState);
    }

    /** Restore category/article selection from saved state. */
    void restoreSelection(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPhotoIndex = savedInstanceState.getInt("photoIndex", -1);
            mPhotoTitle = savedInstanceState.getString("photoTitle", "");
            mPhotoRating = savedInstanceState.getFloat("photoRating", 0);
            if (mIsDualPane) {
                mBicyclesListFragment.setSelection(mPhotoIndex);
            }
            onPhotoSelected(mPhotoIndex, mPhotoTitle, mPhotoRating);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        restoreSelection(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("photoIndex", mPhotoIndex);
        outState.putString("photoTitle", mPhotoTitle);
        outState.putFloat("photoRating", mPhotoRating);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPhotoSelected(int index, String title, float rating) {
        mPhotoIndex = index;
        mPhotoTitle = title;
        mPhotoRating = rating;
        if (mIsDualPane) {
            // display it on the article fragment
            mPhotoFragment.updatePhotoView(index, title, rating);
            ((RatingBar) findViewById(R.id.rating_bar)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    setListRating((int) ratingBar.getTag(), rating);
                    mPhotoRating = rating;
                }
            });
        } else {
            // use separate activity
            Intent i = new Intent(this, PhotoActivity.class);
            i.putExtra("photoIndex", index);
            i.putExtra("photoTitle", title);
            i.putExtra("photoRating", rating);
            startActivityForResult(i, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", -1);
                float rating = data.getFloatExtra("rating", 0.0f);
                if (position >= 0) {
                    setListRating(position, rating);
                    mPhotoRating = rating;
                }
            }
        }
    }

    protected void setListRating(int position, float rating) {
        mBicyclesListFragment.setRating(position, rating);
    }
}
