package com.afrometal.radoslaw.doitlater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
    private TextView mDetailsTextView;
    public DetailsFragment() {
        super();
    }

    /**
     * Sets up the UI. It consists if a single WebView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mDetailsTextView = new TouchImageView(getActivity());
//        updateDetailsView(0);
//        mDetailsTextView = new ImageView(getActivity());
        View mView = inflater.inflate(R.layout.details_view, container, false);
        mDetailsTextView = (TextView) mView.findViewById(R.id.details_textview);

        return mView;
    }

    public void updateDetailsView(int photo, String title, String details) {
        //TODO: fckin update it
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
