package com.hopetechno.raadarbar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.Modal.GooglePlaces;
import com.hopetechno.raadarbar.R;

import java.util.ArrayList;


public class GooglePlaceAdapter extends ArrayAdapter<GooglePlaces.PredictionsBean> {

    private ArrayList<GooglePlaces.PredictionsBean> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        CTextView addressTitle;
        CTextView addressDesc;

    }

    public GooglePlaceAdapter(ArrayList<GooglePlaces.PredictionsBean> data, Context context) {
        super(context, R.layout.item_googleplace, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        GooglePlaces.PredictionsBean dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_googleplace, parent, false);

            viewHolder.addressTitle = (CTextView) convertView.findViewById(R.id.address_title);
            viewHolder.addressDesc = (CTextView) convertView.findViewById(R.id.address_desc);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        assert dataModel != null;
        viewHolder.addressTitle.setText(dataModel.getStructured_formatting().getMain_text());
        viewHolder.addressDesc.setText(dataModel.getDescription());

        viewHolder.addressTitle.setTag(position);
        viewHolder.addressDesc.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }
}