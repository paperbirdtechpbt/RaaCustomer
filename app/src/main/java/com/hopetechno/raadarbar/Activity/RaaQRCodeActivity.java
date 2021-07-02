package com.hopetechno.raadarbar.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.MyApplication;

public class RaaQRCodeActivity extends AppCompatActivity {

    int iscust;
//    boolean login;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (iscust == 1) {
            startActivity(new Intent(RaaQRCodeActivity.this, CustHomePageActivity.class));
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raa_qrcode);

        MyApplication.setCurrentActivity(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        iscust = getIntent().getExtras().getInt("iscust");

    }
}
