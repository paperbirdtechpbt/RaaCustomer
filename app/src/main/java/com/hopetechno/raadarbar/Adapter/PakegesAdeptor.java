package com.hopetechno.raadarbar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hopetechno.raadarbar.R;

public class PakegesAdeptor extends BaseAdapter {
    String[] id, name, kilometer, hours, tax1, tax2, price, condition;

    Context mContext;
    private static LayoutInflater inflater;

    public PakegesAdeptor(Context mContext, String[] id, String[] name, String[] kilometer, String[] hours, String[] tax1,
                          String[] tax2, String[] price, String[] condition) {
        this.mContext = mContext;
        this.id = id;
        this.name = name;
        this.kilometer = kilometer;
        this.hours = hours;
        this.tax1 = tax1;
        this.tax2 = tax2;
        this.price = price;
        this.condition = condition;


        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return id.length;
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
        TextView kmtext, hrtext, basefare;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_pakeges, null);


        holder.kmtext = (TextView) rowView.findViewById(R.id.kmtext);
        holder.hrtext = (TextView) rowView.findViewById(R.id.hrtext);
        holder.basefare = (TextView) rowView.findViewById(R.id.basefare);

        holder.kmtext.setText(kilometer[position]);
        holder.hrtext.setText(hours[position]);
        holder.basefare.setText(price[position]);


        return rowView;
    }
}
