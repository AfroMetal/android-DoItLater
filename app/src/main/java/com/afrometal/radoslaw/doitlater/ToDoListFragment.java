package com.afrometal.radoslaw.doitlater;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ToDoListFragment extends ListFragment implements OnItemClickListener, AdapterView.OnItemLongClickListener {
    // The list adapter for the list we are displaying
    MyArrayAdapter mListAdapter;
    ListView mListView;
    FloatingActionButton mButton;

    // The listener we are to notify when a headline is selected
    OnToDoItemSelectedListener mToDoItemSelectedListener = null;

    public interface OnToDoItemSelectedListener {
        void onToDoItemSelected(Long index, @Nullable int position);
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
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.list_view, container, false);
        mListView = (ListView) mView.findViewById(android.R.id.list);
        mButton = (FloatingActionButton) mView.findViewById(R.id.add_fab);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onToDoItemSelected(-1l, -1);
            }
        });

        mListView.setAdapter(mListAdapter);
        return mView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an array adapter for the list view, using the Ipsum headlines array
        MainActivity act = (MainActivity) getActivity();

        mListAdapter = new MyArrayAdapter(act, act.mDbHelper.selectAll());
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
            mToDoItemSelectedListener.onToDoItemSelected((Long) view.getTag(), position);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("long", "enter");
        Long index = (Long) view.getTag();

        ((MainActivity) getActivity()).mDbHelper.delete(index);

        mListAdapter.removeView(position);

        Toast.makeText(getActivity(), "'" + ((TextView) view.findViewById(R.id.firstLine)).getText().toString() + "' was deleted", Toast.LENGTH_SHORT).show();

        return true;
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
