package com.afrometal.radoslaw.bicycleviewer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements BicyclesListFragment.OnBicycleSelectedListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                getFragmentManager().executePendingTransactions();
                Fragment fragmentById = getFragmentManager().
                        findFragmentById(R.id.photo_fragment);
                if (fragmentById!=null) {
                    getFragmentManager().beginTransaction()
                            .remove(fragmentById).commit();
                }
                return;
            }

            // Create an instance of ExampleFragment
            BicyclesListFragment firstFragment = new BicyclesListFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void onPhotoSelected(int position) {
        PhotoFragment photoFlag = (PhotoFragment)
                getFragmentManager().findFragmentById(R.id.photo_fragment);

        if (photoFlag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ArticleFragment to update its content
            photoFlag.updatePhotoView(position);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            PhotoFragment newFragment = new PhotoFragment();
            Bundle args = new Bundle();
            args.putInt(PhotoFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }
}
