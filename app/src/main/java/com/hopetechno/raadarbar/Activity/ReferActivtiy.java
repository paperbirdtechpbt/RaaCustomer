package com.hopetechno.raadarbar.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.MyApplication;

public class ReferActivtiy extends AppCompatActivity {

    Button btn_invites;
    String sharelinktext, userid;
    SharedPreferences sharedPreferences;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ReferActivtiy.this, CustHomePageActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_activtiy);

        MyApplication.setCurrentActivity(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        userid = sharedPreferences.getString("userid", userid);
        Log.e("main", userid);

        btn_invites = findViewById(R.id.btn_invites);
        btn_invites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite_user();
            }
        });
    }

    private void invite_user() {
        sharelinktext = "https://raadarbar.page.link/?" +
                "link=https://www.raadarbar.com/" +userid+
                "&apn=" + getPackageName() +
                "&st=" + "RaaCabService" +
                "&sd=" + "GetRewards" +
                "&si=" + "https://raadarbar.com/images/front/logo-dark.png";
        Log.e("main", "Short Refer" + sharelinktext);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, sharelinktext);
        intent.setType("text/plain");
        startActivity(intent);
    }
}
