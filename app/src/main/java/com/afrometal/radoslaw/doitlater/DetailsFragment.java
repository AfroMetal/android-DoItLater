package com.afrometal.radoslaw.doitlater;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
    protected EditText mTitleTextView;
    protected EditText mDetailsTextView;
    protected Button mSaveButton;
    public DetailsFragment() {
        super();
    }

    /**
     * Sets up the UI. It consists if a single WebView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.details_view, container, false);
        mTitleTextView = (EditText)  mView.findViewById(R.id.title_textview);
        mDetailsTextView = (EditText) mView.findViewById(R.id.details_textview);
        mSaveButton = (Button) mView.findViewById(R.id.save_button);

        return mView;
    }

    public void updateDetailsView(String title, String details) {
        mTitleTextView.setText(title);
        mDetailsTextView.setText(details);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
