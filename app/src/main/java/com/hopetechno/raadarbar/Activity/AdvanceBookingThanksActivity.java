package com.hopetechno.raadarbar.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import com.hopetechno.raadarbar.Utils.MyApplication;


public class AdvanceBookingThanksActivity extends AppCompatActivity implements AppConstant {
    CTextView subtext;
    CButton submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_booking_thanks);


        MyApplication.setCurrentActivity(this);

        subtext = findViewById(R.id.subtext);
        submit = findViewById(R.id.submit);

        subtext.setText(Html.fromHtml("We will arrange car for you before one hour from Trip start time. In any case of query Please call Our Customer Care no.<b> " + phone));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdvanceBookingThanksActivity.this, CustHomePageActivity.class));
                finish();
            }
        });
    }
}
