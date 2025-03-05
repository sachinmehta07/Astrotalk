package com.app.astrotalk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.astrotalk.listeners.OnCategoryItemClick;
import com.app.astrotalk.model.AstrolgerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VedicCategoryAdapter extends RecyclerView.Adapter<VedicCategoryAdapter.ViewHolder> {
    private Context context;

    public List<AstrolgerModel> homeList = new ArrayList<>();
    public List<AstrolgerModel> filterList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<AstrolgerModel> homeList, OnCategoryItemClick onCategoryItemClick) {
        this.homeList = homeList;
        this.onCategoryItemClick = onCategoryItemClick;
        filterList.clear();
        filterList.addAll(this.homeList);
        notifyDataSetChanged();
    }

    private OnCategoryItemClick onCategoryItemClick;

    public VedicCategoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public VedicCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(com.app.astrotalk.databinding.ItemCategoryListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VedicCategoryAdapter.ViewHolder holder, int position) {
        holder.itemCategoryListBinding.txCategoryStickerName.setSelected(true);
        holder.itemCategoryListBinding.txCategoryStickerName.setText(homeList.get(holder.getAdapterPosition()).getName());
        holder.itemCategoryListBinding.ivCategoryStickerImg.setImageResource(homeList.get(holder.getAdapterPosition()).getImageD());

//        Glide.with(context)
//                .load(homeList.get(holder.getAdapterPosition()).getImageD())
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                        return false;
//                    }
//                })
//                .error(R.drawable.ic_launcher_foreground)
//                .into();

        holder.itemCategoryListBinding.clUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryItemClick.onItemClick(holder.getAdapterPosition(), homeList.get(holder.getAdapterPosition()));
            }
        });
     holder.itemCategoryListBinding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryItemClick.onItemClick(holder.getAdapterPosition(), homeList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("msg", "getItemCount: Home List" + homeList.size());
        return homeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        com.app.astrotalk.databinding.ItemCategoryListBinding itemCategoryListBinding;

        public ViewHolder(com.app.astrotalk.databinding.ItemCategoryListBinding itemCategoryListBinding) {
            super(itemCategoryListBinding.getRoot());
            this.itemCategoryListBinding = itemCategoryListBinding;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String charText) {
        this.homeList.clear();
        // Assume no data is found initially
        if (charText.toLowerCase(Locale.getDefault()).isEmpty()) {
            this.homeList.addAll(filterList);
            // Set data found to true when the search text is empty
        } else {
            for (AstrolgerModel wp : filterList) {
                if (wp.getName().toLowerCase().contains(charText.toLowerCase(Locale.getDefault()))) {
                    this.homeList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
