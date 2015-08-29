package graphagenic.com.mycanvas.customobjects;

import java.io.Serializable;

/**
 * Created by ibrahim on 6/25/15.
 */
public class Photo implements Serializable{
    String id, standard_url;
    int standard_w, standard_h;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStandard_url() {
        return standard_url;
    }

    public void setStandard_url(String standard_url) {
        this.standard_url = standard_url;
    }

    public int getStandard_w() {
        return standard_w;
    }

    public void setStandard_w(int standard_w) {
        this.standard_w = standard_w;
    }

    public int getStandard_h() {
        return standard_h;
    }

    public void setStandard_h(int standard_h) {
        this.standard_h = standard_h;
    }
}
