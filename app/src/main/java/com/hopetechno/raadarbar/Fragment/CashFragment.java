package com.hopetechno.raadarbar.Fragment;


import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.hopetechno.raadarbar.Activity.CustHomePageActivity;
import com.hopetechno.raadarbar.Activity.PaymentActivity;
import com.hopetechno.raadarbar.Fonts.CButton;
import com.hopetechno.raadarbar.Fonts.CTextView;
import com.hopetechno.raadarbar.Notification.NotificationUtils;
import com.hopetechno.raadarbar.R;
import com.hopetechno.raadarbar.Utils.AppConstant;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.hopetechno.raadarbar.Utils.Common.progstart;
import static com.hopetechno.raadarbar.Utils.Common.progstop;

public class CashFragment extends Fragment implements AppConstant {

    Context mContext;
    String estimatedprice = "", strat;

    String payable_amount = "", startkm = "", checkInCity = "", discount_text = "", endkm = "", no_discount = "", free_trip = "", totalkm = "", estimated_price = "", acual_price = "", total_time = "", border_tax = "", tall_tax = "", permission_charger = "", parking_charger = "", driver_allowance = "", driver_night_hold_charge = "", ride_amount = "", EndOtp;

    CTextView payable_amount_text, startkm_text, discount, endkm_text, totalkm_text, estimated_price_text, total_time_text, acual_price_text, border_tax_text, tall_tax_text, permission_charger_text, parking_charger_text, driver_allowance_text, driver_night_hold_charge_text, acual_price_text1, border_tax_text1, tall_tax_text1, permission_charger_text1, parking_charger_text1, driver_allowance_text1, driver_night_hold_charge_text1, endotp_text;
    RelativeLayout discountrel;
    CButton confirm_amount;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    private String wallet_amount;

