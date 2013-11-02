package edu.gatech.cs4261.wheresdabeef.rest;

import java.io.IOException;

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

public class RestApi {

    /*

    public static enum RequestMethod {
		GET, POST
	};

	public static JSONObject getResponse(String url, RequestMethod method,
			Map<String, String> params) throws IOException {
		URL reqUrl = new URL(url);
		HttpURLConnection httpCon = (HttpURLConnection) reqUrl.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod(method.toString());

		switch (method) {
		case GET:
			break;
		case POST:
			break;
		}

		httpCon.setFixedLengthStreamingMode(new String().getBytes().length);
		PrintWriter out = new PrintWriter(httpCon.getOutputStream());
		out.print(new String());
		out.close();
		InputStream in = new BufferedInputStream(httpCon.getInputStream());
		httpCon.disconnect();
		return null;
	}


     */

	public static JSONArray getResponse(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			try {
				JSONArray json = new JSONArray(EntityUtils.toString(response
						.getEntity()));
				return json;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
