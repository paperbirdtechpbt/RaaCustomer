package com.hopetechno.raadarbar.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponseProvider {

    @SerializedName("success")
    @Expose
    private ResponseProvider responseProvider;

    public ResponseProvider getResponseProvider() {
        return responseProvider;
    }

    public void setResponseProvider(ResponseProvider responseProvider) {
        this.responseProvider = responseProvider;
    }
}
