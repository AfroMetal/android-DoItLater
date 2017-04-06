package com.afrometal.radoslaw.bicycleviewer;


import android.app.ListFragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class BicyclesListFragment extends ListFragment {

    OnBicycleSelectedListener mCallback;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnBicycleSelectedListener {
        void onPhotoSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an array adapter for the list view, using the Ipsum headlines array
        setListAdapter(new MyArrayAdapter(getActivity(), getResources().getStringArray(R.array.titles)));
    }

    @Override
    public void onStart() {
        super.onStart();
        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        } else {
            getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnBicycleSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onPhotoSelected(position);

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}
