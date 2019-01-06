package com.ceschin.loris.vitrine.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.ceschin.loris.vitrine.service.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vitrine extends BaseObservable implements Parcelable{

    public String id;
    public String name;
    public Double latitude;
    public Double longitude;
    public Double radius;

    List<Picture> picturesList = new ArrayList<>();
    MutableLiveData<List<Picture>> pictures = new MutableLiveData<>();


    public Vitrine (JSONObject object) throws JSONException {
        id = object.getString("_id");
        name = object.getString("name");
        latitude = object.getDouble("latitude");
        longitude = object.getDouble("longitude");
        radius = object.getDouble("radius");
    }

    public void fetchPictures (Context context, final String token) {

        // Request vitrines from api
        final String url = "http://10.0.2.2:3000/vitrines/" + id + "/pictures";

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("LoadPicturesResponse", response.toString());

                        picturesList.clear();

                        for (int i = 0; i < response.length(); i++)
                        {
                            try {
                                Picture p = new Picture(response.getJSONObject(i));
                                picturesList.add(p);

                            } catch (JSONException e) {
                                Log.e("JSONException","ERROR WHILE PARSING JSON IN LOADVITRINEPICTURES");
                                e.printStackTrace();
                            }
                        }

                        pictures.postValue(picturesList);
                        notifyChange();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("LoadVitrinesError", error.toString());
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> headers = new HashMap<>();

                headers.put("authorization", "bearer " + token);

                return headers;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /*
    Parcelable implementation
     */
    public Vitrine (Parcel in) {

        id = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            radius = null;
        } else {
            radius = in.readDouble();
        }
    }

    public static final Creator<Vitrine> CREATOR = new Creator<Vitrine>() {
        @Override
        public Vitrine createFromParcel(Parcel in) {
            return new Vitrine(in);
        }

        @Override
        public Vitrine[] newArray(int size) {
            return new Vitrine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        if (radius == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(radius);
        }
    }
}
