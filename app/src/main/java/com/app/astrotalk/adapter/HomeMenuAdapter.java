package com.app.astrotalk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.astrotalk.databinding.ItemHomeMenuListBinding;
import com.app.astrotalk.listeners.OnMenuItemClickListener;
import com.app.astrotalk.model.HomeMenuModel;

import java.util.ArrayList;

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.ViewHolder>{
    private final ArrayList<HomeMenuModel> homeMenuModelArrayList;

    private final OnMenuItemClickListener mListener;

    public HomeMenuAdapter(ArrayList<HomeMenuModel> homeMenuModelArrayList, OnMenuItemClickListener mListener){
        this.mListener = mListener;
        this.homeMenuModelArrayList = homeMenuModelArrayList;
    }
    @NonNull
    @Override
    public HomeMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemHomeMenuListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMenuAdapter.ViewHolder holder, int position) {
        holder.menuItemBinding.imvOptionMenu.setImageResource(homeMenuModelArrayList.get(holder.getAdapterPosition()).getOptionImg());
        holder.menuItemBinding.txvOptionName.setText(homeMenuModelArrayList.get(holder.getAdapterPosition()).getOptionName());
        holder.menuItemBinding.txvOptionDesc.setText(homeMenuModelArrayList.get(holder.getAdapterPosition()).getOptionDesc());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMenuItemClick(holder.getAdapterPosition(),homeMenuModelArrayList.get(holder.getAdapterPosition()).getOptionName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeMenuModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemHomeMenuListBinding menuItemBinding;
        public ViewHolder(ItemHomeMenuListBinding menuItemBinding) {
            super(menuItemBinding.getRoot());
            this.menuItemBinding = menuItemBinding;
        }
    }
}
