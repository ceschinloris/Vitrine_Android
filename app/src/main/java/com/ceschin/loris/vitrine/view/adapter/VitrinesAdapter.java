package com.ceschin.loris.vitrine.view.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ceschin.loris.vitrine.R;
import com.ceschin.loris.vitrine.model.Vitrine;
import com.ceschin.loris.vitrine.view.MainActivity;
import com.ceschin.loris.vitrine.view.VitrineDetailsActivity;

import java.util.List;

public class VitrinesAdapter extends RecyclerView.Adapter<VitrinesAdapter.MyViewHolder> {

    private List<Vitrine> vitrinesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, name, latitude, longitude, radius;
        protected View mRootView;

        public MyViewHolder(View view) {
            super(view);

            mRootView = view;

            id = (TextView) view.findViewById(R.id.idTextView);
            name = (TextView) view.findViewById(R.id.nameTextView);
            latitude = (TextView) view.findViewById(R.id.latitudeTextView);
            longitude = (TextView) view.findViewById(R.id.longitudeTextView);
            radius = (TextView) view.findViewById(R.id.radiusTextView);
        }
    }

    public VitrinesAdapter(List<Vitrine> vitrines) {
        vitrinesList = vitrines;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_vitrine_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final VitrinesAdapter.MyViewHolder myViewHolder, int i) {
        final Vitrine vitrine = vitrinesList.get(i);
        myViewHolder.id.setText(vitrine.id);
        myViewHolder.name.setText(vitrine.name);
        myViewHolder.latitude.setText(vitrine.latitude.toString());
        myViewHolder.longitude.setText(vitrine.longitude.toString());
        myViewHolder.radius.setText(vitrine.radius.toString());

        myViewHolder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(myViewHolder.mRootView.getContext(), VitrineDetailsActivity.class);
                intent.putExtra("vitrine", vitrine);
                myViewHolder.mRootView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vitrinesList.size();
    }
}
