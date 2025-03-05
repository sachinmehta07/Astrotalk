package com.app.astrotalk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.astrotalk.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatModelViewHolder> {
    private List<String> chatList;
    private Context context;

    public ChatAdapter(Context context) {
        this.context = context;
        this.chatList = new ArrayList<>();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setChatList(List<String> chatList) {
        this.chatList = chatList;
        notifyDataSetChanged();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void addMessage(String message) {
        chatList.add(message);
        notifyDataSetChanged();
    }

    public List<String> getChatList() {
        return chatList;
    }


    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position) {
        String message = chatList.get(position);

        if (isUserMessage(position)) {
            // User's message
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextview.setText(message);
        } else {
            // App's response
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatTextview.setText(message);
        }
    }
    private boolean isUserMessage(int position) {
        // Check if the message at the given position is from the user
        return position % 2 == 0;
    }
    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatModelViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextview, rightChatTextview;

        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextview = itemView.findViewById(R.id.right_chat_textview);

        }
    }
}
