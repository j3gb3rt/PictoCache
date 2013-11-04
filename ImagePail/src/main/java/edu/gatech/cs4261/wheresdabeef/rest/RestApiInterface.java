package edu.gatech.cs4261.wheresdabeef.rest;

import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.cs4261.wheresdabeef.domain.Image;
import edu.gatech.cs4261.wheresdabeef.domain.Keyword;

/**
 * Created by Kyle.
 */
public class RestApiInterface {
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";

    public static final String SORT_IMG_ID = "id";
    public static final String SORT_IMG_LAT = "latitude";
    public static final String SORT_IMG_LON = "longitude";

    public static final String SORT_KW_ID = "id";
    public static final String SORT_KW_IMAGE = "image";

    private static final String BASE_IMG_URL =
            "http://dev.m.gatech.edu/d/gtg310x/api/imagepail/image";
    public static final String BASE_KW_URL =
            "http://dev.m.gatech.edu/d/gtg310x/api/imagepail/keyword";

    private void fillImageData(final Image i) throws IOException {
        String url = BASE_IMG_URL + "/" + i.getId();

        byte[] image = RestApi.getImage(url, "image");
        //i.setImage(BitmapFactory.decodeByteArray(image, 0, image.length));

        byte[] thumbnail = RestApi.getImage(url, "thumbnail");
        //i.setThumbnail(BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length));
    }

    public Image getImage(final int id) throws IOException {
        String url = BASE_IMG_URL + "/" + id;
        JSONArray jsonArr = RestApi.get(url, new HashMap<String, String>());

        Image image;
        try {
            JSONObject json = jsonArr.getJSONObject(0);

            image = new Image(json.getInt("id"));
            image.setLatitude(json.getDouble("latitude"));
            image.setLongitude(json.getDouble("longitude"));
        } catch (final JSONException e) {
            return null; // swallow error
        }

        fillImageData(image);

        return image;
    }

    /**
     * Retrieves a list of images based upon supplied parameters
     *
     * @param sortDir The initial sort direction of the images
     * @param sortCol The initial column to sort
     * @param limit The max number of records to return
     * @param keyword A keyword to search on. Exclusive with Lat&Lon
     * @param minLat A minimum latitude to search on, must include the other 3
     * @param maxLat A maximum latitude to search on, must include the other 3
     * @param minLon A minimum longitude to search on, must include the other 3
     * @param maxLon A maximum longitude to search on, must include the other 3
     * @return An ArrayList of Image objects, potentially empty.
     */
    public List<Image> getImages(final String sortDir,
                                 final String sortCol,
                                 final Integer limit,
                                 final String keyword,
                                 final Double minLat,
                                 final Double maxLat,
                                 final Double minLon,
                                 final Double maxLon) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("sd", sortDir);
        params.put("sc", sortCol);
        params.put("l", String.valueOf(limit));
        params.put("k", keyword);
        params.put("minLat", String.valueOf(minLat));
        params.put("maxLat", String.valueOf(maxLat));
        params.put("minLon", String.valueOf(minLon));
        params.put("maxLon", String.valueOf(maxLon));
        JSONArray jsonArr = RestApi.get(BASE_IMG_URL, params);

        List<Image> images = new ArrayList<Image>();

        try {
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject json = jsonArr.getJSONObject(i);

                Image image = new Image(json.getInt("id"));
                image.setLatitude(json.getDouble("latitude"));
                image.setLongitude(json.getDouble("longitude"));

                fillImageData(image);

                images.add(image);
            }
        } catch (final JSONException e) {
            return null; // swallow error
        }

        return images;
    }

    public int saveImage(final Image i) throws IOException {
        JSONObject json = RestApi.postImage(BASE_IMG_URL + "/", i);

        int id;
        try {
            id = json.getInt("id");
        } catch (final JSONException e) {
            return -1; // swallow error
        }

        for (final Keyword kw : i.getKeywords()) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("k", kw.getKeyword());
            params.put("imgId", String.valueOf(id));
            RestApi.post(BASE_KW_URL + "/", params);
        }

        return id;
    }

    public List<Keyword> getPopularKeywords(final int limit) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("l", String.valueOf(limit));

        JSONArray jsonArr = RestApi.get(BASE_KW_URL, params);

        List<Keyword> keywords = new ArrayList<Keyword>();

        try {
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject json = jsonArr.getJSONObject(i);

                Keyword keyword = new Keyword(json.getInt("id"));
                keyword.setKeyword(json.getString("keyword"));

                keywords.add(keyword);
            }
        } catch (final JSONException e) {
            return null; // swallow error
        }

        return keywords;
    }
}
