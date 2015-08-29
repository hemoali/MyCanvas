package graphagenic.com.mycanvas.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import graphagenic.com.mycanvas.R;

public class DoneActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        ((Button) findViewById(R.id.done)).setTypeface(Typeface
                .createFromAsset(getAssets(), "fonts/hel.otf"));

        ((TextView) findViewById(R.id.statusText)).setTypeface(Typeface.createFromAsset(getAssets(),
                 "fonts/hel.otf"));

    }

    public void done(View v) {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}
