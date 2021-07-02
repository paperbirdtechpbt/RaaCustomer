package com.hopetechno.raadarbar.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hopetechno.raadarbar.Activity.PhoneNumberVerifyActivity;
import com.hopetechno.raadarbar.Notification.NotificationUtils;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;

public class InfoWindowDialog extends Dialog implements AppConstant {

    public Activity c;
    public Dialog d;
    TextView yes, txtMessage,txtwar;
    String message;
    String status;

    public static final int FILE_PERMISSION_REQUEST_CODE = 112;

    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public InfoWindowDialog(Activity a, String message) {
        super(a);
        this.c = a;
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_info_window);

        yes = findViewById(R.id.txtok);
        txtMessage = findViewById(R.id.txtMessage);
        txtwar = findViewById(R.id.txtwar);

        if (message != null && !message.isEmpty())
            txtMessage.setText(Html.fromHtml(message));

        SharedPreferences prefs = c.getSharedPreferences("Login", MODE_PRIVATE);

        txtwar.setText("terms & condition ");

//        if (!prefs.getString("distanceText", "").isEmpty())
//            txtwar.setText(Html.fromHtml(c.getResources().getString(R.string.jarny) + "  <b>" + prefs.getString("distanceText", "") + "</b> " + c.getResources().getString(R.string.jarnyone)));

//        txtwar.setVisibility(View.GONE);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}

