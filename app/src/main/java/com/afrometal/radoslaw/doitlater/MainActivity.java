package com.afrometal.radoslaw.doitlater;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ToDoListFragment.OnToDoItemSelectedListener {

    // Whether or not we are in dual-pane mode
    boolean mIsDualPane = false;
    ToDoDbHelper mDbHelper;

    // The fragment where the headlines are displayed
    ToDoListFragment mToDoListFragment;

    // The fragment where the article is displayed (null if absent)
    DetailsFragment mDetailsFragment;

    int mAdapterPosition;
    Long mToDoIndex = -1L;
    String mToDoTitle = "";
    String mToDoDetails = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new ToDoDbHelper(this);

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

        if (mIsDualPane) {
            mToDoListFragment.mButton.setSize(FloatingActionButton.SIZE_AUTO);
            mDetailsFragment.mSaveButton.setVisibility(View.VISIBLE);
            mDetailsFragment.mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mDetailsFragment.mTitleTextView.getText().toString();
                    String details = mDetailsFragment.mDetailsTextView.getText().toString();
                    saveData(mToDoIndex, title, details);
                }
            });
            mDetailsFragment.mDetailsTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        String title = mDetailsFragment.mTitleTextView.getText().toString();
                        String details = mDetailsFragment.mDetailsTextView.getText().toString();
                        saveData(mToDoIndex, title, details);
                    }
                    return handled;
                }
            });
        }
        // Register ourselves as the listener for the headlines fragment events.
        mToDoListFragment.setOnToDoItemSelectedListener(this);

        // Set up headlines fragment
        mToDoListFragment.setSelectable(mIsDualPane);
//        restoreSelection(savedInstanceState);
    }

    /** Restore category/article selection from saved state. */
    void restoreSelection(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mToDoIndex = savedInstanceState.getLong("index", -1L);
            mToDoTitle = savedInstanceState.getString("title", "");
            mToDoDetails = savedInstanceState.getString("details", "");
            mAdapterPosition = savedInstanceState.getInt("position");

            if (mIsDualPane) {
                // display it on the article fragment
                mDetailsFragment.updateDetailsView(mToDoTitle, mToDoDetails);
            } else {
                // use separate activity
                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra("itemIndex", mToDoIndex);
                intent.putExtra("itemTitle", mToDoTitle);
                intent.putExtra("itemDetails", mToDoDetails);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        restoreSelection(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mIsDualPane) {
            mToDoTitle = mDetailsFragment.mTitleTextView.getText().toString();
            mToDoDetails = mDetailsFragment.mDetailsTextView.getText().toString();
        }
        outState.putLong("index", mToDoIndex);
        outState.putString("title", mToDoTitle);
        outState.putString("details", mToDoDetails);
        outState.putInt("position", mAdapterPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onToDoItemSelected(Long index, int position) {
        mAdapterPosition = position;
        mToDoIndex = index;
        mToDoTitle = "";
        mToDoDetails = "";
        if (index >= 0) {
            String[] projection = {
                    ToDoContract.ToDoEntry._ID,
                    ToDoContract.ToDoEntry.COLUMN_NAME_TITLE,
                    ToDoContract.ToDoEntry.COLUMN_NAME_DETAILS
            };

            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            // Filter results WHERE "id" = index
            String selection = ToDoContract.ToDoEntry._ID + " = ?";
            String[] selectionArgs = { index.toString() };

            Cursor cursor = db.query(
                    ToDoContract.ToDoEntry.TABLE_NAME,        // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                      // The sort order
            );

            cursor.moveToNext();

            mToDoTitle = cursor.getString(
                    cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_NAME_TITLE));
            mToDoDetails = cursor.getString(
                    cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_NAME_DETAILS));

            cursor.close();
        }
        if (mIsDualPane) {
            // display it on the article fragment
            mDetailsFragment.updateDetailsView(mToDoTitle, mToDoDetails);
        } else {
            // use separate activity
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("itemIndex", mToDoIndex);
            intent.putExtra("itemTitle", mToDoTitle);
            intent.putExtra("itemDetails", mToDoDetails);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Long index = data.getLongExtra("itemIndex", -2);
                String title = data.getStringExtra("itemTitle");
                String details = data.getStringExtra("itemDetails");

                saveData(index, title, details);
            }
        }
    }

    public void saveData(Long index, String title, String details) {
        if (title.isEmpty() && details.isEmpty()) {
            return;
        } else if (title.isEmpty()) {
            title = details.split(" ", 2)[0];
        }

        if (index == -1) {
            // Gets the data repository in write mode
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            long time = System.currentTimeMillis();
            ContentValues values = new ContentValues(3);
            values.put(ToDoContract.ToDoEntry.COLUMN_NAME_DATE, time);
            values.put(ToDoContract.ToDoEntry.COLUMN_NAME_TITLE, title);
            values.put(ToDoContract.ToDoEntry.COLUMN_NAME_DETAILS, details);

            // Insert the new row, returning the primary key value of the new row
            mToDoIndex = db.insert(ToDoContract.ToDoEntry.TABLE_NAME, null, values);
            mToDoTitle = title;
            mToDoDetails = details;

            mToDoListFragment.mListAdapter.addView(mToDoIndex, mToDoTitle, ((Long) time).toString());
        } else if (index >= 0) {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            // New value for one column
            long time = System.currentTimeMillis();
            ContentValues values = new ContentValues(3);
            values.put(ToDoContract.ToDoEntry.COLUMN_NAME_DATE, time);
            values.put(ToDoContract.ToDoEntry.COLUMN_NAME_TITLE, title);
            values.put(ToDoContract.ToDoEntry.COLUMN_NAME_DETAILS, details);

            // Which row to update, based on the title
            String selection = ToDoContract.ToDoEntry._ID + " = ?";
            String[] selectionArgs = { index.toString() };

            int count = db.update(
                    ToDoContract.ToDoEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            mToDoIndex = index;
            mToDoTitle = title;
            mToDoDetails = details;

            if (count > 0) {
                mToDoListFragment.mListAdapter.editView(mToDoIndex, mToDoTitle, ((Long) time).toString(), mAdapterPosition);
            }
        } else {
            return;
        }

        Toast.makeText(this, "Note '" + title + "' saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
