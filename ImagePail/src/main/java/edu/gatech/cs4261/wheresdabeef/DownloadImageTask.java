package edu.gatech.cs4261.wheresdabeef;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;

import edu.gatech.cs4261.wheresdabeef.domain.Image;
import edu.gatech.cs4261.wheresdabeef.rest.RestApiInterface;

/**
 * Created by jonathan on 11/4/13.
 */
public class DownloadImageTask extends AsyncTask<ArrayList<Image>,Integer,Boolean>{
    Uri imageLocation;

    @Override
    protected Boolean doInBackground(ArrayList<Image>... params) {
        ArrayList<Image> images = params[0];
        for (int i = 0; i < images.size(); i++) {
            RestApiInterface rai = new RestApiInterface();
            try {
                imageLocation = rai.getImageData(images.get(i).getId());
                publishProgress(i);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Uri imageLoc = imageLocation;
        int position = values[0];
        Home_feed.getImageAdapter().setImageUri(position, imageLocation);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

    }

}
