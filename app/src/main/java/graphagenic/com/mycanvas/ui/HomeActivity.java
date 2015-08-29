package graphagenic.com.mycanvas.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.utils.MyTypeFace;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MyTypeFace.setTextViewTypeFace((TextView) findViewById(R.id.moveon), "hel.otf", this,
                0);

    }

    public void moveOn(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}