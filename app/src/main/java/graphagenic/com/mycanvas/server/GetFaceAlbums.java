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

import graphagenic.com.mycanvas.customobjects.FaceAlbum;
import graphagenic.com.mycanvas.ui.FaceAlbumsActivity;
import graphagenic.com.mycanvas.utils.Checks;
import graphagenic.com.mycanvas.utils.Constants;
import graphagenic.com.mycanvas.utils.MyPreferences;
import graphagenic.com.mycanvas.utils.Parse;
import graphagenic.com.mycanvas.utils.Tools;

/**
 * Created by ibrahim on 6/22/15.
 */
public class GetFaceAlbums extends AsyncTask<String, String, String> {

    String nextUrl;
    Context context;

    public GetFaceAlbums(Context context, String nextUrl) {
        this.context = context;
        this.nextUrl = nextUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //if (nextUrl.equals(""));
        //    FaceAlbumsActivity.hideProgessBar(false);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            if (nextUrl.equals("")) {
                ArrayList<FaceAlbum> allFaceAlbums = Parse.parseFaceAlbums(context, result);
                FaceAlbumsActivity.fillListView(allFaceAlbums, "");
            } else {
                ArrayList<FaceAlbum> allFaceAlbums = Parse.parseFaceAlbums(context, result);
                FaceAlbumsActivity.fillListView(allFaceAlbums, "add");
            }
        }
        FaceAlbumsActivity.fetching = false;
    }

    @Override
    protected String doInBackground(String... params) {
        if (Checks.isNetworkAvailable(context)) {
            try {
                if (nextUrl.equals("")) {
                    MessageDigest digest;
                    DefaultHttpClient httpclient = new DefaultHttpClient();

                    HttpGet httpget = new HttpGet(
                            "https://graph.facebook.com/" + MyPreferences.getString(context, Constants.FACE_ID) + "/albums?access_token=" + MyPreferences.getString(context, Constants.FACE_ACCESS_TOKEN));

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
                    Log.e("FACE", result + " =  " + "https://graph.facebook.com/" + MyPreferences.getString(context, Constants.FACE_ID) + "/albums?access_token=" + MyPreferences.getString(context, Constants.FACE_ACCESS_TOKEN));
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
