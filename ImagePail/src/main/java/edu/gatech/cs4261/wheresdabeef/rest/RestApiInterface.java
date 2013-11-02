package edu.gatech.cs4261.wheresdabeef.rest;

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

    public Image getImage(final int id) {
        String url = BASE_IMG_URL + "/" + id;
        JSONObject json = RestApi.getResponse(url);

        Image image;
        try {
            image = new Image(json.getInt("id"));
            image.setLatitude(json.getDouble("latitude"));
            image.setLongitude(json.getDouble("longitude"));
        } catch (final JSONException e) {
            return null; // swallow error
        }

        fillImageData(image);

        return image;
    }

    private void fillImageData(final Image i) {
        // TODO
    }

    public List<Image> getImages(final String sortDir,
                                 final String sortCol,
                                 final int limit) {
        String url = BASE_IMG_URL + "?sd=" + sortDir
                + "&sc=" + sortCol + "&l=" + limit;
        JSONObject json = RestApi.getResponse(url);

        List<Image> images = new ArrayList<Image>();

        // TODO retrieve image metadata and image data

        return images;
    }
}
