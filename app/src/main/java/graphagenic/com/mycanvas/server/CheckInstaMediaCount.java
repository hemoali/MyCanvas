package graphagenic.com.mycanvas.server;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;

import graphagenic.com.mycanvas.customobjects.InstaPhoto;
import graphagenic.com.mycanvas.ui.InstaPhotosActivity;
import graphagenic.com.mycanvas.utils.Checks;
import graphagenic.com.mycanvas.utils.Constants;
import graphagenic.com.mycanvas.utils.MyPreferences;
import graphagenic.com.mycanvas.utils.Parse;
import graphagenic.com.mycanvas.utils.Tools;

/**
 * Created by ibrahim on 6/23/15.
 */
public class CheckInstaMediaCount extends AsyncTask<String, String, String> {
    Context context;
    ArrayList<InstaPhoto> allInstaPhotos;

    public CheckInstaMediaCount(Context context, ArrayList<InstaPhoto> allInstaPhotos) {
        this.context = context;
        this.allInstaPhotos = allInstaPhotos;
    }

    @Override
    protected String doInBackground(String... params) {
        if (Checks.isNetworkAvailable(context)) {
            try {
                MessageDigest digest;
                DefaultHttpClient httpclient = new DefaultHttpClient();


                HttpGet httpget = new HttpGet(
                        "https://api.instagram.com/v1/users/" + MyPreferences.getString(context, Constants.INSTA_ID) + "/?access_token="
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Tools.showShortMsg(context, "Please connect to internet");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        int count = Parse.instaMediaCount(s);
        InstaPhotosActivity.setPagesCount((count / 20) + 1);
        if (count > 20) {
            InstaPhotosActivity.addFooterToGrid();
            InstaPhotosActivity.setGridViewScrollListener();

        }
        InstaPhotosActivity.fillGridView(allInstaPhotos, "");
    }
}
