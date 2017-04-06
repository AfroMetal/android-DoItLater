package com.afrometal.radoslaw.bicycleviewer;

import android.content.res.TypedArray;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PhotoFragment extends Fragment {
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    TouchImageView photo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION, -1);
        }
        View v = inflater.inflate(R.layout.photo_view, container, false);
        photo = (TouchImageView) v.findViewById(R.id.imageview_photo);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            updatePhotoView(savedInstanceState.getInt(ARG_POSITION, -1));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updatePhotoView(args.getInt(ARG_POSITION, -1));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updatePhotoView(mCurrentPosition);
        }
    }

    public void updatePhotoView(int position) {
//        ImageView photo = (ImageView) getView().findViewById(R.id.imageview_photo);
        if (position != -1) {
            TypedArray imgs = getResources().obtainTypedArray(R.array.photos);
            photo.setImageResource(imgs.getResourceId(position, -1));
            imgs.recycle();
            mCurrentPosition = position;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
}
