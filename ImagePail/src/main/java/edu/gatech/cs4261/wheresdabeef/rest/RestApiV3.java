package edu.gatech.cs4261.wheresdabeef.rest;

import android.content.Context;
import android.net.Uri;
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

    private Image image;
    private int newImageId;
    private int newKeywordId;
    private Uri imageLoc;
    private List<Image> images;
    private Map<String, Integer> popularKeywords;

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
            int id;
            switch (action) {
                case GET_IMAGE:
                    id = Integer.valueOf(paramsMap.get("id")).intValue();
                    this.image = rai.getImage(id);
                    break;
                case GET_IMAGE_DATA:
                    id = Integer.valueOf(paramsMap.get("id")).intValue();
                    this.imageLoc = rai.getImageData(id);
                    break;
                case GET_IMAGES:
                    String sd = null, sc = null, k = null;
                    Integer l = null;
                    Double minLat = null, maxLat = null, minLon = null, maxLon = null;

                    if (paramsMap.containsKey("sd")) {
                        sd = paramsMap.get("sd");
                    }
                    if (paramsMap.containsKey("sc")) {
                        sc = paramsMap.get("sc");
                    }
                    if (paramsMap.containsKey("k")) {
                        k = paramsMap.get("k");
                    }
                    if (paramsMap.containsKey("l")) {
                        l = Integer.valueOf(paramsMap.get("l"));
                    }
                    if (paramsMap.containsKey("minLat")) {
                        minLat = Double.valueOf(paramsMap.get("minLat"));
                    }
                    if (paramsMap.containsKey("maxLat")) {
                        minLat = Double.valueOf(paramsMap.get("maxLat"));
                    }
                    if (paramsMap.containsKey("minLon")) {
                        minLat = Double.valueOf(paramsMap.get("minLon"));
                    }
                    if (paramsMap.containsKey("maxLon")) {
                        minLat = Double.valueOf(paramsMap.get("maxLon"));
                    }

                    this.images = rai.getImages(sd, sc, l, k, minLat, maxLat, minLon, maxLon);
                    break;
                case GET_POPULAR_KEYWORDS:
                    Integer limit = null;

                    if (paramsMap.containsKey("l")) {
                        limit = Integer.valueOf(paramsMap.get("l"));
                    }

                    this.popularKeywords = rai.getPopularKeywords(limit);
                    break;
                case POST_IMAGE:
                    Image i = new Image(-1);
                    i.setLongitude(Double.valueOf(paramsMap.get("lon")));
                    i.setLatitude(Double.valueOf(paramsMap.get("lat")));
                    i.setImage(data.getImage());
                    i.setThumbnail(data.getThumb());
                    this.newImageId = rai.saveImage(i);
                    break;
                case POST_KEYWORD:
                    String kw = paramsMap.get("kw");
                    int imgId = Integer.valueOf(paramsMap.get("imgId")).intValue();
                    this.newKeywordId = rai.saveKeyword(kw, imgId);
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
