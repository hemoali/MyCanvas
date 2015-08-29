package graphagenic.com.mycanvas.customobjects;

import java.io.Serializable;

/**
 * Created by ibrahim on 6/23/15.
 */
public class FacePhoto extends Photo implements Serializable {
    String thumbnail_url;
    int thumbnail_w, thumbnail_h;

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public int getThumbnail_w() {
        return thumbnail_w;
    }

    public void setThumbnail_w(int thumbnail_w) {
        this.thumbnail_w = thumbnail_w;
    }

    public int getThumbnail_h() {
        return thumbnail_h;
    }

    public void setThumbnail_h(int thumbnail_h) {
        this.thumbnail_h = thumbnail_h;
    }

}
