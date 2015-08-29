package graphagenic.com.mycanvas.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.adapters.FaceImageAdapter;
import graphagenic.com.mycanvas.customobjects.FacePhoto;
import graphagenic.com.mycanvas.customviews.GridViewWithHeaderAndFooter;
import graphagenic.com.mycanvas.server.GetFacePhotos;
import graphagenic.com.mycanvas.utils.MyTypeFace;

public class FacePhotosActivity extends Activity {
    static GridViewWithHeaderAndFooter gridView;
    static Activity activity;
    static ProgressBar progressBar;
    public static int page;
    public static int pagesCount;
    static String nextUrl;
    static FaceImageAdapter faceImageAdapter;
    static ProgressBar loading;
    public static boolean fetching = true;
    static String albumID;

    public static void setPagesCount(int count) {
        pagesCount = count;
    }

    public static void setNextUrl(String url) {
        nextUrl = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face_photos);

        albumID = getIntent().getStringExtra("album_id");
        activity = this;
        fetching = true;
        page = 1;
        loading = new ProgressBar(activity);
        loading.setClickable(false);
        loading.setIndeterminate(true);

        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.images_grid);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        MyTypeFace.setTextViewTypeFace(((TextView) findViewById(R.id.top_bar_title)), "helbold.ttf", activity, 0);

        new GetFacePhotos(activity, "", albumID).execute();

     /*   new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + albumID + "/photos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        ArrayList<FacePhoto> allFacePhotos = Parse.parseFacebookPhotos(response.getRawResponse());
                        fillGridView(allFacePhotos, "");
                    }
                }
        ).executeAsync();*/

    }

    public static void setGridViewScrollListener() {
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int currentFirstVisibleItem, currentVisibleItemCount,
                    currentScrollState;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                isScrollCompleted();
            }

            public void isScrollCompleted() {
                if (this.currentFirstVisibleItem + this.currentVisibleItemCount > faceImageAdapter
                        .getCount()
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    Log.e("PAGE", page + "  " + pagesCount);
                    if (!nextUrl.equals("") && !fetching) {
                        fetching = true;
                        Log.e("NEXT", nextUrl);
                        new GetFacePhotos(activity, nextUrl, albumID).execute();
                    }
                }
            }
        });
    }

    public static void addFooterToGrid(boolean hide) {

        if (hide)
            gridView.addFooterView(loading);
        else
            gridView.removeFooterView(loading);
    }

    public static void fillGridView(final ArrayList<FacePhoto> allFacePhotos, String action) {
        if (action.equals("")) {
            faceImageAdapter = new FaceImageAdapter(activity, allFacePhotos);
            gridView.setAdapter(faceImageAdapter);
            gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            if (allFacePhotos.size() > 9) {
                                hideProgessBar(true);
                            }

                            gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    });
        } else if (action.equals("add")) {
            faceImageAdapter.add(allFacePhotos);
            faceImageAdapter.notifyDataSetChanged();
            page++;
            if (page == pagesCount) {
                Log.e("Remove", "Remove");
                gridView.removeFooterView(loading);
            }
        }
    }

    public static void hideProgessBar(boolean hide) {
        if (hide) {
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            }, 1500);

        } else
            progressBar.setVisibility(View.VISIBLE);
    }

    public void goBack(View v) {
        onBackPressed();
    }
}
