package com.afrometal.radoslaw.doitlater;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by radoslaw on 30.04.17.
 */

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
    Long mToDoDueDate = System.currentTimeMillis();

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
                    Long due = (Long) mDetailsFragment.mDueButton.getTag();
                    String details = mDetailsFragment.mDetailsTextView.getText().toString();

                    if (!due.equals(mToDoDueDate) || !title.equals(mToDoTitle) || !details.equals(mToDoDetails)) {
                        saveData(mToDoIndex, title, details, due);
                    }
                }
            });
            mDetailsFragment.mDetailsTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        String title = mDetailsFragment.mTitleTextView.getText().toString();
                        Long due = (Long) mDetailsFragment.mDueButton.getTag();
                        String details = mDetailsFragment.mDetailsTextView.getText().toString();
                        saveData(mToDoIndex, title, details, due);
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
            mToDoDueDate = savedInstanceState.getLong("due", System.currentTimeMillis());
            mToDoDetails = savedInstanceState.getString("details", "");
            mAdapterPosition = savedInstanceState.getInt("position");

            if (!mToDoTitle.isEmpty() || !mToDoDetails.isEmpty()) {
                if (mIsDualPane) {
                    // display it on the article fragment
                    mDetailsFragment.updateDetailsView(mToDoTitle, mToDoDueDate, mToDoDetails);
                } else {
                    // use separate activity
                    Intent intent = new Intent(this, DetailsActivity.class);
                    intent.putExtra("itemIndex", mToDoIndex);
                    intent.putExtra("itemTitle", mToDoTitle);
                    intent.putExtra("itemDueDate", mToDoDueDate);
                    intent.putExtra("itemDetails", mToDoDetails);
                    intent.putExtra("restored", true);
                    startActivityForResult(intent, 1);
                }
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
            mToDoDueDate = (Long) mDetailsFragment.mDueButton.getTag();
            mToDoDetails = mDetailsFragment.mDetailsTextView.getText().toString();
        }
        outState.putLong("index", mToDoIndex);
        outState.putString("title", mToDoTitle);
        outState.putLong("due", mToDoDueDate);
        outState.putString("details", mToDoDetails);
        outState.putInt("position", mAdapterPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onToDoItemSelected(Long index, int position) {
        mAdapterPosition = position;
        mToDoIndex = index;
        mToDoDueDate = System.currentTimeMillis();
        mToDoTitle = "";
        mToDoDetails = "";
        if (index >= 0) {
            ToDoDetailsItem item = mDbHelper.selectDetails(index);

            mToDoTitle = item.title;
            mToDoDueDate = item.due;
            mToDoDetails = item.details;
        }
        if (mIsDualPane) {
            // display it on the article fragment
            mDetailsFragment.updateDetailsView(mToDoTitle, mToDoDueDate, mToDoDetails);
        } else {
            // use separate activity
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("itemIndex", mToDoIndex);
            intent.putExtra("itemTitle", mToDoTitle);
            intent.putExtra("itemDueDate", mToDoDueDate);
            intent.putExtra("itemDetails", mToDoDetails);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK || resultCode == RESULT_FIRST_USER + 0) {
                mToDoIndex = data.getLongExtra("itemIndex", -2);
                mToDoTitle = data.getStringExtra("itemTitle");
                mToDoTitle = mToDoTitle == null ? "" : mToDoTitle;
                mToDoDueDate = data.getLongExtra("itemDueDate", System.currentTimeMillis());
                mToDoDetails = data.getStringExtra("itemDetails");
                mToDoDetails = mToDoDetails == null ? "" : mToDoDetails;

                if (resultCode == RESULT_OK) {
                    saveData(mToDoIndex, mToDoTitle, mToDoDetails, mToDoDueDate);
                } else {
                    mDetailsFragment.updateDetailsView(mToDoTitle, mToDoDueDate, mToDoDetails);
                }
            }
        }
    }

    public void saveData(Long index, String title, String details, Long due) {
        if (title.isEmpty() && details.isEmpty()) {
            return;
        } else if (title.isEmpty()) {
            title = details.split(" ", 2)[0];
        }
        if (index == -1) {
            mToDoIndex = mDbHelper.insert(title, details, due);
            mToDoTitle = title;
            mToDoDueDate = due;
            mToDoDetails = details;

            mToDoListFragment.mListAdapter.addView(mToDoIndex, mToDoTitle, ((Long) System.currentTimeMillis()).toString(), due.toString());
            Toast.makeText(this, "Note '" + title + "' saved", Toast.LENGTH_SHORT).show();
        } else if (index >= 0) {
            int count = mDbHelper.update(index, title, details, due);

            mToDoIndex = index;
            mToDoTitle = title;
            mToDoDueDate = due;
            mToDoDetails = details;

            if (count > 0) {
                mToDoListFragment.mListAdapter.editView(mToDoIndex, mToDoTitle, ((Long) System.currentTimeMillis()).toString(), due.toString(), mAdapterPosition);
                Toast.makeText(this, "Note '" + title + "' edited", Toast.LENGTH_SHORT).show();
            }
        } else {
            mDetailsFragment.updateDetailsView("", System.currentTimeMillis(), "");
            return;
        }

        if (mIsDualPane) {
            onToDoItemSelected(-1L, -1);
        }
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
