package graphagenic.com.mycanvas.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.customobjects.Photo;

public class RotateActivity extends Activity {
    ImageView image;
    long rotate = 0;
    static ProgressDialog pd;
    Bitmap bm;
    static Activity activity;
    float Hpx, Wpx;
    Photo photo;
    String type, code, H, W;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate);
        image = (ImageView) findViewById(R.id.image);
        photo = (Photo) getIntent().getSerializableExtra("photo");

        if (!(photo).getId().equals("local")) {
            new DownloadImageTask(image).execute((photo).getStandard_url());
        } else {

            bm = resizeBitmap();
            image.setImageBitmap(bm);

        }

        activity = this;
        Hpx = getIntent().getFloatExtra("Hpx", 0);
        Wpx = getIntent().getFloatExtra("Wpx", 0);
        type = getIntent().getStringExtra("type");
        code = getIntent().getStringExtra("code");
        H = getIntent().getStringExtra("H");
        W = getIntent().getStringExtra("W");


    }

    public Bitmap resizeBitmap() {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/MyCanvas/" + "temp.png", bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int targetW = 0, targetH = 0;
        if (photoH > 512 || photoW > 512) {
            targetW = 512;
            targetH = (int) (photoH * (512.0 / photoW));
        } else {
            return BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/MyCanvas/" + "temp.png", bmOptions);
        }
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/MyCanvas/" + "temp.png", bmOptions);
    }

    public void goBack(View v) {
        onBackPressed();
    }

    public void rotate(View v) {


        ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(image,
                "rotation", rotate, rotate + 90);

        imageViewObjectAnimator.setDuration(200); // miliseconds
        imageViewObjectAnimator.start();

        rotate += 90;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void goNext(View v) {
        if (rotate % 360 != 0) {

            try {

                File file = new File(Environment.getExternalStorageDirectory() + "/MyCanvas/" + "temp.png");

                FileOutputStream out = new FileOutputStream(file);
                RotateBitmap(bm, rotate).compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        new parseURI().execute();

    }

    private class parseURI extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(activity);
            pd.setIndeterminate(true);
            pd.setMessage("Please wait...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/MyCanvas/");
            File file = new File(dir, "temp.png");
            doCrop(Uri.fromFile(file), Hpx, Wpx, photo.getStandard_h(), photo.getStandard_w());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public static void hideProgressBar() {
        if (pd != null)
            pd.hide();
    }

    public static class ViewHolder {
        public TextView price;
        public TextView size;
    }

    public static class ViewHeaderHolder {
        public TextView name;
    }

    private void doCrop(Uri picUri, float H, float W, float sH, float sW) {
        try {
            Log.e("ERROR", H + "  " + W + "  --  " + sH + " -- " + sW);

            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", (int) H);
            cropIntent.putExtra("aspectY", (int) W);
            cropIntent.putExtra("scale", true);

            cropIntent.putExtra("outputY", (int) H);
            cropIntent.putExtra("outputX", (int) W);

            cropIntent.putExtra("return-data", false);

            File file = new File(Environment.getExternalStorageDirectory() + "/MyCanvas/" + "temp.png");

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(cropIntent, 100);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Log.e("ERR", errorMessage);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        if (requestCode == 100) {
            Intent i = new Intent(this, Preview.class);
            i.putExtra("width", W);
            i.putExtra("height", H);
            i.putExtra("code", code);
            i.putExtra("type", type);
            startActivity(i);

        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RotateActivity.this);
            pd.setMessage("Please wait...");
            pd.setIndeterminate(true);
            pd.show();
            ;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bm = result;
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/MyCanvas/");
            dir.mkdirs();
            File file = new File(dir, "temp.png");

            try {
                FileOutputStream out = new FileOutputStream(file);
                result.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            pd.hide();
            pd.dismiss();
        }
    }
}
