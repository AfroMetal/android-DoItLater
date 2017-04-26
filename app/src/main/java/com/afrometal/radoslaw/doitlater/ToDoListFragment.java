package com.afrometal.radoslaw.doitlater;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class ToDoListFragment extends ListFragment implements OnItemClickListener {
    // The list adapter for the list we are displaying
    MyArrayAdapter mListAdapter;
    ListView mListView;

    // The listener we are to notify when a headline is selected
    OnToDoItemSelectedListener mToDoItemSelectedListener = null;

    public interface OnToDoItemSelectedListener {
        void onToDoItemSelected(int position);
    }

    /**
     * Default constructor required by framework.
     */
    public ToDoListFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.list_view, container, false);
        mListView = (ListView) mView.findViewById(android.R.id.list);

        mListView.setAdapter(mListAdapter);
        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an array adapter for the list view, using the Ipsum headlines array
        Activity act = getActivity();
        //TODO: get titles from database, with first liners
        String[] titles = {"Title1", "Title2", "Title3", "Title1", "Title2", "Title3", "Title1", "Title2", "Title3", "Title1", "Title2", "Title3"};
        String[] shorts = {"Lorem ipsum dolor", "Ala ma kota a kot ma ale, ale kot nie ma myszy", "Praise the lord!",
                "Lorem ipsum dolor", "Ala ma kota a kot ma ale, ale kot nie ma myszy", "Praise the lord!",
                "Lorem ipsum dolor", "Ala ma kota a kot ma ale, ale kot nie ma myszy", "Praise the lord!",
                "Lorem ipsum dolor", "Ala ma kota a kot ma ale, ale kot nie ma myszy", "Praise the lord!"};

        mListAdapter = new MyArrayAdapter(act, titles, shorts);
    }

    /**
     * Sets the listener that should be notified of headline selection events.
     * @param listener the listener to notify.
     */
    public void setOnToDoItemSelectedListener(OnToDoItemSelectedListener listener) {
        mToDoItemSelectedListener = listener;
    }

    /**
     * Handles a click on a headline.
     *
     * This causes the configured listener to be notified that a headline was selected.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mToDoItemSelectedListener) {
            mToDoItemSelectedListener.onToDoItemSelected(position);
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
        mToDoItemSelectedListener = null;
    }
}
