package com.ceschin.loris.vitrine.utils;

import android.util.Base64;

import com.ceschin.loris.vitrine.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JWTUtils {

    public static User getUser (String JWTEncoded) {
        String bodyEncoded = JWTEncoded.split("\\.")[1];
        try {

            String body = new String(Base64.decode(bodyEncoded, Base64.URL_SAFE), "UTF-8");
            JSONObject obj = new JSONObject(body);
            return new User(obj);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
