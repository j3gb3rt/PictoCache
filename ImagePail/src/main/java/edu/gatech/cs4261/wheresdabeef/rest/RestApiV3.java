package edu.gatech.cs4261.wheresdabeef.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.cs4261.wheresdabeef.domain.Image;

/**
 * Created by HolocronCoder.
 */
public class RestApiV3 extends AsyncTask<RestData, Void, Boolean> {
    private Context context;

    public RestApiV3(final Context c) {
        this.context = c;
    }

    @Override
    protected Boolean doInBackground(RestData... restDatas) {
        RestData data = restDatas[0];
        RestData.RestAction action = data.getAction();
        List<NameValuePair> params = data.getParams();
        Map<String, String> paramsMap = new HashMap<String, String>();
        for (int i = 0; i < params.size(); i++) {
            paramsMap.put(params.get(i).getName(), params.get(i).getValue());
        }

        try {
            RestApiInterface rai = new RestApiInterface();
            switch (action) {
                case GET_IMAGE:
                    break;
                case GET_IMAGE_DATA:
                    break;
                case GET_IMAGES:
                    break;
                case GET_POPULAR_KEYWORDS:
                    break;
                case POST_IMAGE:
                    Image i = new Image(-1);
                    i.setLongitude(Double.valueOf(paramsMap.get("lon")));
                    i.setLatitude(Double.valueOf(paramsMap.get("lat")));
                    i.setImage(data.getImage());
                    i.setThumbnail(data.getThumb());
                    rai.saveImage(i);
                    break;
                case POST_KEYWORD:
                    break;
            }
        } catch (final IOException e) {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBool) {
        super.onPostExecute(aBool);

        if (aBool) {
            Toast.makeText(context, "Upload Successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Upload Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
