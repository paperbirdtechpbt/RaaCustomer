package com.hopetechno.raadarbar.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Provider {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("carimage")
    @Expose
    private String carimage;


    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("fueltype")
    @Expose
    private String fueltype;

    @SerializedName("drivername")
    @Expose
    private String drivername;

    @SerializedName("phone")
    @Expose
    private String phone;


    @SerializedName("carnumber")
    @Expose
    private String carnumber;

    @SerializedName("last_long")
    @Expose
    private String last_long;

    @SerializedName("last_lat")
    @Expose
    private String last_lat;

    @SerializedName("trip_status")
    @Expose
    private String trip_status;

    @SerializedName("driver_mode")
    @Expose
    private String driver_mode;

    @SerializedName("raadarbar_car")
    @Expose
    private String raadarbar_car;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("is_regular")
    @Expose
    private String is_regular;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarimage() {
        return carimage;
    }

    public void setCarimage(String carimage) {
        this.carimage = carimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getLast_long() {
        return last_long;
    }

    public void setLast_long(String last_long) {
        this.last_long = last_long;
    }

    public String getLast_lat() {
        return last_lat;
    }

    public void setLast_lat(String last_lat) {
        this.last_lat = last_lat;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }

    public String getDriver_mode() {
        return driver_mode;
    }

    public void setDriver_mode(String driver_mode) {
        this.driver_mode = driver_mode;
    }

    public String getRaadarbar_car() {
        return raadarbar_car;
    }

    public void setRaadarbar_car(String raadarbar_car) {
        this.raadarbar_car = raadarbar_car;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getIs_regular() {
        return is_regular;
    }

    public void setIs_regular(String is_regular) {
        this.is_regular = is_regular;
    }
}
