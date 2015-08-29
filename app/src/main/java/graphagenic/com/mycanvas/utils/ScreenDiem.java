package graphagenic.com.mycanvas.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by ibrahim on 6/23/15.
 */
public class ScreenDiem {

    public static int screenHeight(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return height;
    }

    public static int screenWidth(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        return width;
    }
}
