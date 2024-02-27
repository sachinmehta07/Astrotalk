package com.app.astrotalk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.astrotalk.R;
import com.app.astrotalk.listeners.OnCategoryItemClick;
import com.app.astrotalk.listeners.onPoojaItemClick;
import com.app.astrotalk.model.AstrolgerModel;
import com.app.astrotalk.model.PoojaBookModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PoojaBookAdapter extends RecyclerView.Adapter<PoojaBookAdapter.PoojaViewHolder> {

    public List<PoojaBookModel> poojaList;
    private Context context;
    public List<PoojaBookModel> filterList = new ArrayList<>();

    private onPoojaItemClick onCategoryItemClick;

    public PoojaBookAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<PoojaBookModel> poojaList, onPoojaItemClick onCategoryItemClick) {
        this.poojaList = poojaList;
        this.onCategoryItemClick = onCategoryItemClick;
        filterList.clear();
        filterList.addAll(this.poojaList);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public PoojaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pooja, parent, false);
        return new PoojaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoojaViewHolder holder, int position) {
        PoojaBookModel pooja = poojaList.get(position);

        holder.poojaTitle.setText(/*context.getString(R.string.pooja_name_format, context.getString(R.string.pooja_name_label),*/ pooja.getPoojaTitle());
        holder.poojaDesc.setText(pooja.getPoojaDesc());
        holder.poojaImg.setImageResource(pooja.getPoojaImg());

        holder.bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryItemClick.onItemClick(holder.getAdapterPosition(), poojaList.get(holder.getAdapterPosition()));
                // Handle book now button click
            }
        });
    }

    @Override
    public int getItemCount() {
        return poojaList.size();
    }

    public static class PoojaViewHolder extends RecyclerView.ViewHolder {

        ImageView poojaImg;
        TextView poojaTitle, poojaDesc;
        Button bookNowButton;

        public PoojaViewHolder(@NonNull View itemView) {
            super(itemView);
            poojaImg = itemView.findViewById(R.id.pooja_img);
            poojaTitle = itemView.findViewById(R.id.pooja_title);
            poojaDesc = itemView.findViewById(R.id.pooja_desc);
            bookNowButton = itemView.findViewById(R.id.book_now_button);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String charText) {
        this.poojaList.clear();
        // Assume no data is found initially
        if (charText.toLowerCase(Locale.getDefault()).length() == 0) {
            this.poojaList.addAll(filterList);
            // Set data found to true when the search text is empty
        } else {
            for (PoojaBookModel wp : filterList) {
                if (wp.getPoojaName().toLowerCase().contains(charText.toLowerCase(Locale.getDefault()))) {
                    this.poojaList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}