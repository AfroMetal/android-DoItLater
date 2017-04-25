package com.afrometal.radoslaw.doitlater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RatingBar;

/**
 * Created by radoslaw on 06.04.17.
 */

public class DetailsActivity extends AppCompatActivity {
    // The fragment where the article is displayed (null if absent)
    private DetailsFragment mDetailsFragment;
    // The news category index and the article index for the article we are to display
    private int mToDoIndex;
    private String mToDoTitle;
    private String mToDoDetails;

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

        if (getResources().getBoolean(R.bool.has_two_panes)) {
            finish();
            return;
        }

        setContentView(R.layout.one_pane_details);

        Intent intent = getIntent();
        Bundle args = intent.getExtras();

        //TODO: save args to fields

        mDetailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentById(
                R.id.details_fragment);
        // Place an ArticleFragment as our content pane
        DetailsFragment f = new DetailsFragment();
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, f).commit();

        // Display the correct news article on the fragment

        mDetailsFragment.updateDetailsView(mToDoIndex, mToDoTitle, mToDoDetails);
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
        //TODO: put data
        setResult(RESULT_OK, intent);
        finish();
    }
}
