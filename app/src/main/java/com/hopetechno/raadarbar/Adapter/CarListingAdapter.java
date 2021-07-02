package com.hopetechno.raadarbar.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class CarListingAdapter extends BaseAdapter implements AppConstant {
    String[] id, title, carnumber, image, distance, duration;

    Context mContext;
    private static LayoutInflater inflater;

    public CarListingAdapter(Context mContext, String[] id, String[] title, String[] image, String[] carnumber, String[] distance, String[] duration) {
        this.id = id;
        this.title = title;
        this.carnumber = carnumber;
        this.image = image;
        this.distance = distance;
        this.duration = duration;
        this.mContext = mContext;

        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class Holder {
        CTextView title, carnumber, textdistance;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_car, null);

        holder.carnumber = (CTextView) rowView.findViewById(R.id.carnumber);
        holder.title = (CTextView) rowView.findViewById(R.id.title);
        holder.img = (ImageView) rowView.findViewById(R.id.carimg);
        holder.textdistance = (CTextView) rowView.findViewById(R.id.textdistance);

        UrlImageViewHelper.setUrlDrawable(holder.img, image + "cars/primary/thumbnail/" + image[position], R.drawable.raalogo);

        Log.e("test", image + "cars/primary/thumbnail/" + image[position]);
        holder.title.setText(title[position]);
        /*if (!distance[position].equals("null") || distance[position].trim().length() != 0) {
            long l = Long.parseLong(distance[position]);
            DecimalFormat d = new DecimalFormat("##.##");
        }*/
        String time = duration[position];
        if (time.trim().length() > 0) {
            if (time.equalsIgnoreCase("Far")) {
                holder.textdistance.setText("" + time);
            } else {
                holder.textdistance.setText("" + time + " min");
            }

        }

//        holder.textdistance.setText(""+String.format("Value of a: %.2f", distance[position]));
        holder.carnumber.setText(carnumber[position]);

        holder.carnumber.setVisibility(View.GONE);
        /*holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });*/
        return rowView;
    }
}
