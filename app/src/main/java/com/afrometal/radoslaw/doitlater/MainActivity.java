package com.afrometal.radoslaw.doitlater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;

public class MainActivity extends AppCompatActivity implements ToDoListFragment.OnToDoItemSelectedListener {

    // Whether or not we are in dual-pane mode
    boolean mIsDualPane = false;

    // The fragment where the headlines are displayed
    ToDoListFragment mToDoListFragment;

    // The fragment where the article is displayed (null if absent)
    DetailsFragment mDetailsFragment;

    int mToDoIndex = 0;
    String mToDoTitle = "";
    String mToDoDetails = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // find our fragments
        mToDoListFragment = (ToDoListFragment) getSupportFragmentManager().findFragmentById(
                R.id.list_fragment);
        mDetailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(
                R.id.details_fragment);

        // Determine whether we are in single-pane or dual-pane mode by testing the visibility
        // of the article view.
        View detailsView = findViewById(R.id.details_fragment);
        mIsDualPane = detailsView != null && detailsView.getVisibility() == View.VISIBLE;

        // Register ourselves as the listener for the headlines fragment events.
        mToDoListFragment.setOnToDoItemSelectedListener(this);

        // Set up headlines fragment
        mToDoListFragment.setSelectable(mIsDualPane);
//        restoreSelection(savedInstanceState);
    }

    /** Restore category/article selection from saved state. */
    void restoreSelection(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //TODO: restore saved instance
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        restoreSelection(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //TODO: save instance
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onToDoItemSelected(int index) {
        mToDoIndex = index;
        mToDoTitle = ""; //TODO:  get title from database
        mToDoDetails = ""; // TODO: get details from database
        if (mIsDualPane) {
            // display it on the article fragment
            mDetailsFragment.updateDetailsView(mToDoIndex, mToDoTitle, mToDoDetails);
        } else {
            // use separate activity
            Intent i = new Intent(this, DetailsActivity.class);
            //TODO: put extras
            startActivityForResult(i, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //TODO: handle response data
            }
        }
    }
}
