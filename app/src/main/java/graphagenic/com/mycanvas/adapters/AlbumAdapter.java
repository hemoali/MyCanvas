package graphagenic.com.mycanvas.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.customclasses.SingleTapConfirm;
import graphagenic.com.mycanvas.customobjects.FaceAlbum;
import graphagenic.com.mycanvas.ui.FacePhotosActivity;

public class AlbumAdapter extends BaseAdapter {
    private Context context;
    int imgWidth;
    public static ArrayList<FaceAlbum> allFaceAlbums;
    public static Bitmap albumCache[], tempAlbumCache[];

    // Constructor
    public AlbumAdapter(Context c, ArrayList<FaceAlbum> allFaceAlbums) {
        context = c;
        this.allFaceAlbums = allFaceAlbums;
        albumCache = new Bitmap[allFaceAlbums.size()];
    }

    public void add(ArrayList<FaceAlbum> faceAlbums) {
        for (int i = 0; i < faceAlbums.size(); i++) {
            allFaceAlbums.add(faceAlbums.get(i));
        }
        tempAlbumCache = albumCache;
        albumCache = new Bitmap[tempAlbumCache.length + faceAlbums.size()];
        for (int i = 0; i < tempAlbumCache.length; i++) {
            albumCache[i] = tempAlbumCache[i];
        }
    }

    @Override
    public int getCount() {
        return allFaceAlbums.size();
    }

    @Override
    public Object getItem(int position) {
        return allFaceAlbums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final TextView textView = new TextView(context);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setPadding(10, 40, 10, 40);
        textView.setBackgroundResource(R.drawable.pick_type_background_selector);
        textView.setGravity(Gravity.CENTER);
        textView.setFocusableInTouchMode(true);/*
        textView.setBackgroundColor(Color.parseColor("#40ffffff"));*/
        textView.setText(allFaceAlbums.get(position).getName());
        final GestureDetector gestureDetector = new GestureDetector(context, new SingleTapConfirm());

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (gestureDetector.onTouchEvent(event)) {
                    Intent i = new Intent(context, FacePhotosActivity.class);
                    i.putExtra("album_id", allFaceAlbums.get(position).getId());
                    i.putExtra("album_count", allFaceAlbums.get(position).getCount());
                    context.startActivity(i);
                    return true;
                }
                return false;
            }
        });
       /* final ImageView imageView = new ImageView(context);
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setFocusableInTouchMode(true);
        imageView.setBackgroundColor(Color.parseColor("#40ffffff"));
        if (albumCache[position] != null) {
            imageView.setImageBitmap(albumCache[position]);
        } else {
            new GetCoverPhotoURL(imageView, position).execute("https://graph.facebook.com/" + allFaceAlbums.get(position).getCover_photo() + "?access_token=" + MyPreferences.getString(context, Constants.FACE_ACCESS_TOKEN));
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgWidth = (ScreenDiem.screenWidth(context) - 20) / 2;
        imageView.setLayoutParams(new GridView.LayoutParams(imgWidth, imgWidth));

        final GestureDetector gestureDetector = new GestureDetector(context, new SingleTapConfirm());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (gestureDetector.onTouchEvent(event)) {
                    Intent i = new Intent(context, FacePhotosActivity.class);
                    i.putExtra("album_id", allFaceAlbums.get(position).getId());
                    i.putExtra("album_count", allFaceAlbums.get(position).getCount());
                    context.startActivity(i);
                    return true;
                } else {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        imageView.setAlpha(0.8f);
                    } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        imageView.setAlpha(1f);
                    }
                }
                return false;
            }
        });
*/
        return textView;
    }

    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        int position;

        public DownloadImageTask(ImageView bmImage, int position) {
            this.bmImage = bmImage;
            this.position = position;
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
            albumCache[position] = result;
            if (position == 6 || allFaceAlbums.size() < 6)
                FaceAlbumsActivity.hideProgessBar(true);
        }
    }

   /* private class GetCoverPhotoURL extends AsyncTask<String, Void, String> {
        ImageView bmImage;
        int position;

        public GetCoverPhotoURL(ImageView bmImage, int position) {
            this.bmImage = bmImage;
            this.position = position;
        }

        protected String doInBackground(String... urls) {
            String urldisplay = urls[0];
            if (Checks.isNetworkAvailable(context)) {
                try {
                    MessageDigest digest;
                    DefaultHttpClient httpclient = new DefaultHttpClient();

                    HttpGet httpget = new HttpGet(urldisplay);

                    HttpResponse response;
                    response = httpclient.execute(httpget);
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            response.getEntity().getContent()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    String NL = System.getProperty("line.separator");
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    in.close();
                    String result = sb.toString();
                    Log.e("FACEBOOK", result);
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Tools.showShortMsg(context, "Please connect to internet");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            String url = Parse.parseFaceAlbumCoverPhoto(result);
            new DownloadImageTask(bmImage, position).execute(url);

        }
    }
*/
}