package com.hopetechno.raadarbar.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class FaveAdapter extends ArrayAdapter<String> {

    private ArrayList<String> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        CTextView addressTitle;
        RelativeLayout relative_fav;
    }

    public FaveAdapter(ArrayList<String> data, Context context) {
        super(context, R.layout.item_fav, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_fav, parent, false);

            viewHolder.addressTitle = (CTextView) convertView.findViewById(R.id.address_title);
            viewHolder.relative_fav = (RelativeLayout) convertView.findViewById(R.id.relative_fav);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        assert dataModel != null;
        viewHolder.addressTitle.setText(dataModel);

        viewHolder.relative_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataSet.remove(position);
                notifyDataSetChanged();

                try {
                    String newfav123 = "";
                    for (int i = 0; i < dataSet.size(); i++) {
                        if (newfav123.equalsIgnoreCase("")) {
                            newfav123 = dataSet.get(i);
                        } else {
                            newfav123 = newfav123 + "<mrrhope>" + dataSet.get(i);
                        }
                    }

                    SharedPreferences sharedpreferences;
                    sharedpreferences = mContext.getSharedPreferences("Login", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("favplace", newfav123);
                    editor.apply();

                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
            }
        });
        viewHolder.addressTitle.setTag(position);
        viewHolder.relative_fav.setTag(position);


        // Return the completed view to render on screen
        return convertView;
    }
}