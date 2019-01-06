package com.ceschin.loris.vitrine.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {

    public String id;
    public String email;
    public String username;

    public User (JSONObject in)
    {
        try {
            id = in.getString("_id");
            email = in.getString("email");
            username = in.getString("username");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /*
    Parcelable Implementation
     */
    public User (Parcel in) {
        id = in.readString();
        email = in.readString();
        username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(username);
    }

}
