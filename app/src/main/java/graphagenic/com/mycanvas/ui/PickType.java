package graphagenic.com.mycanvas.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.customobjects.Photo;

/**
 * Created by ibrahim on 6/25/15.
 */
public class PickType extends Activity {
    public String type = "";
    Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_type);
        photo = (Photo) getIntent().getSerializableExtra("photo");
        Log.e("ERROR", photo.getStandard_url());

    }

    public void showGalleryPrices(View v) {
        Intent i = new Intent(this, PickSizeActivity.class);
        i.putExtra("photo", photo);
        i.putExtra("type", "gallery");
        startActivity(i);
    }

    public void showStandardPrices(View v) {
        Intent i = new Intent(this, PickSizeActivity.class);
        i.putExtra("photo", photo);
        i.putExtra("type", "standard");
        startActivity(i);
    }
}
