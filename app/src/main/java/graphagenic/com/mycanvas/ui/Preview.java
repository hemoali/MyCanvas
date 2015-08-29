package graphagenic.com.mycanvas.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import graphagenic.com.mycanvas.R;

public class Preview extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ImageView image = (ImageView) findViewById(R.id.image);

        File sdCard = Environment.getExternalStorageDirectory();
        File imgFile = new File(sdCard.getAbsolutePath() + "/MyCanvas/" + "temp.png");

        if (imgFile.exists()) {

            Bitmap myBitmap = resizeBitmap();

            image.setImageBitmap(myBitmap);
        }

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

    public void goNext(View v) {
        Intent i = new Intent(this, Details.class);
        i.putExtra("width", getIntent().getStringExtra("width"));
        i.putExtra("height", getIntent().getStringExtra("height"));
        i.putExtra("type", getIntent().getStringExtra("type"));
        i.putExtra("code", getIntent().getStringExtra("code"));
        startActivity(i);

    }

}
