package com.example.madproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CatagoriesAdapter extends FirebaseRecyclerAdapter<model_catagories, CatagoriesAdapter.adapterViewHolder> {

    private final Context context;

    public CatagoriesAdapter(@NonNull FirebaseRecyclerOptions<model_catagories> options, Context context) {
        super(options);
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    protected void onBindViewHolder(@NonNull adapterViewHolder holder, int position, @NonNull model_catagories modelCatagories) {
        String key = getRef(position).getKey();
        holder.tvcatagories.setText(modelCatagories.getCategory());

        Glide.with(holder.ivphoto.getContext())
                .load(modelCatagories.getImageUrl())
                .into(holder.ivphoto);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, dishes_home.class);
            intent.putExtra("key", key);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public adapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_catagories, parent, false);
        return new adapterViewHolder(v);
    }

    @Override
    public long getItemId(int position) {
        return getRef(position).getKey().hashCode(); // Return a unique ID based on the key
    }

    public static class adapterViewHolder extends RecyclerView.ViewHolder {
        ImageView ivphoto;
        TextView tvcatagories;

        public adapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ivphoto = itemView.findViewById(R.id.ivlogo);
            tvcatagories = itemView.findViewById(R.id.tvcatagories);
        }
    }
}