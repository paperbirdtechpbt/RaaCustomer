package com.hopetechno.raadarbar.Adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.hopetechno.raadarbar.Activity.PdfViewActivity;
import com.hopetechno.raadarbar.Activity.RideDetailActivity;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Other.FileDownloader;
import com.hopetechno.raadarbar.R;

import java.io.File;
import java.io.IOException;

import static com.hopetechno.raadarbar.Utils.Common.hasPermissions;
import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;


public class RideHistryAdeptor extends BaseAdapter {
    String[] id, start_date, end_date, start_point, end_point, pdflink;
    static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    Context mContext;
    String filename_final;
    private static LayoutInflater inflater;

    public RideHistryAdeptor(Context mContext, String[] id, String[] start_date, String[] end_date, String[] start_point, String[] end_point, String[] pdflink) {
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_point = start_point;
        this.end_point = end_point;
        this.pdflink = pdflink;
        this.mContext = mContext;

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
        TextView datetime, from, carnumber, price, to;
        ImageView img;
        CButton clickfordetail, bill;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_ride_history, null);

        holder.datetime = (TextView) rowView.findViewById(R.id.datetime);
        holder.from = (TextView) rowView.findViewById(R.id.from);
        holder.carnumber = (TextView) rowView.findViewById(R.id.carnumber);
        holder.price = (TextView) rowView.findViewById(R.id.price);
        holder.to = (TextView) rowView.findViewById(R.id.to);
        holder.clickfordetail = rowView.findViewById(R.id.clickfordetail);
        holder.bill = rowView.findViewById(R.id.bill);
        // holder.img=(ImageView) rowView.findViewById(R.id.carimg);

        holder.datetime.setText(start_date[position]);
        holder.from.setText(start_point[position]);
        holder.to.setText(end_point[position]);
        if (end_date[position] != null && !end_date[position].isEmpty() && !end_date[position].equals("null")) {
            holder.price.setText(end_date[position]);
            holder.bill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, 112);

                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        Toast t = Toast.makeText(mContext, "You don't have write access !", Toast.LENGTH_LONG);
                        t.show();
                    } else {
                        //Log.v(TAG, "download() Method HAVE PERMISSIONS ");
                        filename_final = end_date[position] + "_" + id[position] + "_Raadarbar.pdf";

                        //new DownloadFile().execute("http://maven.apache.org/maven-1.x/maven.pdf", "maven.pdf");
                        progstart(mContext, "Downloading", "Please wait your Bill is Downloading");
                        new DownloadFile().execute(pdflink[position], filename_final);
                        Log.e("Common.pdad+monthName", pdflink[position]);
                    }

                }
            });
        } else {
            holder.price.setVisibility(View.GONE);
            holder.bill.setVisibility(View.GONE);
        }

        holder.carnumber.setVisibility(View.GONE);


        holder.clickfordetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(mContext, RideDetailActivity.class);
                i.putExtra("id", id[position]);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });


        return rowView;
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        public String url;
        String fileName;

        @Override
        protected Void doInBackground(String... strings) {

            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "Raadarbar");
            folder.mkdir();

            url = fileUrl;

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progstop();
            Toast.makeText(mContext, "Download Complete in Storage " + Environment.getExternalStorageDirectory().toString() + "/Raadarbar/" + filename_final, Toast.LENGTH_LONG).show();
            viewpdf();
        }
    }



    void viewpdf() {
        if (!hasPermissions(mContext, PERMISSIONS)) {
            Toast t = Toast.makeText(mContext, "You don't have read access !", Toast.LENGTH_LONG);
            t.show();
        } else {
            File file;
            Intent intent = new Intent(mContext, PdfViewActivity.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                File directory = mContext.getFilesDir();
                file = new File("/sdcard/Raadarbar/" + filename_final);
                intent.putExtra("file","/sdcard/Raadarbar/" + filename_final);
            } else {
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Raadarbar/" + filename_final);
                intent.putExtra("file",Environment.getExternalStorageDirectory().getAbsolutePath() + "/Raadarbar/" + filename_final);

            }

            intent.setAction(Intent.ACTION_VIEW);

            mContext.startActivity(intent);

//            Intent target = new Intent(Intent.ACTION_VIEW);
//            target.setDataAndType(Uri.fromFile(file),"application/pdf");
//            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            Intent intent = Intent.createChooser(target, "Open File");
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//            try {
//                mContext.startActivity((intent));
//            } catch (ActivityNotFoundException e) {
//            }
        }
    }
    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // Checks if a volume containing external storage is available to at least read.
    private boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }
}
