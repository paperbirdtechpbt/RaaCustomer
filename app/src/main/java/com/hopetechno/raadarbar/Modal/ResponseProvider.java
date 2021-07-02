package com.hopetechno.raadarbar.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseProvider {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("cars")
    @Expose
    private List<Provider> responseProvider;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Provider> getResponseProvider() {
        return responseProvider;
    }

    public void setResponseProvider(List<Provider> responseProvider) {
        this.responseProvider = responseProvider;
    }
}
