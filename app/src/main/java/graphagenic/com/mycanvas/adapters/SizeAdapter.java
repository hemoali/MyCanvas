package graphagenic.com.mycanvas.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

import graphagenic.com.mycanvas.R;
import graphagenic.com.mycanvas.customobjects.Item;
import graphagenic.com.mycanvas.customobjects.Photo;
import graphagenic.com.mycanvas.ui.RotateActivity;
import graphagenic.com.mycanvas.utils.MyTypeFace;

/**
 * Created by ibrahim on 6/25/15.
 */
public class SizeAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    public static String H, W, code;
    private ArrayList<Item> allPrices = new ArrayList<Item>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;
    Context context;
    Photo photo;
    static ProgressDialog pd;
    View holder = null;
    float Hpx, Wpx;
    String type;

    public SizeAdapter(Context context, Photo photo, ArrayList<Item> allPrices, String type) {
        this.context = context;
        this.photo = photo;
        this.allPrices = allPrices;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.type = type;
    }


    @Override
    public int getCount() {
        return allPrices.size();
    }

    @Override
    public Item getItem(int position) {
        return allPrices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        if (allPrices.get(position).getCode().equals("Standard") || allPrices.get(position).getCode().equals("Panoramic") || allPrices.get(position).getCode().equals("Square")) {

            holder = mInflater.inflate(R.layout.listview_picksize_header_layout, null);
            TextView name = (TextView) holder.findViewById(R.id.name);
            name.setText(allPrices.get(position).getCode());

            MyTypeFace.setTextViewTypeFace(name, "helbold.ttf", context, 0);
            holder.setClickable(false);
            name.setClickable(false);
            holder.setOnClickListener(null);
        } else {
            holder = mInflater.inflate(R.layout.listview_picksize_size_layout, null);
            TextView size = (TextView) holder.findViewById(R.id.size);
            TextView price = (TextView) holder.findViewById(R.id.price);
            size.setText(allPrices.get(position).getSize());
            price.setText(allPrices.get(position).getPrice());
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String sizeText = allPrices.get(position).getSize().replaceAll(" ", "").replaceAll("\"", "").replace("$", "");
                    String[] sizeArray = sizeText.split("×");
                    W = sizeArray[0];
                    H = sizeArray[1];
                    code = allPrices.get(position).getCode();
                    Hpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, (float) (Float.valueOf(H) * 25.4),
                            context.getResources().getDisplayMetrics());

                    Wpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, (float) (Float.valueOf(W) * 25.4),
                            context.getResources().getDisplayMetrics());

                    Intent i = new Intent(context, RotateActivity.class);
                    i.putExtra("photo", photo);
                    i.putExtra("Hpx", Hpx);
                    i.putExtra("Wpx", Wpx);
                    i.putExtra("type", type);
                    i.putExtra("code", code);
                    i.putExtra("H", H);
                    i.putExtra("W", W);
                    context.startActivity(i);

                }
            });
        }

        return holder;
    }


}