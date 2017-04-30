package com.afrometal.radoslaw.doitlater;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by radoslaw on 30.04.17.
 */

public class DetailsFragment extends Fragment {
    protected EditText mTitleTextView;
    protected EditText mDetailsTextView;
    protected Button mDueButton;
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
        mDueButton = (Button) mView.findViewById(R.id.due_button);
        mDueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });
        updateDueDate(System.currentTimeMillis());
        mSaveButton = (Button) mView.findViewById(R.id.save_button);

        return mView;
    }

    public void updateDetailsView(String title, Long due, String details) {
        mTitleTextView.setText(title);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(due);
        mDueButton.setText(sdf.format(calendar.getTime()));
        mDueButton.setTag(due);
        mDetailsTextView.setText(details);
    }

    public void updateDueDate(Long due) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(due);
        mDueButton.setText(sdf.format(calendar.getTime()));
        mDueButton.setTag(calendar.getTimeInMillis());
    }
}
