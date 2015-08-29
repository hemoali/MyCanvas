package graphagenic.com.mycanvas.ui;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.adapters.SizeAdapter;
import graphagenic.com.mycanvas.customobjects.Item;
import graphagenic.com.mycanvas.customobjects.Photo;
import graphagenic.com.mycanvas.utils.Prices;

/**
 * Created by ibrahim on 6/25/15.
 */
public class PickSizeActivity extends ListActivity {
    private SizeAdapter sizeAdapter;
    String type;
    Photo photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photo = (Photo) getIntent().getSerializableExtra("photo");
        type = getIntent().getStringExtra("type");

        ArrayList<Item> allPrices = new ArrayList<Item>();
        if (type.equals("gallery")) {
            allPrices = Prices.getGalleryPrices();
        } else if (type.equals("standard")) {
            allPrices = Prices.getStandardPrices();
        }
        sizeAdapter = new SizeAdapter(this, photo, allPrices, type);
/*
        sizeAdapter.addSectionHeaderItem("Standard Sizes");
        for (Map.Entry<String, String> entry : getGalleryStandardSizes.entrySet()) {
            sizeAdapter.addItem(entry.getKey(), entry.getValue());
        }
        sizeAdapter.addSectionHeaderItem("Panoramic Sizes");
        for (Map.Entry<String, String> entry : getGalleryPanoramicSizes.entrySet()) {
            sizeAdapter.addItem(entry.getKey(), entry.getValue());
        }
        sizeAdapter.addSectionHeaderItem("Square Sizes");
        for (Map.Entry<String, String> entry : getGallerySquareSizes.entrySet()) {
            sizeAdapter.addItem(entry.getKey(), entry.getValue());
    }    }*/


    /*if(type.equals("standard"))

    {
        sizeAdapter.addSectionHeaderItem("Standard Sizes");
        for (Map.Entry<String, String> entry : getStandardStandardSizes.entrySet()) {
            sizeAdapter.addItem(entry.getKey(), entry.getValue());
        }
        sizeAdapter.addSectionHeaderItem("Panoramic Sizes");
        for (Map.Entry<String, String> entry : getStandardPanoramicSizes.entrySet()) {
            sizeAdapter.addItem(entry.getKey(), entry.getValue());
        }
        sizeAdapter.addSectionHeaderItem("Square Sizes");
        for (Map.Entry<String, String> entry : getStandardSquareSizes.entrySet()) {
            sizeAdapter.addItem(entry.getKey(), entry.getValue());
        }
    }*/

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) getListView()
                .getLayoutParams();

        mlp.setMargins(20, 20, 20, 20);

        getListView()

                .

                        setPadding(20, 20, 20, 20);

        getListView().

                setLayoutParams(mlp);

        getListView()

                .

                        setBackgroundResource(R.drawable.pick_size_listview_background);

        setListAdapter(sizeAdapter);


    }


}