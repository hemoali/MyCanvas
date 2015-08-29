package graphagenic.com.mycanvas.server;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.ui.InstaPhotosActivity;
import graphagenic.com.mycanvas.ui.MainActivity;
import graphagenic.com.mycanvas.utils.Checks;
import graphagenic.com.mycanvas.utils.Constants;
import graphagenic.com.mycanvas.utils.MyPreferences;
import graphagenic.com.mycanvas.utils.Tools;

/**
 * Created by ibrahim on 6/22/15.
 */
public class InstaLogin extends AsyncTask<Void, Void, Void>

{
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainActivity.hideInstaDialog();
        Intent intent = new Intent(context, InstaPhotosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    String client_id, client_secret, request_token;
    Context context;

    public InstaLogin(Context context, String request_token) {
        this.context = context;
        this.request_token = request_token;
        client_id = context.getResources().getString(R.string.instagram_client_id);
        client_secret = context.getResources().getString(R.string.instagram_client_secret);

    }

    String result;

    @Override
    protected Void doInBackground(Void... params) {
        if (Checks.isNetworkAvailable(context)) {
            try {

                String urlString = "https://api.instagram.com/oauth/access_token";
                try {
                    String query = "client_id=" + client_id + "&client_secret=" + client_secret + "&grant_type=authorization_code&redirect_uri=" + Constants.CALLBACKURL + "&code=" + request_token;

                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(10000);
                    Writer writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    result = in.readLine();
                    Log.d("DATA", result);
                } catch (Exception ex) {
                    Log.e("Debug", "error: " + ex.getMessage(), ex);
                }

                JSONObject jsonObject = (JSONObject) new JSONTokener(result).nextValue();
                MyPreferences.add(context, Constants.INSTA_ACCESS_TOKEN, jsonObject.getString("access_token"), "string");
                MyPreferences.add(context, Constants.INSTA_USER_NAME, jsonObject.getJSONObject("user").getString("username"), "string");
                MyPreferences.add(context, Constants.INSTA_BIO, jsonObject.getJSONObject("user").getString("bio"), "string");
                MyPreferences.add(context, Constants.INSTA_WEBSITE, jsonObject.getJSONObject("user").getString("website"), "string");
                MyPreferences.add(context, Constants.INSTA_PROFILE_PICTURE, jsonObject.getJSONObject("user").getString("profile_picture"), "string");
                MyPreferences.add(context, Constants.INSTA_FULL_NAME, jsonObject.getJSONObject("user").getString("full_name"), "string");
                MyPreferences.add(context, Constants.INSTA_ID, jsonObject.getJSONObject("user").getString("id"), "string");
                Log.e("Acce", jsonObject.getString("access_token"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Tools.showShortMsg(context, "Please connect to internet");
        }
        return null;
    }
}
