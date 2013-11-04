package edu.gatech.cs4261.wheresdabeef.rest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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

    public static JSONObject post(String url, Map<String, String> params) throws IOException {
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
            return new JSONObject(response);
        } catch (final JSONException e) {
            throw null;
        }
    }

    public static byte[] getImage(String url, String imageType) throws IOException {
        StringBuilder query = new StringBuilder();

        HttpURLConnection connection = (HttpURLConnection) new URL(url + "?imgType=" + imageType).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept-Charset", CHARSET);

        int contentLength = connection.getContentLength();

        byte[] data = new byte[contentLength];
        InputStream inputStream = connection.getInputStream();
        inputStream.read(data);

        return data;
    }

    // http://stackoverflow.com/questions/2793150/how-to-use-java-net-urlconnection-to-fire-and-handle-http-requests
    public static JSONObject postImage(String url, Image image) throws IOException {
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        HttpURLConnection connection =
                (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);

        PrintWriter writer = null;
        try {
            OutputStream outputStream = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true);

            // Send normal param.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"lat\"")
                    .append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + CHARSET).append(CRLF);
            writer.append(CRLF);
            writer.append(String.valueOf(image.getLatitude())).append(CRLF).flush();

            // Send normal param.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"lon\"")
                    .append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + CHARSET).append(CRLF);
            writer.append(CRLF);
            writer.append(String.valueOf(image.getLongitude())).append(CRLF).flush();

            // Send image
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"img\"; filename=\"img.png\"").append(CRLF);
            writer.append("Content-Type: image/png").append(CRLF);
            writer.append(CRLF).flush();

            File f = new File(image.getImage().getPath());
            long l = f.length();
            InputStream input = new FileInputStream(f);
            try {
                byte[] buffer = new byte[1024];
                for (int length = 0; (length = input.read(buffer)) > 0;) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
            } finally {
                try { input.close(); } catch (IOException e) {}
            }
            writer.append(CRLF).flush();

            writer.append("--" + boundary + "--").append(CRLF);
        } finally {
            if (writer != null) writer.close();
        }

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
        String response = reader.readLine();

        try {
            return new JSONObject(response);
        } catch (final JSONException e) {
            throw null;
        }
    }

}
