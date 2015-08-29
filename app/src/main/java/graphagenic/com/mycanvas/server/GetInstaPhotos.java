package graphagenic.com.mycanvas.server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.customobjects.InstaPhoto;
import graphagenic.com.mycanvas.ui.InstaPhotosActivity;
import graphagenic.com.mycanvas.utils.Checks;
import graphagenic.com.mycanvas.utils.Constants;
import graphagenic.com.mycanvas.utils.MyPreferences;
import graphagenic.com.mycanvas.utils.Parse;
import graphagenic.com.mycanvas.utils.Tools;

/**
 * Created by ibrahim on 6/22/15.
 */
public class GetInstaPhotos extends AsyncTask<String, String, String> {


    String client_id, client_secret, nextUrl;
    Context context;

    public GetInstaPhotos(Context context, String nextUrl) {
        this.context = context;
        this.nextUrl = nextUrl;
        client_id = context.getResources().getString(R.string.instagram_client_id);
        client_secret = context.getResources().getString(R.string.instagram_client_secret);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            if (nextUrl.equals("")) {
                ArrayList<InstaPhoto> allInstaPhotos = Parse.parseInstaPhotos(context, result);
                new CheckInstaMediaCount(context, allInstaPhotos).execute();
            } else {
                ArrayList<InstaPhoto> allInstaPhotos = Parse.parseInstaPhotos(context, result);
                InstaPhotosActivity.fillGridView(allInstaPhotos, "add");
            }
        }
        InstaPhotosActivity.fetching = false;
    }

    @Override
    protected String doInBackground(String... params) {
        if (Checks.isNetworkAvailable(context)) {
            try {
                if (nextUrl.equals("")) {
                    MessageDigest digest;
                    DefaultHttpClient httpclient = new DefaultHttpClient();


                    HttpGet httpget = new HttpGet(
                            "https://api.instagram.com/v1/users/self/media/recent?access_token="
                                    + MyPreferences.getString(context, Constants.INSTA_ACCESS_TOKEN));
                    HttpResponse response;
                    response = httpclient.execute(httpget);
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    String NL = System.getProperty("line.separator");
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    in.close();
                    String result = sb.toString();

                    return result;
                } else {
                    MessageDigest digest;
                    DefaultHttpClient httpclient = new DefaultHttpClient();


                    HttpGet httpget = new HttpGet(nextUrl);
                    HttpResponse response;
                    response = httpclient.execute(httpget);
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    String NL = System.getProperty("line.separator");
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    in.close();
                    String result = sb.toString();
                    Log.e("More", result);
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Tools.showShortMsg(context, "Please connect to internet");
        }
        return null;
    }


}
