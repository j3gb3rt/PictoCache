package edu.gatech.cs4261.wheresdabeef.rest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.gatech.cs4261.wheresdabeef.domain.Image;

public class RestApi {
    private static final String CHARSET = "UTF-8";

    public static enum RequestMethod {
		GET, POST
	};

    public static JSONArray get(String url, Map<String, String> params) throws IOException {
        StringBuilder query = new StringBuilder();
        for (String key : params.keySet()) {
            query.append(
                    String.format(key + "=%s&", URLEncoder.encode(params.get(key), CHARSET))
            );
        }

        HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + query).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Charset", CHARSET);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
        String response = reader.readLine();

        try {
            return new JSONArray(response);
        } catch (final JSONException e) {
            throw null;
        }
    }

    public static JSONArray post(String url, Map<String, String> params) throws IOException {
        StringBuilder query = new StringBuilder();
        for (String key : params.keySet()) {
            query.append(
                    String.format(key + "=%s&", URLEncoder.encode(params.get(key), CHARSET))
            );
        }

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true); // Triggers POST
        connection.setRequestProperty("Accept-Charset", CHARSET);
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded;charset=" + CHARSET);

        OutputStream output = connection.getOutputStream();
        try {
            output.write(query.toString().getBytes(CHARSET));
        } finally {
            output.close();
        }

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
        String response = reader.readLine();

        try {
            return new JSONArray(response);
        } catch (final JSONException e) {
            throw null;
        }
    }

    // http://stackoverflow.com/questions/2793150/how-to-use-java-net-urlconnection-to-fire-and-handle-http-requests
    public static JSONArray postImage(String url, Image image) throws IOException {
        return null;
    }

}
