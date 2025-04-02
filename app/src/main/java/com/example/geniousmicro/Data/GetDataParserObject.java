package com.example.geniousmicro.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class GetDataParserObject {
    ProgressDialog dialog;
    static JSONObject resResponse;

    private void createDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
    }

    private void showpDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    private void hidepDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    public GetDataParserObject(Context context, String url, final VolleyCallback callback) {
        createDialog(context);
        showpDialog();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d("Jresponse: ", response);
                            callback.onSuccessResponse(jsonResponse);  // Pass response to the callback
                        } catch (Exception e) {
                            Toast.makeText(context, "Unable to process Request", Toast.LENGTH_SHORT).show();
                            Log.d("exError: ", e.toString());
                            callback.onErrorResponse(e.toString());
                        }
                        hidepDialog();  // Move dialog dismissal here
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Unable to Process Request", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", error.toString());
                callback.onErrorResponse(error.toString());
                hidepDialog();  // Move dialog dismissal here
            }
        });

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, // Timeout in milliseconds
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(postRequest);
    }
}

