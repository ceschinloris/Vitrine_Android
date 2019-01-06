package com.ceschin.loris.vitrine.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ceschin.loris.vitrine.R;
import com.ceschin.loris.vitrine.model.User;
import com.ceschin.loris.vitrine.model.Vitrine;
import com.ceschin.loris.vitrine.view.adapter.VitrinesAdapter;
import com.ceschin.loris.vitrine.viewmodel.FavoritesViewModel;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private VitrinesAdapter mAdapter;
    private List<Vitrine> mVitrinesList;

    private FavoritesViewModel mViewModel;
    private String mToken;
    private User mUser;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mToken = ((MainActivity) getActivity()).getToken();
        mUser = ((MainActivity) getActivity()).getUser();

        mViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        mViewModel.init(mToken, mUser);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.VitrineListRecyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        mVitrinesList = mViewModel.getVitrineListObservable().getValue();
        mAdapter = new VitrinesAdapter(mVitrinesList);
        mRecyclerView.setAdapter(mAdapter);
        observeViewModel(mViewModel);
    }


    private void observeViewModel(FavoritesViewModel viewModel) {
        viewModel.getVitrineListObservable().observe(this, new Observer<List<Vitrine>>() {
            @Override
            public void onChanged(@Nullable List<Vitrine> vitrines) {
                if(vitrines != null){
                    mVitrinesList = vitrines;
                    mAdapter = new VitrinesAdapter(mVitrinesList);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }

        });
    }
}
