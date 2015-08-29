package graphagenic.com.mycanvas.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;

import graphagenic.com.mycanvas.customclasses.SingleTapConfirm;
import graphagenic.com.mycanvas.customobjects.InstaPhoto;
import graphagenic.com.mycanvas.ui.InstaPhotosActivity;
import graphagenic.com.mycanvas.ui.PickType;
import graphagenic.com.mycanvas.utils.ScreenDiem;

public class InstaImageAdapter extends BaseAdapter {
    private Context mContext;
    int imgWidth;
    ArrayList<InstaPhoto> allInstaPhotos = new ArrayList<InstaPhoto>();
    public static Bitmap imageCache[], tempCache[];

    // Constructor
    public InstaImageAdapter(Context c, ArrayList<InstaPhoto> allInstaPhotos) {
        mContext = c;
        this.allInstaPhotos = allInstaPhotos;
        imageCache = new Bitmap[allInstaPhotos.size()];

        Log.e("ERROR", allInstaPhotos.get(0).getStandard_url());

    }

    public void add(ArrayList<InstaPhoto> instaPhoto) {
        for (int i = 0; i < instaPhoto.size(); i++) {
            allInstaPhotos.add(instaPhoto.get(i));
        }
        tempCache = imageCache;
        imageCache = new Bitmap[tempCache.length + instaPhoto.size()];
        for (int i = 0; i < tempCache.length; i++) {
            imageCache[i] = tempCache[i];
        }
    }

    @Override
    public int getCount() {
        return allInstaPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return allInstaPhotos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ImageView imageView = new ImageView(mContext);
        imageView.setClickable(true);
        imageView.setFocusable(true);
        imageView.setFocusableInTouchMode(true);
        imageView.setBackgroundColor(Color.parseColor("#40ffffff"));
        if (imageCache[position] != null) {
            imageView.setImageBitmap(imageCache[position]);
        } else {
            new DownloadImageTask(imageView, position).execute(allInstaPhotos.get(position).getThumbnail_url());
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgWidth = (ScreenDiem.screenWidth(mContext) - 20) / 3;
        imageView.setLayoutParams(new GridView.LayoutParams(imgWidth, imgWidth));

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    imageView.setAlpha(0.8f);
                else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                    imageView.setAlpha(1f);
                return true;
            }
        });

        final GestureDetector gestureDetector = new GestureDetector(mContext, new SingleTapConfirm());
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (gestureDetector.onTouchEvent(event)) {

                    Intent i = new Intent(mContext, PickType.class);
                    i.putExtra("photo", allInstaPhotos.get(position));
                    mContext.startActivity(i);
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

        return imageView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
            imageCache[position] = result;

            if (position == allInstaPhotos.size() - 1)
                InstaPhotosActivity.hideProgessBar(true);
        }
    }
}