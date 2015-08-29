package graphagenic.com.mycanvas.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by ibrahim on 6/23/15.
 */
public class Tools {
    public static void showShortMsg(final Context context, final String msg) {

        ((Activity) context).runOnUiThread(new Runnable() {

            public void run() {

                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static void showLongMsg(final Context context, final String msg) {

        ((Activity) context).runOnUiThread(new Runnable() {

            public void run() {

                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

            }
        });
    }
}
