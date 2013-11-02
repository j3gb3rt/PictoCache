package edu.gatech.cs4261.wheresdabeef.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs4261.wheresdabeef.domain.Image;

/**
 * Created by Kyle on 11/2/13.
 */
public class RestApiInterface {
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";

    public static final String SORT_IMG_ID = "id";
    public static final String SORT_IMG_LAT = "latitude";
    public static final String SORT_IMG_LON = "longitude";

    private static final String BASE_IMG_URL =
            "http://dev.m.gatech.edu/d/gtg310x/api/imagepail/image";
    public static final String BASE_KW_URL =
            "http://dev.m.gatech.edu/d/gtg310x/api/imagepail/keyword";

    private void fillImageData(final Image i) {
        // TODO
    }

    public Image getImage(final int id) {
        String url = BASE_IMG_URL + "/" + id;
        JSONArray jsonArr = RestApi.getResponse(url);

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
     * @param latitude A latitude to search on, must include longitude
     * @param longitude A longitude to search on, must include latitude
     * @return An ArrayList of Image objects, potentially empty.
     */
    public List<Image> getImages(final String sortDir,
                                 final String sortCol,
                                 final int limit,
                                 final String keyword,
                                 final double latitude,
                                 final double longitude) {
        String url = BASE_IMG_URL + "?sd=" + sortDir + "&sc=" + sortCol + "&l=" + limit
                + "&k=" + keyword + "&lat=" + latitude + "&lon=" + longitude;
        JSONArray jsonArr = RestApi.getResponse(url);

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
}
