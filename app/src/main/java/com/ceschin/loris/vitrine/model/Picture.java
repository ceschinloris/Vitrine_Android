package com.ceschin.loris.vitrine.model;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONObject;

import java.util.Date;

public class Picture {

    public String dataID;
    public Bitmap data;
    public Date time;
    public User author;
    public Vitrine vitrine;


    public Picture(JSONObject jsonObject) {
        Log.d("PICTURE", "PictureJsonObject : " + jsonObject.toString());
    }
}
