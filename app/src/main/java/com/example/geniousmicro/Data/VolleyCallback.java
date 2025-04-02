package com.example.geniousmicro.Data;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccessResponse(JSONObject result);


    void onErrorResponse(String error);
}
