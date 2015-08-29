package graphagenic.com.mycanvas.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.customobjects.Photo;
import graphagenic.com.mycanvas.server.InstaLogin;
import graphagenic.com.mycanvas.utils.Constants;
import graphagenic.com.mycanvas.utils.MyPreferences;
import graphagenic.com.mycanvas.utils.MyTypeFace;

public class MainActivity extends Activity {

    public static String authURLString;
    public static String tokenURLString;
    public boolean firstTimeInsta = false, firstTimeFace = false;
    String request_token, picturePath;

    WebView webView;
    ProgressBar pb;
    static AlertDialog.Builder builder;
    static Dialog dialog;
    public static Activity activity;
    CallbackManager callbackManager;
    File selectedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        FacebookSdk.sdkInitialize(activity);

        setContentView(R.layout.activity_main);

        final com.facebook.login.widget.LoginButton face_login_button = (com.facebook.login.widget.LoginButton) findViewById(R.id.face_login_button);

        face_login_button.setBackgroundResource(R.drawable.facebook_icon);

        face_login_button.setText("");
        face_login_button.setBackgroundResource(R.drawable.facebook_button_background);
        face_login_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        face_login_button.setReadPermissions("user_photos");

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("facebook - profile",
                                loginResult.getAccessToken().getUserId() + "   " + loginResult.getAccessToken().getToken());
                        MyPreferences.add(activity, Constants.FACE_ID,
                                loginResult.getAccessToken().getUserId(), "string");
                        face_login_button.setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.move_on_to_face)).setVisibility(View.VISIBLE);
                        MyPreferences.add(activity, Constants.FACE_ACCESS_TOKEN, loginResult.getAccessToken().getToken(), "string");
                        moveToFacePhotosActivity();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("ERR", exception.getMessage());
                    }
                });

        if (MyPreferences.getString(getApplicationContext(), Constants.FACE_ACCESS_TOKEN) != null) {
            face_login_button.setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.move_on_to_face)).setVisibility(View.VISIBLE);
        } else { // Go to face images activity

        }
        MyTypeFace.setTextViewTypeFace(
                ((TextView) findViewById(R.id.importfrom)), "hel.otf", this, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            selectedImg = new File(picturePath);

            File sdCard = Environment.getExternalStorageDirectory();
            File imgFile = new File(sdCard.getAbsolutePath() + "/MyCanvas/" + "temp.png");

            new Copy(selectedImg, imgFile).execute();

            cursor.close();


        }
    }

    private class Copy extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd;
        File src, dst;

        public Copy(File src, File dst) {
            this.src = src;
            this.dst = dst;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            pd.dismiss();
            Intent intent = new Intent(activity, PickType.class);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImg.getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;

            Photo photo = new Photo();
            photo.setStandard_url(Environment.getExternalStorageDirectory() + "/MyCanvas/" + "temp.png");
            photo.setStandard_h(imageHeight);
            photo.setStandard_w(imageWidth);
            photo.setId("local");
            intent.putExtra("photo", photo);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }


        @Override
        protected Void doInBackground(Void... params) {

            try {
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dst);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(activity);
            pd.setMessage("Please wait..");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }
    }


    public void facebookSetup(View v) {
        moveToFacePhotosActivity();
    }

    public void moveToFacePhotosActivity() {
        Intent intent = new Intent(activity, FaceAlbumsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void instagramSetup(View v) {
        if (MyPreferences.getString(getApplicationContext(), Constants.INSTA_ACCESS_TOKEN) == null) {
            if (!firstTimeInsta) {
                String client_id = getApplicationContext().getResources().getString(R.string.instagram_client_id);
                String client_secret = getApplicationContext().getResources().getString(R.string.instagram_client_secret);

                authURLString = Constants.AUTHURL + "?client_id=" + client_id + "&redirect_uri=" + Constants.CALLBACKURL + "&response_type=code&display=touch";
                tokenURLString = Constants.TOKENURL + "?client_id=" + client_id + "&client_secret=" + client_secret + "&redirect_uri=" + Constants.CALLBACKURL + "&grant_type=authorization_code";

                pb = (ProgressBar) findViewById(R.id.progressBar);
                webView = (WebView) findViewById(R.id.insta_auth_webview);

                webView.loadUrl(authURLString);
                webView.setWebViewClient(new AuthWebViewClient());
                webView.setWebChromeClient(new AuthWebChromeClient());
                firstTimeInsta = true;
            }
            ((LinearLayout) findViewById(R.id.instaLayout)).setVisibility(View.VISIBLE);
        } else {
            Intent intent = new Intent(getApplicationContext(), InstaPhotosActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }
    }

    public class AuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("E", url);
            if (url.startsWith(Constants.CALLBACKURL)) {
                if (url.contains("&error=access_denied")) {
                    ((LinearLayout) findViewById(R.id.instaLayout)).setVisibility(View.GONE);
                } else if (url.contains("code=")) {
                    System.out.println(url);
                    String parts[] = url.split("=");
                    request_token = parts[1];
                    webView.setVisibility(View.GONE);
                    new InstaLogin(getApplicationContext(), request_token).execute();
                }
            } else {
                view.loadUrl(url);
            }
            return true;
        }
    }

    public class AuthWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view, progress);
            if (progress < 100 && pb.getVisibility() == ProgressBar.GONE) {
                pb.setVisibility(ProgressBar.VISIBLE);
            }
            pb.setProgress(progress);
            if (progress == 100) {
                pb.setVisibility(ProgressBar.GONE);
            }
        }

    }

    public void sdCard(View v) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 2);
    }

    @Override
    public void onBackPressed() {
        if (((LinearLayout) findViewById(R.id.instaLayout)).getVisibility() == View.VISIBLE) {
            ((LinearLayout) findViewById(R.id.instaLayout)).setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public static void hideInstaDialog() {

        ((LinearLayout) activity.findViewById(R.id.instaLayout)).setVisibility(View.GONE);
    }

}
