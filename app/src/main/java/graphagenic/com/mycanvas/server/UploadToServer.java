package graphagenic.com.mycanvas.server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;

import graphagenic.com.mycanvas.ui.DoneActivity;
import graphagenic.com.mycanvas.utils.Checks;
import graphagenic.com.mycanvas.utils.Constants;
import graphagenic.com.mycanvas.utils.Tools;

/**
 * Created by ibrahim on 6/22/15.
 */
public class UploadToServer extends AsyncTask<Void, Void, Void>

{
    public String nameTXT, emailTXT, phoneTXT, addressTXT, width, height, type;
    ProgressDialog pd;
    Context context;
    boolean done = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setIndeterminate(true);
        pd.setMessage("Please wait...");
        pd.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (done) {
            Intent i = new Intent(context, DoneActivity.class);
            context.startActivity(i);
            ((Activity) context).finish();
        }
        pd.dismiss();
    }

    public UploadToServer(Context context, String nameTXT, String emailTXT, String phoneTXT, String addressTXT, String width, String height, String type) {
        this.context = context;
        this.nameTXT = nameTXT;
        this.emailTXT = emailTXT;
        this.phoneTXT = phoneTXT;
        this.addressTXT = addressTXT;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    String result;

    @Override
    protected Void doInBackground(Void... params) {
        if (Checks.isNetworkAvailable(context)) {
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/MyCanvas/");
                dir.mkdirs();
                File file = new File(dir, "temp.png");

                String urlString = Constants.CALLBACKURL + "/upload.php";
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                    HttpPost httppost = new HttpPost(urlString);

                    MultipartEntity mpEntity = new MultipartEntity();
                    ContentBody cbFile = new FileBody(file);
                    mpEntity.addPart("userfile", cbFile);
                    mpEntity.addPart("name", new StringBody(nameTXT));
                    mpEntity.addPart("email", new StringBody(emailTXT));
                    mpEntity.addPart("phone", new StringBody(phoneTXT));
                    mpEntity.addPart("address", new StringBody(addressTXT));
                    mpEntity.addPart("width", new StringBody(width));
                    mpEntity.addPart("height", new StringBody(height));
                    mpEntity.addPart("type", new StringBody(type));
                    httppost.setEntity(mpEntity);
                    System.out.println("executing request " + httppost.getRequestLine());
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity resEntity = response.getEntity();
                    String responseTXT = EntityUtils.toString(resEntity);

                    if (responseTXT.contains("Done")) {

                        done = true;
                    }

                } catch (Exception ex) {
                    Log.e("Debug", "error: " + ex.getMessage(), ex);
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
