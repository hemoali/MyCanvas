package graphagenic.com.mycanvas.customobjects;

import java.io.Serializable;

/**
 * Created by ibrahim on 6/23/15.
 */
public class InstaPhoto extends Photo implements Serializable {
    String low_url, thumbnail_url;
    int low_w, low_h, thumbnail_w, thumbnail_h;

    public void setId(String id) {
        this.id = id;
    }

    public void setLow_url(String low_url) {
        this.low_url = low_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
    public void setLow_w(int low_w) {
        this.low_w = low_w;
    }

    public void setLow_h(int low_h) {
        this.low_h = low_h;
    }

    public void setThumbnail_w(int thumbnail_w) {
        this.thumbnail_w = thumbnail_w;
    }

    public void setThumbnail_h(int thumbnail_h) {
        this.thumbnail_h = thumbnail_h;
    }

    public String getLow_url() {
        return low_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public int getLow_w() {
        return low_w;
    }

    public int getLow_h() {
        return low_h;
    }

    public int getThumbnail_w() {
        return thumbnail_w;
    }

    public int getThumbnail_h() {
        return thumbnail_h;
    }

}
