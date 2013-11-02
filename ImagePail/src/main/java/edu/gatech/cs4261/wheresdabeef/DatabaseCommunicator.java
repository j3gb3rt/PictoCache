package edu.gatech.cs4261.wheresdabeef;

/**
 * Created by jonathan on 10/31/13.
 */

//import org.apache.http.client.ClientProtocolException;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONObject;
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class DatabaseTask extends AsyncTask<DBDataCapsule,Void,String>{
//
//    static InputStream is = null;
//    static JSONObject jObj = null;
//    static String response = "";
//    static String url = "http://eggames.mooo.com:10424/";
//    List<NameValuePair> params = new ArrayList<NameValuePair>();
//
//    // function get json from url
//    // by making HTTP POST or GET mehtod
//
//    @Override
//    protected String doInBackground(DBDataCapsule... arg0) {
//        switch (arg0[0].getAction()){
//            case CHECK_LOGIN:
//                url = url + "check_login";
//                break;
//            case ADD_ACCOUNT:
//                url = url + "add_user";
//                break;
//            case LOCK_ACCOUNT:
//                url = url + "change_user_status";
//                break;
//            case UNLOCK_ACCOUNT:
//                url = url + "change_user_status";
//                break;
//            case ADD_ITEM:
//                url = url + "add_item";
//                break;
//            case SET_PRIVILEDGES:
//                url = url + "change_user_privileges";
//                break;
//            case DELETE_ITEM:
//                url = url + "remove_item";
//                break;
//            case DELETE_ACCOUNT:
//                url = url + "remove_user";
//                break;
//            case GET_USERS_ITEMS:
//                url = url + "get_user_items";
//                break;
//            case GET_ACCOUNTS:
//                url = url + "get_user";
//                break;
//            case CHECK_EXISTS:
//                url = url + "check_user_existance";
//                break;
//            case GET_ALL_ITEMS:
//                url = url + "get_all_items";
//                break;
//        };
//        params = arg0[0].getParameters();
//        // Making HTTP request
//        try {
//
//            // check for request method
//            //                  if(method == "POST"){
//            // request method is POST
//            // defaultHttpClient
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost(url);
//            httpPost.setEntity(new UrlEncodedFormEntity(params));
//
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            is = httpEntity.getContent();
//
//            //                  }else if(method == "GET"){
//            //                      // request method is GET
//            //                      DefaultHttpClient httpClient = new DefaultHttpClient();
//            //                      String paramString = URLEncodedUtils.format(params, "utf-8");
//            //                      url += "?" + paramString;
//            //                      HttpGet httpGet = new HttpGet(url);
//            //
//            //                      HttpResponse httpResponse = httpClient.execute(httpGet);
//            //                      HttpEntity httpEntity = httpResponse.getEntity();
//            //                      is = httpEntity.getContent();
//            //                  }
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String json = "";
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    is, "iso-8859-1"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            is.close();
//            json = sb.toString();
//        } catch (Exception e) {
//            Log.e("Buffer Error", "Error converting result " + e.toString());
//        }
//        return json;
//
//    }
//
