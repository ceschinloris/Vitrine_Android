package com.ceschin.loris.vitrine.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ceschin.loris.vitrine.R;
import com.ceschin.loris.vitrine.model.Picture;
import com.ceschin.loris.vitrine.model.User;
import com.ceschin.loris.vitrine.model.Vitrine;
import com.ceschin.loris.vitrine.viewmodel.VitrineDetailsViewModel;

import java.util.List;

public class VitrineDetailsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    //private PicturesAdapter mAdapter;

    private String mToken;
    private User mUser;
    private Vitrine mVitrine;

    public VitrineDetailsViewModel mViewModel;

    private List<Picture> mPictureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitrine_details);

        mToken = getIntent().getStringExtra("token");
        mUser = getIntent().getExtras().getParcelable("user");
        mVitrine = getIntent().getExtras().getParcelable("vitrine");

        mViewModel = ViewModelProviders.of(this).get(VitrineDetailsViewModel.class);
        mViewModel.init(mToken, mUser, mVitrine);

        mRecyclerView = (RecyclerView) findViewById(R.id.PictureListRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mPictureList = mViewModel.getPicturesListObservable().getValue();
        //mAdapter = new PicturesAdapter(mPictureList);
        //mRecyclerView.setAdapter(mAdapter);
        observeViewModel(mViewModel);

        TextView textView = findViewById(R.id.textView);
        textView.setText(mVitrine.id + " - " + mVitrine.name);
    }

    private void observeViewModel(VitrineDetailsViewModel viewModel)
    {
        viewModel.getPicturesListObservable().observe(this, new Observer<List<Picture>>() {
            @Override
            public void onChanged(@Nullable List<Picture> pictures) {

            }
        });
    }
}
