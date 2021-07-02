package com.hopetechno.raadarbar.Activity;

import android.annotation.SuppressLint;
import android.content.*;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.Notification.NotificationUtils;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.MyApplication;
import org.json.JSONObject;

public class NewCustomerWaitingActivity extends AppCompatActivity implements AppConstant {
    BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, back, Toast.LENGTH_LONG).show();

    }

    String back;
    CTextView tv;
    CButton ok;
    ProgressBar progressBar;
    protected PowerManager.WakeLock mWakeLock;
    private CountDownTimer countDownTimer;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcustomerwaiting);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        MyApplication.setCurrentActivity(this);

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        back = getResources().getString(R.string.custwaiting);

        progressBar = findViewById(R.id.progressBar);
        tv = findViewById(R.id.tv);
        ok = findViewById(R.id.ok);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String payload = intent.getStringExtra("payload");
                String message = intent.getStringExtra("message");
                String data = intent.getStringExtra("datafromnoti");

                // this code commented for CountDownTimer stop
                // countDownTimer.cancel();
                Log.e("data", data);

                Log.e("message", message);
                try {
                    JSONObject payload1 = new JSONObject(payload);
                    JSONObject dataJsonObject = new JSONObject(data);
                    Log.e("Data Object", dataJsonObject.toString());
                    JSONObject dataObject = dataJsonObject.getJSONObject("data");
                    String title = dataObject.optString("title");
                    String Driver_name = payload1.getString("trip_status");

                    if (Driver_name.equalsIgnoreCase("car_not_available")) {
                        progressBar.setVisibility(View.GONE);
                        ok.setVisibility(View.VISIBLE);
                        tv.setText(title);
                        back = "Please select OK";

                    }
                    else if (Driver_name.equalsIgnoreCase("car_is_available")) {
                        Intent i = new Intent(NewCustomerWaitingActivity.this, CutomerWaitingActivity.class);
                        i.putExtra("payload", payload);
                        i.putExtra("message", message);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    Log.e("asdf  Exception", e.toString());
                }

            }
        };

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String inrent = sharedpreferences.getString("inRental", "");
                if (inrent.equalsIgnoreCase("1")) {
                    sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("inRental", "");
                    editor.putString("isridestart", "");
                    editor.apply();
                }

                Intent i = new Intent(NewCustomerWaitingActivity.this, CustHomePageActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }

    private void showProgressBarTimer() {
        countDownTimer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //this will be done every 1000 milliseconds ( 1 seconds )
                int progress = (int) ((120000 - millisUntilFinished) / 1000);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                //the progressBar will be invisible after 60 000 miliseconds ( 1 minute)
                progressBar.setVisibility(View.GONE);
                String action;
                Intent intent = new Intent(NewCustomerWaitingActivity.this, CustHomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

        }.start();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());


    }
}
