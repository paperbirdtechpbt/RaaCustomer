
package com.hopetechno.raadarbar.Modal;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class AcceptTrip {

   

    private int id;
    private String advance_booking_date;
    private String advance_booking_time;
    private String start_point;
    private String start_longitude;
    private String start_latitude;
    private String start_km;
    private String start_date;
    private String start_time;
    private String end_point;
    private String end_longitude;
    private String end_latitude;
    private String end_km;
    private String end_date;
    private String end_time;
    private String ac_trip;
    private String trip_rate_per_km;
    private String trip_rate_per_hr;
    private String extra_kl;
    private String extra_hr;
    private String extra_per_kl_charge;
    private String extra_per_hr_charge;
    private String extra_kl_charge;
    private String extra_hr_charge;
    private String fare;
    private String taxandparking;
    private String tax1;
    private String tax2;
    private String tax_total_price;
    private String totalfare;
    private int estimated_fare;
    private String payment_mode;
    private String trip_status;
    private String start_otp;
    private String end_otp;
    private int user_id;
    private String driver_id;
    private String car_id;
    private String package_id;
    private String company_id;
    private String created_at;
    private String updated_at;
    private int car_type;
    private String fueltype;
    private String incity;
    private String roundtrip;
    private String tall_tax;
    private String border_tax;
    private String permission_charger;
    private String parking_charger;
    private String driver_allowance;
    private String driver_night_hold_charge;
    private String external_user_name;
    private String external_user_mobile;
    private String is_auto_trip;
    private String external_user_email;
    private String estimated_totalkm;
    private String estimated_totalkm_value;
    private String estimated_totalhr;
    private String estimated_totalhr_value;
    private String discount;
    private String online_payment_transaction_id;
    private String status_feedback;
    private String signature_image;
    private int bill_order_number;
    private String force_rate_perkm;
    private String reason;
    private String other;
    private String payment_type;
    private int trip_count_flag;
    private int days;
    private String remark;
    private int free_trip;
    private String accept_lat;
    private String accept_lon;
    private String start_trip_lat;
    private String start_trip_lon;
    private String km;
    private String estimatedprice;
    private int request_driver_id;

    public static AcceptTrip objectFromData(String str) {

        return new Gson().fromJson(str, AcceptTrip.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdvance_booking_date() {
        return advance_booking_date;
    }

    public void setAdvance_booking_date(String advance_booking_date) {
        this.advance_booking_date = advance_booking_date;
    }

    public String getAdvance_booking_time() {
        return advance_booking_time;
    }

    public void setAdvance_booking_time(String advance_booking_time) {
        this.advance_booking_time = advance_booking_time;
    }

    public String getStart_point() {
        return start_point;
    }

    public void setStart_point(String start_point) {
        this.start_point = start_point;
    }

    public String getStart_longitude() {
        return start_longitude;
    }

    public void setStart_longitude(String start_longitude) {
        this.start_longitude = start_longitude;
    }

    public String getStart_latitude() {
        return start_latitude;
    }

    public void setStart_latitude(String start_latitude) {
        this.start_latitude = start_latitude;
    }

    public String getStart_km() {
        return start_km;
    }

    public void setStart_km(String start_km) {
        this.start_km = start_km;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_point() {
        return end_point;
    }

    public void setEnd_point(String end_point) {
        this.end_point = end_point;
    }

    public String getEnd_longitude() {
        return end_longitude;
    }

    public void setEnd_longitude(String end_longitude) {
        this.end_longitude = end_longitude;
    }

    public String getEnd_latitude() {
        return end_latitude;
    }

    public void setEnd_latitude(String end_latitude) {
        this.end_latitude = end_latitude;
    }

    public String getEnd_km() {
        return end_km;
    }

    public void setEnd_km(String end_km) {
        this.end_km = end_km;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAc_trip() {
        return ac_trip;
    }

    public void setAc_trip(String ac_trip) {
        this.ac_trip = ac_trip;
    }

    public String getTrip_rate_per_km() {
        return trip_rate_per_km;
    }

    public void setTrip_rate_per_km(String trip_rate_per_km) {
        this.trip_rate_per_km = trip_rate_per_km;
    }

    public String getTrip_rate_per_hr() {
        return trip_rate_per_hr;
    }

    public void setTrip_rate_per_hr(String trip_rate_per_hr) {
        this.trip_rate_per_hr = trip_rate_per_hr;
    }

    public String getExtra_kl() {
        return extra_kl;
    }

    public void setExtra_kl(String extra_kl) {
        this.extra_kl = extra_kl;
    }

    public String getExtra_hr() {
        return extra_hr;
    }

    public void setExtra_hr(String extra_hr) {
        this.extra_hr = extra_hr;
    }

    public String getExtra_per_kl_charge() {
        return extra_per_kl_charge;
    }

    public void setExtra_per_kl_charge(String extra_per_kl_charge) {
        this.extra_per_kl_charge = extra_per_kl_charge;
    }

    public String getExtra_per_hr_charge() {
        return extra_per_hr_charge;
    }

    public void setExtra_per_hr_charge(String extra_per_hr_charge) {
        this.extra_per_hr_charge = extra_per_hr_charge;
    }

    public String getExtra_kl_charge() {
        return extra_kl_charge;
    }

    public void setExtra_kl_charge(String extra_kl_charge) {
        this.extra_kl_charge = extra_kl_charge;
    }

    public String getExtra_hr_charge() {
        return extra_hr_charge;
    }

    public void setExtra_hr_charge(String extra_hr_charge) {
        this.extra_hr_charge = extra_hr_charge;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getTaxandparking() {
        return taxandparking;
    }

    public void setTaxandparking(String taxandparking) {
        this.taxandparking = taxandparking;
    }

    public String getTax1() {
        return tax1;
    }

    public void setTax1(String tax1) {
        this.tax1 = tax1;
    }

    public String getTax2() {
        return tax2;
    }

    public void setTax2(String tax2) {
        this.tax2 = tax2;
    }

    public String getTax_total_price() {
        return tax_total_price;
    }

    public void setTax_total_price(String tax_total_price) {
        this.tax_total_price = tax_total_price;
    }

    public String getTotalfare() {
        return totalfare;
    }

    public void setTotalfare(String totalfare) {
        this.totalfare = totalfare;
    }

    public int getEstimated_fare() {
        return estimated_fare;
    }

    public void setEstimated_fare(int estimated_fare) {
        this.estimated_fare = estimated_fare;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }

    public String getStart_otp() {
        return start_otp;
    }

    public void setStart_otp(String start_otp) {
        this.start_otp = start_otp;
    }

    public String getEnd_otp() {
        return end_otp;
    }

    public void setEnd_otp(String end_otp) {
        this.end_otp = end_otp;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getCar_type() {
        return car_type;
    }

    public void setCar_type(int car_type) {
        this.car_type = car_type;
    }

    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }

    public String getIncity() {
        return incity;
    }

    public void setIncity(String incity) {
        this.incity = incity;
    }

    public String getRoundtrip() {
        return roundtrip;
    }

    public void setRoundtrip(String roundtrip) {
        this.roundtrip = roundtrip;
    }

    public String getTall_tax() {
        return tall_tax;
    }

    public void setTall_tax(String tall_tax) {
        this.tall_tax = tall_tax;
    }

    public String getBorder_tax() {
        return border_tax;
    }

    public void setBorder_tax(String border_tax) {
        this.border_tax = border_tax;
    }

    public String getPermission_charger() {
        return permission_charger;
    }

    public void setPermission_charger(String permission_charger) {
        this.permission_charger = permission_charger;
    }

    public String getParking_charger() {
        return parking_charger;
    }

    public void setParking_charger(String parking_charger) {
        this.parking_charger = parking_charger;
    }

    public String getDriver_allowance() {
        return driver_allowance;
    }

    public void setDriver_allowance(String driver_allowance) {
        this.driver_allowance = driver_allowance;
    }

    public String getDriver_night_hold_charge() {
        return driver_night_hold_charge;
    }

    public void setDriver_night_hold_charge(String driver_night_hold_charge) {
        this.driver_night_hold_charge = driver_night_hold_charge;
    }

    public String getExternal_user_name() {
        return external_user_name;
    }

    public void setExternal_user_name(String external_user_name) {
        this.external_user_name = external_user_name;
    }

    public String getExternal_user_mobile() {
        return external_user_mobile;
    }

    public void setExternal_user_mobile(String external_user_mobile) {
        this.external_user_mobile = external_user_mobile;
    }

    public String getIs_auto_trip() {
        return is_auto_trip;
    }

    public void setIs_auto_trip(String is_auto_trip) {
        this.is_auto_trip = is_auto_trip;
    }

    public String getExternal_user_email() {
        return external_user_email;
    }

    public void setExternal_user_email(String external_user_email) {
        this.external_user_email = external_user_email;
    }

    public String getEstimated_totalkm() {
        return estimated_totalkm;
    }

    public void setEstimated_totalkm(String estimated_totalkm) {
        this.estimated_totalkm = estimated_totalkm;
    }

    public String getEstimated_totalkm_value() {
        return estimated_totalkm_value;
    }

    public void setEstimated_totalkm_value(String estimated_totalkm_value) {
        this.estimated_totalkm_value = estimated_totalkm_value;
    }

    public String getEstimated_totalhr() {
        return estimated_totalhr;
    }

    public void setEstimated_totalhr(String estimated_totalhr) {
        this.estimated_totalhr = estimated_totalhr;
    }

    public String getEstimated_totalhr_value() {
        return estimated_totalhr_value;
    }

    public void setEstimated_totalhr_value(String estimated_totalhr_value) {
        this.estimated_totalhr_value = estimated_totalhr_value;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOnline_payment_transaction_id() {
        return online_payment_transaction_id;
    }

    public void setOnline_payment_transaction_id(String online_payment_transaction_id) {
        this.online_payment_transaction_id = online_payment_transaction_id;
    }

    public String getStatus_feedback() {
        return status_feedback;
    }

    public void setStatus_feedback(String status_feedback) {
        this.status_feedback = status_feedback;
    }

    public String getSignature_image() {
        return signature_image;
    }

    public void setSignature_image(String signature_image) {
        this.signature_image = signature_image;
    }

    public int getBill_order_number() {
        return bill_order_number;
    }

    public void setBill_order_number(int bill_order_number) {
        this.bill_order_number = bill_order_number;
    }

    public String getForce_rate_perkm() {
        return force_rate_perkm;
    }

    public void setForce_rate_perkm(String force_rate_perkm) {
        this.force_rate_perkm = force_rate_perkm;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public int getTrip_count_flag() {
        return trip_count_flag;
    }

    public void setTrip_count_flag(int trip_count_flag) {
        this.trip_count_flag = trip_count_flag;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getFree_trip() {
        return free_trip;
    }

    public void setFree_trip(int free_trip) {
        this.free_trip = free_trip;
    }

    public String getAccept_lat() {
        return accept_lat;
    }

    public void setAccept_lat(String accept_lat) {
        this.accept_lat = accept_lat;
    }

    public String getAccept_lon() {
        return accept_lon;
    }

    public void setAccept_lon(String accept_lon) {
        this.accept_lon = accept_lon;
    }

    public String getStart_trip_lat() {
        return start_trip_lat;
    }

    public void setStart_trip_lat(String start_trip_lat) {
        this.start_trip_lat = start_trip_lat;
    }

    public String getStart_trip_lon() {
        return start_trip_lon;
    }

    public void setStart_trip_lon(String start_trip_lon) {
        this.start_trip_lon = start_trip_lon;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getEstimatedprice() {
        return estimatedprice;
    }

    public void setEstimatedprice(String estimatedprice) {
        this.estimatedprice = estimatedprice;
    }

    public int getRequest_driver_id() {
        return request_driver_id;
    }

    public void setRequest_driver_id(int request_driver_id) {
        this.request_driver_id = request_driver_id;
    }
}