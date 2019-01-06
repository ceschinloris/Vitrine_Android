package com.ceschin.loris.vitrine.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ceschin.loris.vitrine.model.Picture;
import com.ceschin.loris.vitrine.model.User;
import com.ceschin.loris.vitrine.model.Vitrine;

import java.util.List;

public class VitrineDetailsViewModel extends AndroidViewModel {

    private String mToken;
    private User mUser;
    private Vitrine mVitrine;

    private LiveData<List<Picture>> pictures;

    public VitrineDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public void init (String token, User user, Vitrine vitrine) {
        mToken = token;
        mUser = user;
        mVitrine = vitrine;
    }

    public LiveData<List<Picture>> getPicturesListObservable() {
        return pictures;
    }


}
