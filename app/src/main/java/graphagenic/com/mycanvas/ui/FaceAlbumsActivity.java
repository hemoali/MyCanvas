package graphagenic.com.mycanvas.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.adapters.AlbumAdapter;
import graphagenic.com.mycanvas.customobjects.FaceAlbum;
import graphagenic.com.mycanvas.server.GetFaceAlbums;
import graphagenic.com.mycanvas.utils.MyTypeFace;

public class FaceAlbumsActivity extends Activity {
    static ListView listView;
    static Activity activity;
    static ProgressBar progressBar;
    static String nextUrl;
    static AlbumAdapter albumAdapter;
    static ProgressBar loading;
    public static boolean fetching = true;

    public static void setNextUrl(String url) {
        nextUrl = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face_albums);

        activity = this;
        fetching = true;
        loading = new ProgressBar(activity);
        loading.setClickable(false);
        loading.setIndeterminate(true);

        listView = (ListView) findViewById(R.id.images_list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new GetFaceAlbums(activity, "").execute();

        MyTypeFace.setTextViewTypeFace(((TextView) findViewById(R.id.top_bar_title)), "helbold.ttf", activity, 0);

        //Tools.showLongMsg(activity, "Please wait, this could take few minutes");
    }

    public static void setListViewScrollListener() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                if (this.currentFirstVisibleItem + this.currentVisibleItemCount > albumAdapter
                        .getCount()
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    if (!nextUrl.equals("") && !fetching) {
                        fetching = true;
                        Log.e("NEXT", nextUrl);
                        new GetFaceAlbums(activity, nextUrl).execute();
                    }
                }
            }
        });
    }

    public static void addFooterToGrid(boolean add) {
        if (add) {
            listView.addFooterView(loading);
        } else {
            listView.removeFooterView(loading);
        }
    }

    public static void fillListView(final ArrayList<FaceAlbum> allFaceAlbums, String action) {
        if (action.equals("")) {
            albumAdapter = new AlbumAdapter(activity, allFaceAlbums);
            listView.setAdapter(albumAdapter);
       /*     gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            if (allFaceAlbums.size() > 9) {
                                hideProgessBar(true);
                            }

                            gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    });*/
        } else if (action.equals("add")) {
            albumAdapter.add(allFaceAlbums);
            albumAdapter.notifyDataSetChanged();
        }
        hideProgessBar(true);
    }

    public static void hideProgessBar(boolean hide) {
        if (hide) {
            progressBar.setVisibility(View.GONE);
        } else
            progressBar.setVisibility(View.VISIBLE);
    }

    public void goBack(View v) {
        onBackPressed();
    }
}
