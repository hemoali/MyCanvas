package graphagenic.com.mycanvas.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import graphagenic.com.mycanvas.customobjects.FaceAlbum;
import graphagenic.com.mycanvas.customobjects.FacePhoto;
import graphagenic.com.mycanvas.customobjects.InstaPhoto;
import graphagenic.com.mycanvas.ui.FaceAlbumsActivity;
import graphagenic.com.mycanvas.ui.FacePhotosActivity;
import graphagenic.com.mycanvas.ui.InstaPhotosActivity;

/**
 * Created by ibrahim on 6/23/15.
 */
public class Parse {

    public static ArrayList<InstaPhoto> parseInstaPhotos(Context context, String result) {

        ArrayList<InstaPhoto> allInstaPhotos = new ArrayList<InstaPhoto>();

        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONArray images = jsonObj.getJSONArray("data");

            for (int i = 0; i < images.length(); i++) {
                InstaPhoto instaPhoto = new InstaPhoto();

                JSONObject c = images.getJSONObject(i);
                JSONObject image = c.getJSONObject("images");
                JSONObject low = image
                        .getJSONObject("low_resolution");
                JSONObject thumbnail = image
                        .getJSONObject("thumbnail");
                JSONObject standard = image
                        .getJSONObject("standard_resolution");

                String imageID = c.getString("id");

                instaPhoto.setId(imageID);

                instaPhoto.setLow_url(low.getString("url"));
                instaPhoto.setStandard_url(standard.getString("url"));
                instaPhoto.setThumbnail_url(thumbnail.getString("url"));

                instaPhoto.setLow_h(Integer.valueOf(low.getString("height")));
                instaPhoto.setThumbnail_h(Integer.valueOf(thumbnail.getString("height")));
                instaPhoto.setStandard_h(Integer.valueOf(standard.getString("height")));

                instaPhoto.setLow_w(Integer.valueOf(low.getString("width")));
                instaPhoto.setThumbnail_w(Integer.valueOf(thumbnail.getString("width")));
                instaPhoto.setStandard_w(Integer.valueOf(standard.getString("width")));

                if (standard.getString("url").trim().equals("") && !low.getString("url").trim().equals("")) {
                    instaPhoto.setStandard_url(low.getString("url"));
                    instaPhoto.setStandard_w(Integer.valueOf(low.getString("width")));
                    instaPhoto.setStandard_h(Integer.valueOf(low.getString("height")));

                } else if (standard.getString("url").trim().equals("") && low.getString("url").trim().equals("") && !thumbnail.getString("url").trim().equals("")) {
                    instaPhoto.setStandard_url(thumbnail.getString("url"));
                    instaPhoto.setStandard_w(Integer.valueOf(thumbnail.getString("width")));
                    instaPhoto.setStandard_h(Integer.valueOf(thumbnail.getString("height")));

                    instaPhoto.setLow_url(thumbnail.getString("url"));
                    instaPhoto.setLow_w(Integer.valueOf(thumbnail.getString("width")));
                    instaPhoto.setLow_h(Integer.valueOf(thumbnail.getString("height")));
                }
                allInstaPhotos.add(instaPhoto);

            }

            if (InstaPhotosActivity.page != InstaPhotosActivity.pagesCount) {
                JSONObject pagination = jsonObj.getJSONObject("pagination");
                String next_url = pagination.getString("next_url").replaceAll("\u0026", "&");
                InstaPhotosActivity.setNextUrl(next_url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allInstaPhotos;
    }

    public static int instaMediaCount(String result) {
        int count = 0;
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONObject data = jsonObj.getJSONObject("data");
            JSONObject counts = data.getJSONObject("counts");
            String countsT = counts.getString("media");
            count = Integer.valueOf(countsT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static ArrayList<FaceAlbum> parseFaceAlbums(Context context, String result) {

        ArrayList<FaceAlbum> allFaceAlbums = new ArrayList<FaceAlbum>();
        int i = 0;
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONArray albums = jsonObj.getJSONArray("data");

            for (i = 0; i < albums.length(); i++) {
                FaceAlbum faceAlbum = new FaceAlbum();

                JSONObject album = albums.getJSONObject(i);

                if (album.has("cover_photo")) {
                    faceAlbum.setId(album.getString("id"));
                    faceAlbum.setCount(album.getString("count"));
                    faceAlbum.setName(album.getString("name"));
                    faceAlbum.setCover_photo(album.getString("cover_photo"));
                    faceAlbum.setLink(album.getString("link"));

                    allFaceAlbums.add(faceAlbum);
                }

            }
            JSONObject paging = jsonObj.getJSONObject("paging");
            if (paging.has("next")) {
                FaceAlbumsActivity.addFooterToGrid(true);

                String next_url = paging.getString("next");
                FaceAlbumsActivity.setNextUrl(next_url);
                FaceAlbumsActivity.setListViewScrollListener();

            } else {
                FaceAlbumsActivity.addFooterToGrid(false);
                FaceAlbumsActivity.setNextUrl("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allFaceAlbums;
    }

    /*public static String parseFaceAlbumCoverPhoto(String result) {
        String url = "";
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONArray images = jsonObj.getJSONArray("images");

            for (int i = 0; i < images.length(); i++) {
                FaceAlbum faceAlbum = new FaceAlbum();

                JSONObject image = images.getJSONObject(i);
                int k = 1;
                while (url == "") {
                    if (Integer.valueOf(image.getString("width")) < 150 * k) {
                        url = image.getString("source");
                    }
                    k++;
                }
            }
        } catch (Exception e)

        {
            e.printStackTrace();
        }
        return url;
    }
*/
    public static ArrayList<FacePhoto> parseFacebookPhotos(String result) {
        ArrayList<FacePhoto> allFacePhotos = new ArrayList<FacePhoto>();
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONArray data = jsonObj.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                FacePhoto facePhoto = new FacePhoto();

                JSONObject image = data.getJSONObject(i);

                facePhoto.setId(image.getString("id"));
                facePhoto.setStandard_h(Integer.valueOf(image.getString("height")));
                facePhoto.setStandard_w(Integer.valueOf(image.getString("width")));
                facePhoto.setStandard_url((image.getString("source")));

                JSONArray images = image.getJSONArray("images");
                int thumb_width = 0, thumb_height = 0;
                String thumb_source = "";

                for (int j = 0; j < images.length(); j++) {
                    int height = Integer.valueOf(images.getJSONObject(j).getString("height"));
                    int width = Integer.valueOf(images.getJSONObject(j).getString("width"));
                    String source = (images.getJSONObject(j).getString("source"));

                    if (thumb_width == 0 || width < thumb_width) {
                        thumb_width = width;
                        thumb_height = height;
                        thumb_source = source;

                    }

                }
                facePhoto.setThumbnail_url(thumb_source);
                facePhoto.setThumbnail_w(thumb_width);
                facePhoto.setThumbnail_h(thumb_height);
                allFacePhotos.add(facePhoto);

            }
            JSONObject paging = jsonObj.getJSONObject("paging");
            if (paging.has("next")) {
                FacePhotosActivity.addFooterToGrid(true);

                String next_url = paging.getString("next");
                FacePhotosActivity.setNextUrl(next_url);
                FacePhotosActivity.setGridViewScrollListener();

            } else {
                FacePhotosActivity.addFooterToGrid(false);
                FacePhotosActivity.setNextUrl("");
            }
        } catch (Exception e)

        {
            e.printStackTrace();
        }
        return allFacePhotos;
    }
}