    RelativeLayout loutwalletamount;
    TextView walleteamount, title_your_wallate_amount, text_temp_payamount;
    String payable_amount_temp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_cash, container, false);
        mContext = getActivity();


        SharedPreferences prefs = mContext.getSharedPreferences("Login", MODE_PRIVATE);
        estimatedprice = prefs.getString("payable_amount", estimatedprice);
        strat = prefs.getString("start", "");
        EndOtp = prefs.getString("endotpnot", "");

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cash123", "yes");
        editor.apply();

        discountrel = (RelativeLayout) RootView.findViewById(R.id.discountrel);
        discount = (CTextView) RootView.findViewById(R.id.discount);
        try {
            JSONObject main = new JSONObject(strat);


            Log.e("checkInCity", "checkInCity Payload : " + strat);

            startkm = main.getString("start_km");
            total_time = main.getString("total_time");
            ride_amount = main.getString("ride_amount");
            permission_charger = main.getString("permission_charger");
            driver_allowance = main.getString("driver_allowance");
            endkm = main.getString("end_km");
            checkInCity = main.getString("incity");
            border_tax = main.getString("border_tax");
            parking_charger = main.getString("parking_charger");
            driver_night_hold_charge = main.getString("driver_night_hold_charge");
            tall_tax = main.getString("tall_tax");
            payable_amount = main.getString("payable_amount");
            total_time = main.getString("total_time");
            totalkm = main.getString("total_km");
            payable_amount = prefs.getString("payable_amount", "");
            payable_amount_temp = prefs.getString("payable_amount", "");
//            wallet_amount = prefs.getString("wallet_amount", "0");
            wallet_amount =  main.getString("wallet_amount");
            free_trip = main.getString("free_trip");
            no_discount = main.getString("no_discount");

            Log.e("checkInCity", "checkInCity : " + checkInCity);

            try {
                if (main.has("discount")) {
                    Log.e("discount", "discount");
                    discount_text = main.getString("discount");
                    discount.setText(Html.fromHtml("<b>- ₹ " + discount_text));

                } else {
                    discountrel.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Log.e("Exception", e.toString());
            }
            Log.e("ride_amount", ride_amount);
        } catch (Exception e) {
            Log.e("Exception Wst", e.toString());
        }

        confirm_amount = RootView.findViewById(R.id.confirm_amount);
        payable_amount_text = RootView.findViewById(R.id.price1);
        startkm_text = RootView.findViewById(R.id.startkm);
        endkm_text = RootView.findViewById(R.id.endkm);
        totalkm_text = RootView.findViewById(R.id.totalkm);
        estimated_price_text = RootView.findViewById(R.id.estimated_price);
        acual_price_text = RootView.findViewById(R.id.acual_price);
        total_time_text = RootView.findViewById(R.id.total_time);
        border_tax_text = RootView.findViewById(R.id.border_tax);
        tall_tax_text = RootView.findViewById(R.id.tall_tax);
        permission_charger_text = RootView.findViewById(R.id.permission_charger);
        parking_charger_text = RootView.findViewById(R.id.parking_charger);
        driver_allowance_text = RootView.findViewById(R.id.driver_allowance);
        driver_night_hold_charge_text = RootView.findViewById(R.id.driver_night_hold_charge);
        acual_price_text1 = RootView.findViewById(R.id.acual_price1);
        border_tax_text1 = RootView.findViewById(R.id.border_tax1);
        tall_tax_text1 = RootView.findViewById(R.id.tall_tax1);
        permission_charger_text1 = RootView.findViewById(R.id.permission_charger1);
        parking_charger_text1 = RootView.findViewById(R.id.parking_charger1);
        driver_allowance_text1 = RootView.findViewById(R.id.driver_allowance1);
        driver_night_hold_charge_text1 = RootView.findViewById(R.id.driver_night_hold_charge1);
        endotp_text = RootView.findViewById(R.id.endotp);
        text_temp_payamount = RootView.findViewById(R.id.text_temp_payamount);

        loutwalletamount = RootView.findViewById(R.id.loutwalletamount);
        walleteamount = RootView.findViewById(R.id.walleteamount);
        title_your_wallate_amount = RootView.findViewById(R.id.your_wallete_amount);

        Log.e("checkInCity", " no_discount :" + no_discount);

//        if (checkInCity.equalsIgnoreCase("1")) {
//
//            if (Integer.valueOf(totalkm) >= 121) {
//
//                if (!no_discount.equals("-1")) {
//
//                    title_your_wallate_amount.setText("Outcity Discount");
//                    walleteamount.setText("" + 500);
//                    loutwalletamount.setVisibility(View.VISIBLE);
//
//                    Double basePrice = Double.valueOf(ride_amount) + 500;
//
//                    Log.e("checkInCity", "checkInCity wallet_amount Price :" + wallet_amount);
//
//                    if (ride_amount.equalsIgnoreCase("") || ride_amount.equalsIgnoreCase("0")) {
//                        acual_price_text.setVisibility(View.GONE);
//                        acual_price_text1.setVisibility(View.GONE);
//                    } else {
//                        acual_price_text.setText(Html.fromHtml("₹ <b>" + basePrice));
//                    }
//                } else {
//                    title_your_wallate_amount.setVisibility(View.GONE);
//                    walleteamount.setVisibility(View.GONE);
//                    acual_price_text.setText(Html.fromHtml("₹ <b>" + ride_amount));
//                }
//
//            } else {
//
//                walleteamount.setVisibility(View.GONE);
//                title_your_wallate_amount.setVisibility(View.GONE);
//
//                if (wallet_amount.equalsIgnoreCase("0")) {
//                    walleteamount.setText("" + wallet_amount);
//                } else {
//                    walleteamount.setText("" + wallet_amount);
//                    loutwalletamount.setVisibility(View.VISIBLE);
//                }
//
//                if (ride_amount.equalsIgnoreCase("") || ride_amount.equalsIgnoreCase("0")) {
//                    acual_price_text.setVisibility(View.GONE);
//                    acual_price_text1.setVisibility(View.GONE);
//                } else {
//                    acual_price_text.setText(Html.fromHtml("₹ <b>" + ride_amount));
//                }
//            }
//
//        } else {

            if (ride_amount.equalsIgnoreCase("") || ride_amount.equalsIgnoreCase("0")) {
                acual_price_text.setVisibility(View.GONE);
                acual_price_text1.setVisibility(View.GONE);
            } else {
                acual_price_text.setText(Html.fromHtml("₹ <b>" + ride_amount));
            }

            if (wallet_amount.equalsIgnoreCase("0")) {
                walleteamount.setText("" + wallet_amount);
            } else {
                walleteamount.setText("" + wallet_amount);
                loutwalletamount.setVisibility(View.VISIBLE);
            }
//        }

        Log.e("asdf", payable_amount + "   " + wallet_amount);


//        if(free_trip.equals("1")) {

        if (Double.parseDouble(payable_amount) <= Double.parseDouble(wallet_amount)) {
            payable_amount = String.valueOf(0);
            text_temp_payamount.setText("" + payable_amount);
        } else if (Double.parseDouble(wallet_amount) < Double.parseDouble(payable_amount)) {
            payable_amount = String.valueOf(Double.parseDouble(payable_amount) - Double.parseDouble(wallet_amount));
            text_temp_payamount.setText("" + payable_amount);
        }

//        }else
//            text_temp_payamount.setText(Html.fromHtml("₹ <b>" + "00.00"));


//        if (Double.parseDouble(payable_amount) <= Double.parseDouble(wallet_amount)) {
//            payable_amount = String.valueOf(0);
//            text_temp_payamount.setText("" + payable_amount);
//        } else if (Double.parseDouble(wallet_amount) < Double.parseDouble(payable_amount)) {
//            payable_amount = String.valueOf(Double.parseDouble(payable_amount) - Double.parseDouble(wallet_amount));
//            text_temp_payamount.setText("" + payable_amount);
//        }


        estimated_price_text.setVisibility(View.GONE);
        //Log.e("TEST", payable_amount + "  " + startkm + "  " + endkm + "  " + totalkm + "  " + estimated_price + "  " + acual_price + "  " + total_time + "  " + border_tax + "  " + tall_tax + "  " + permission_charger + "  " + parking_charger + "  " + driver_allowance + "  " + driver_night_hold_charge + "  " + ride_amount);

        endotp_text.setText(Html.fromHtml("End OTP: " + EndOtp));
        if (!payable_amount.equalsIgnoreCase("")) {
            payable_amount_text.setText(Html.fromHtml("₹ <b>" + payable_amount_temp));
        }
        if (startkm.equalsIgnoreCase("") || startkm.equalsIgnoreCase("0")) {
            startkm_text.setVisibility(View.GONE);
        } else {
            startkm_text.setText(Html.fromHtml("Start KM: <b>" + startkm));
        }
        if (endkm.equalsIgnoreCase("") || endkm.equalsIgnoreCase("0")) {
            endkm_text.setVisibility(View.GONE);
        } else {
            endkm_text.setText(Html.fromHtml("END KM: <b>" + endkm));
        }
        if (totalkm.equalsIgnoreCase("") || totalkm.equalsIgnoreCase("0")) {
            totalkm_text.setVisibility(View.GONE);
        } else {
            totalkm_text.setText(Html.fromHtml("Total KM: <b>" + totalkm));
        }

        if (total_time.equalsIgnoreCase("") || total_time.equalsIgnoreCase("0")) {
            total_time_text.setVisibility(View.GONE);
        } else {
            total_time_text.setText(Html.fromHtml("Total travel time: <b>" + total_time));
        }
        if (border_tax.equalsIgnoreCase("") || border_tax.equalsIgnoreCase("0")) {
            border_tax_text.setVisibility(View.GONE);
            border_tax_text1.setVisibility(View.GONE);
        } else {
            border_tax_text.setText(Html.fromHtml("₹ <b>" + border_tax));
        }
        if (tall_tax.equalsIgnoreCase("") || tall_tax.equalsIgnoreCase("0")) {
            tall_tax_text.setVisibility(View.GONE);
            tall_tax_text1.setVisibility(View.GONE);
        } else {
            tall_tax_text.setText(Html.fromHtml("₹ <b>" + tall_tax));
        }
        if (permission_charger.equalsIgnoreCase("") || permission_charger.equalsIgnoreCase("0")) {
            permission_charger_text.setVisibility(View.GONE);
            permission_charger_text1.setVisibility(View.GONE);
        } else {
            permission_charger_text.setText(Html.fromHtml("₹ <b>" + permission_charger));
        }
        if (parking_charger.equalsIgnoreCase("") || parking_charger.equalsIgnoreCase("0")) {
            parking_charger_text.setVisibility(View.GONE);
            parking_charger_text1.setVisibility(View.GONE);
        } else {
            parking_charger_text.setText(Html.fromHtml("₹ <b>" + parking_charger));
        }
        if (driver_allowance.equalsIgnoreCase("") || driver_allowance.equalsIgnoreCase("0")) {
            driver_allowance_text.setVisibility(View.GONE);
            driver_allowance_text1.setVisibility(View.GONE);
        } else {
            driver_allowance_text.setText(Html.fromHtml("₹ <b>" + driver_allowance));
        }
        if (driver_night_hold_charge.equalsIgnoreCase("") || driver_night_hold_charge.equalsIgnoreCase("0")) {
            driver_night_hold_charge_text.setVisibility(View.GONE);
            driver_night_hold_charge_text1.setVisibility(View.GONE);
        } else {
            driver_night_hold_charge_text.setText(Html.fromHtml("₹ <b>" + driver_night_hold_charge));
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedpreferences;
                sharedpreferences = mContext.getSharedPreferences("Login", Context.MODE_PRIVATE);
                String page = sharedpreferences.getString("page", "");
                String message = sharedpreferences.getString("message", "");
                String payload = sharedpreferences.getString("payload", "");

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("page", "");
                editor.putString("message", "");
                editor.putString("payload", "");
                editor.apply();

                Log.e("payload", payload);
                Log.e("page", page);
                Log.e("message1", message);

                try {
                    JSONObject main = new JSONObject(payload);


                    if (main.has("message")) {
                        String status = main.getString("message");
                        if (status.equalsIgnoreCase("Trip finished Success")) {

                            Log.e("FINAL NOTIFICATION", payload);
                            editor.putString("Driver_namecws", "");
                            editor.putString("phonecws", "");
                            editor.putString("cash123", "");
                            editor.putString("driver_latcws", "");
                            editor.putString("driver_lngcws", "");
                            editor.putString("trip_start_latcws", "");
                            editor.putString("trip_end_latcws", "");
                            editor.putString("trip_start_otpcws", "");
                            editor.putString("carnumbercws", "");
                            editor.putString("driver_photocws", "");
                            editor.putString("car_namecws", "");
                            editor.putString("car_image_textcws", "");
                            editor.putString("finalstartlatcws", "");
                            editor.putString("finalstartlongcws", "");
                            editor.putString("finalendlatcws", "");
                            editor.putString("finalendlongcws", "");
                            editor.remove("finalcws");
                            editor.putString("message1cws", "");
                            editor.remove("isridestart");
                            editor.putString("finalBill", "");
                            editor.putString("endotpnot", "");
                            editor.putString("messagenoti", "");
                            editor.putString("page", "");
                            editor.putString("message", "");
                            editor.putString("payload", "");
                            editor.remove("KEY_PREF_TRIP_ID");
                            editor.apply();


                            //  startActivity(new Intent(mContext, ThanksScreenActivity.class));
                            // getActivity().finish();

                        } else if (status.equalsIgnoreCase("trip_canceled_from_admin")) {

                            progstart(getActivity(), "Remove Data", "Forcefully finished from Administrator please wait...");
                            String message321 = main.getString("message");

                            Log.d("asdf 55  ", message321);


                            editor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis() - COUNT_DOWN_TIME);
                            editor.putString("Driver_namecws", "");
                            editor.putString("phonecws", "");
                            editor.putString("cash123", "");
                            editor.putString("driver_latcws", "");
                            editor.putString("driver_lngcws", "");
                            editor.putString("trip_start_latcws", "");
                            editor.putString("trip_end_latcws", "");
                            editor.putString("trip_start_otpcws", "");
                            editor.putString("carnumbercws", "");
                            editor.putString("driver_photocws", "");
                            editor.putString("car_namecws", "");
                            editor.putString("car_image_textcws", "");
                            editor.putString("finalstartlatcws", "");
                            editor.putString("finalstartlongcws", "");
                            editor.putString("finalendlatcws", "");
                            editor.putString("finalendlongcws", "");
                            editor.remove("finalcws");
                            editor.putString("message1cws", "");
                            editor.remove("isridestart");
                            editor.putString("finalBill", "");
                            editor.putString("endotpnot", "");
                            editor.putString("messagenoti", "");
                            editor.putString("page", "");
                            editor.putString("message", "");
                            editor.putString("payload", "");
                            editor.apply();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progstop();
                                    Intent i = new Intent(getActivity(), CustHomePageActivity.class);
                                    startActivity(i);
//                            gefinish();
                                }
                            }, 3000);

                            return;
                        }

                    } else {
                        Log.e("Data", "Got it");
                        Intent i = new Intent(mContext, PaymentActivity.class);
                        mContext.startActivity(i);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e("Exception ", "CashFragment   " + e.toString());
                }
            }
        };

        return RootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(mContext);

        SharedPreferences sharedpreferences;
        sharedpreferences = mContext.getSharedPreferences("Login", Context.MODE_PRIVATE);
        String page = sharedpreferences.getString("page", "");
        String message = sharedpreferences.getString("message", "");
        String payload = sharedpreferences.getString("payload", "");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("page", "");
        editor.putString("message", "");
        editor.putString("payload", "");
        editor.apply();

