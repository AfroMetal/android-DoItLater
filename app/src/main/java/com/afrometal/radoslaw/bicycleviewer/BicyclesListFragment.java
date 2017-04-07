package com.afrometal.radoslaw.bicycleviewer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class BicyclesListFragment extends ListFragment implements OnItemClickListener {
    // The list adapter for the list we are displaying
    MyArrayAdapter mListAdapter;

    // The listener we are to notify when a headline is selected
    OnBicycleSelectedListener mBicycleSelectedListener = null;

    public void setRating(int position, float rating) {
//        View view  = mListAdapter.getView(position, null, null);
        Log.d("setRating:", "rating: " + rating + "\t" + "position: " + position);
//        TextView tv = (TextView) view.findViewWithTag(getActivity().getResources().getString(R.string.ratingData)+position);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(getActivity().getResources().getString(R.string.ratingData)+position, rating);
        editor.commit();

        if (((MainActivity)getActivity()).mIsDualPane) {
            Activity act = getActivity();
            SharedPreferences data = act.getPreferences(Context.MODE_PRIVATE);
            mListAdapter.data = data;
//                mListAdapter = new MyArrayAdapter(act, titles, data);
//                setListAdapter(mListAdapter);
            mListAdapter.notifyDataSetChanged();
        }

//        Activity act = getActivity();
//        SharedPreferences data = act.getPreferences(Context.MODE_PRIVATE);
//        String[] titles = getResources().getStringArray(R.array.titles);
//
//        mListAdapter = new MyArrayAdapter(act, titles, data);
    }

    public interface OnBicycleSelectedListener {
        void onPhotoSelected(int position, String title, float rating);
    }

    /**
     * Default constructor required by framework.
     */
    public BicyclesListFragment() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
        setListAdapter(mListAdapter);

        getListView().setSelector(R.drawable.selector);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an array adapter for the list view, using the Ipsum headlines array
        Activity act = getActivity();
        SharedPreferences data = act.getPreferences(Context.MODE_PRIVATE);
        String[] titles = getResources().getStringArray(R.array.titles);

        mListAdapter = new MyArrayAdapter(act, titles, data);
    }


    @Override
    public void onStop() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String prefix = getActivity().getResources().getString(R.string.ratingData);

        for (int i=0; i<mListAdapter.getCount(); i++) {
            View view  = mListAdapter.getView(i, null, null);
            float rating = Float.parseFloat(((TextView)view.findViewById(R.id.secondLine)).getText().toString());
            editor.putFloat(prefix+i, rating);
        }

        editor.commit();

        super.onStop();
    }
    /**
     * Sets the listener that should be notified of headline selection events.
     * @param listener the listener to notify.
     */
    public void setOnBicycleSelectedListener(OnBicycleSelectedListener listener) {
        mBicycleSelectedListener = listener;
    }

    /**
     * Handles a click on a headline.
     *
     * This causes the configured listener to be notified that a headline was selected.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String title = ((TextView)view.findViewById(R.id.firstLine)).getText().toString();
        float rating = Float.parseFloat(((TextView)view.findViewById(R.id.secondLine)).getText().toString()); //TODO: get rating from view or memory
        if (null != mBicycleSelectedListener) {
            mBicycleSelectedListener.onPhotoSelected(position, title, rating);
        }
    }

    /** Sets choice mode for the list
     *
     * @param selectable whether list is to be selectable.
     */
    public void setSelectable(boolean selectable) {
        if (selectable) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        else {
            getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBicycleSelectedListener = null;
    }
}
