package com.afrometal.radoslaw.doitlater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by radoslaw on 06.04.17.
 */

public class DetailsActivity extends AppCompatActivity {
    // The fragment where the article is displayed (null if absent)
    private DetailsFragment mDetailsFragment;
    // The news category index and the article index for the article we are to display
    private Long mToDoIndex;
    private String mTitle;
    private String mDetails;

    /**
     * Sets up the activity.
     *
     * Setting up the activity means reading the category/article index from the Intent that
     * fired this Activity and loading it onto the UI. We also detect if there has been a
     * screen configuration change (in particular, a rotation) that makes this activity
     * unnecessary, in which case we do the honorable thing and get out of the way.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_pane_details);

        if (getResources().getBoolean(R.bool.has_two_panes)) {
            finish();
            return;
        }

        Intent intent = getIntent();
        Bundle args = intent.getExtras();

        mToDoIndex = args.getLong("itemIndex");
        mTitle = args.getString("itemTitle");
        mDetails = args.getString("itemDetails");

        mDetailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(
                R.id.details_fragment);

        mDetailsFragment.mDetailsTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendResult();
                }
                return handled;
            }
        });

        mDetailsFragment.updateDetailsView(mTitle, mDetails);
    }

    @Override
    protected void onStop() {
        sendResult();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        sendResult();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            sendResult();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void sendResult() {
        Intent intent = new Intent((String) null);
        intent.putExtra("itemIndex", mToDoIndex);
        intent.putExtra("itemTitle", mDetailsFragment.mTitleTextView.getText().toString());
        intent.putExtra("itemDetails", mDetailsFragment.mDetailsTextView.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