//        try {
//            JSONObject main = new JSONObject(payload);
//            if (main.has("message")) {
//                String status = main.getString("message");
//                if (status.equalsIgnoreCase("Trip finished Success")) {
//                    Log.e("FINAL NOTIFICATION", payload);
//                    editor.putString("Driver_namecws", "");
//                    editor.putString("phonecws", "");
//                    editor.putString("cash123", "");
//                    editor.putString("driver_latcws", "");
//                    editor.putString("driver_lngcws", "");
//                    editor.putString("trip_start_latcws", "");
//                    editor.putString("trip_end_latcws", "");
//                    editor.putString("trip_start_otpcws", "");
//                    editor.putString("carnumbercws", "");
//                    editor.putString("driver_photocws", "");
//                    editor.putString("car_namecws", "");
//                    editor.putString("car_image_textcws", "");
//                    editor.putString("finalstartlatcws", "");
//                    editor.putString("finalstartlongcws", "");
//                    editor.putString("finalendlatcws", "");
//                    editor.putString("finalendlongcws", "");
//                    editor.remove("finalcws");
//                    editor.putString("message1cws", "");
//                    editor.remove("isridestart");
//                    editor.putString("finalBill", "");
//                    editor.putString("endotpnot", "");
//                    editor.putString("messagenoti", "");
//                    editor.putString("page", "");
//                    editor.putString("message", "");
//                    editor.putString("payload", "");
//                    editor.remove("KEY_PREF_TRIP_ID");
//                    editor.apply();
//
////                    startActivity(new Intent(mContext, ThanksScreenActivity.class));
////                    getActivity().finish();
//
//                }
//                else if (status.equalsIgnoreCase("trip_canceled_from_admin")) {
//
//                    progstart(getActivity(), "Remove Data", "Forcefully finished from Administrator please wait...");
//                    String message321 = main.getString("message");
//
//                    Log.d("asdf 55  ", message321);
//
//
//                    editor.putLong(KEY_PREF_CANCEL_TIME_STAMP, System.currentTimeMillis() - COUNT_DOWN_TIME);
//                    editor.putString("Driver_namecws", "");
//                    editor.putString("phonecws", "");
//                    editor.putString("cash123", "");
//                    editor.putString("driver_latcws", "");
//                    editor.putString("driver_lngcws", "");
//                    editor.putString("trip_start_latcws", "");
//                    editor.putString("trip_end_latcws", "");
//                    editor.putString("trip_start_otpcws", "");
//                    editor.putString("carnumbercws", "");
//                    editor.putString("driver_photocws", "");
//                    editor.putString("car_namecws", "");
//                    editor.putString("car_image_textcws", "");
//                    editor.putString("finalstartlatcws", "");
//                    editor.putString("finalstartlongcws", "");
//                    editor.putString("finalendlatcws", "");
//                    editor.putString("finalendlongcws", "");
//                    editor.remove("finalcws");
//                    editor.putString("message1cws", "");
//                    editor.remove("isridestart");
//                    editor.putString("finalBill", "");
//                    editor.putString("endotpnot", "");
//                    editor.putString("messagenoti", "");
//                    editor.putString("page", "");
//                    editor.putString("message", "");
//                    editor.putString("payload", "");
//                    editor.apply();
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progstop();
//                            Intent i = new Intent(getActivity(), CustHomePage.class);
//                            startActivity(i);
////                            gefinish();
//                        }
//                    }, 3000);
//
//                    return;
//                }
//
//            }
//            else {
//                Log.e("Data", "Got it");
//                Intent i = new Intent(mContext, PaymentActivity.class);
//                mContext.startActivity(i);
//                getActivity().finish();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            Log.e("Exception ", "CashFragment   " + e.toString());
//        }
    }


}
