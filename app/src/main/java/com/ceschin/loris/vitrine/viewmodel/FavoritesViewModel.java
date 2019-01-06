package com.ceschin.loris.vitrine.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.Observable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.ceschin.loris.vitrine.model.User;
import com.ceschin.loris.vitrine.model.Vitrine;
import com.ceschin.loris.vitrine.service.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoritesViewModel extends AndroidViewModel {

    private String mToken;
    private User mUser;

    private Observable.OnPropertyChangedCallback mCallback;

    private LiveData<List<Vitrine>> vitrineListObservable;

    public FavoritesViewModel(Application application) {
        super (application);
    }

    public void init (String token, User user)
    {
        mToken = token;
        mUser = user;

        vitrineListObservable = new MutableLiveData<>();
        List<Vitrine> list = new ArrayList<Vitrine>();
        ((MutableLiveData<List<Vitrine>>) vitrineListObservable).setValue(list);

        mCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ((MutableLiveData<List<Vitrine>>) vitrineListObservable).setValue(vitrineListObservable.getValue());
            }
        };

        loadVitrines(getApplication().getApplicationContext());
    }

    // Expose  the LiveData Vitrine so the UI can observe it
    public LiveData<List<Vitrine>> getVitrineListObservable() {
        return vitrineListObservable;
    }

    public void loadVitrines(final Context context)
    {

        // Request vitrines from api
        final String url = "http://10.0.2.2:3000/profile/subscriptions";

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("LoadVitrinesResponse", response.toString());

                        ArrayList<Vitrine> vitrines = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++)
                        {
                            try {
                                Vitrine v = new Vitrine(response.getJSONObject(i));
                                v.fetchPictures(context, mToken);
                                v.addOnPropertyChangedCallback(mCallback);
                                vitrines.add(v);
                            } catch (JSONException e) {
                                Log.e("JSONException","ERROR WHILE PARSING JSON IN LOADFAVORITESVITRINES");
                                e.printStackTrace();
                            }
                        }

                        ((MutableLiveData<List<Vitrine>>) vitrineListObservable).setValue(vitrines);
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

                headers.put("authorization", "bearer " + mToken);

                return headers;
            }
        };

        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
