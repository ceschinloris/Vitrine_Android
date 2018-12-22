package com.ceschin.loris.vitrine.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ceschin.loris.vitrine.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class Authentication {

    public static void verifyToken (final Context context, final String token)
    {
        String url = "http://10.0.2.2:3000/validateToken";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("onResponse","Token OK");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("onErrorResponse", "Token KO");
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", token);

                return headers;
            }
        };

    }

    public static void getToken (final Context context, final String email, final String password)
    {
        String url ="http://10.0.2.2:3000/login";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        // Add the request to the RequestQueue.
        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
