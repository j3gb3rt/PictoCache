package edu.gatech.cs4261.wheresdabeef;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import edu.gatech.cs4261.wheresdabeef.camera.CameraManager;
import edu.gatech.cs4261.wheresdabeef.domain.Image;
import edu.gatech.cs4261.wheresdabeef.location.LocationApi;


public class Home_feed extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(boolean predefined, String keyword) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(predefined, keyword))
                .commit();
    }

    public void onSectionAttached(String keyword) {
        if (keyword != null) {
            mTitle = keyword;
        }
        else {
            mTitle = "PictoCache";
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home_feed, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            //SearchManager searchManager =
            //        	         (SearchManager)getSystemService(Context.SEARCH_SERVICE);
            //SearchableInfo info =
            //        	         searchManager.getSearchableInfo(getComponentName());
            //searchView.setSearchableInfo(info);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_camera:
                openCamera();

            //case R.id.action_search:
                //MenuItemCompat.expandActionView(item);
                //Toast.makeText(this , "Example action.", Toast.LENGTH_SHORT).show();
                //return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = CameraManager.getOutputMediaFileUri(this); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getExternalCacheDir().getAbsolutePath() + "/temp"); // set the image file name

        LocationApi.startPollingLocation(this);
        // start the image capture Intent
        this.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Bitmap image = BitmapFactory.decodeFile(getExternalCacheDir().getAbsolutePath() + "/temp");
                Location location = LocationApi.stopPollingLocation();
                Image takenPicture = new Image(image, location);
                Intent intent = new Intent(this, Single_image.class);
                intent.putExtra("image", takenPicture);
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                //image = null;
                //location = null;
            } else {
                //image = null;
                //location = null;
            }
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_PREDEFINED_SECTION = "predefined section";
        private static final String ARG_KEYWORD = "keyword";
        private static boolean mPredefined;
        private static String mKeyword;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(boolean predefined, String keyword) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putBoolean(ARG_PREDEFINED_SECTION, predefined);
            args.putCharSequence(ARG_KEYWORD, keyword);
            fragment.setArguments(args);
            mPredefined = predefined;
            mKeyword = keyword;
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.picture_grid_main, container, false);

            GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
            final ImageAdapter imageAdapter = new ImageAdapter(getActivity(), mPredefined, mKeyword);
            gridview.setAdapter(imageAdapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Intent intent = new Intent(getActivity(), Single_image.class);
                    intent.putExtra("image", imageAdapter.getImageId(position));
                    startActivity(intent);
                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Home_feed) activity).onSectionAttached(
                    getArguments().getString(ARG_KEYWORD));
        }
    }

}
