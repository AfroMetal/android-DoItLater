package com.afrometal.radoslaw.doitlater;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by radoslaw on 30.04.17.
 */

public class DatePickerFragment extends android.support.v4.app.DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    DetailsFragment detailsFragment;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        detailsFragment = (DetailsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.details_fragment);
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis((Long) detailsFragment.mDueButton.getTag());

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        detailsFragment.updateDueDate(c.getTimeInMillis());
    }
}
