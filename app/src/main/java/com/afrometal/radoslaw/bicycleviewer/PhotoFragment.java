package com.afrometal.radoslaw.bicycleviewer;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

public class PhotoFragment extends Fragment {
    private TouchImageView mPhotoView;
    private RatingBar mRatingBar;
    public PhotoFragment() {
        super();
    }

    /**
     * Sets up the UI. It consists if a single WebView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mPhotoView = new TouchImageView(getActivity());
//        updatePhotoView(0);
//        mPhotoView = new ImageView(getActivity());
        View mView = inflater.inflate(R.layout.photo_view, container, false);
        mPhotoView = (TouchImageView) mView.findViewById(R.id.imageview_photo);
        mRatingBar = (RatingBar) mView.findViewById(R.id.rating_bar);

        return mView;
    }

    public void updatePhotoView(int photo, String title, float rating) {
        TypedArray imgs = getResources().obtainTypedArray(R.array.photos);
        int photoId = imgs.getResourceId(photo, -1);
        imgs.recycle();
        if (mPhotoView != null && photoId != -1) {
            mPhotoView.setImageResource(photoId);
            getActivity().setTitle(title);
            mRatingBar.setRating(rating);
            mRatingBar.setTag(photo);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
