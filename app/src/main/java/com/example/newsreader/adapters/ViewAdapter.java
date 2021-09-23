package com.example.newsreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsreader.R;
import com.example.newsreader.models.NewsModel;

import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    List<NewsModel> newsModelList;
    Context context;
    OnRowClickListener listener;

    public ViewAdapter(List<NewsModel> newsModelList, Context context, OnRowClickListener listener) {
        this.newsModelList = newsModelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.r_view_template, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iconImage.setImageResource(R.drawable.news_icon);
        holder.titleText.setText(newsModelList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        ImageView iconImage;
        OnRowClickListener listener;
        public ViewHolder(@NonNull View itemView, OnRowClickListener listener) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleTextView);
            iconImage = itemView.findViewById(R.id.iconImage);
            this.listener = listener;
            itemView.setOnClickListener(v -> {
                listener.onClick(getAdapterPosition());
            });
            itemView.setOnLongClickListener(v -> {
                listener.onLongClick(getAdapterPosition());
                return true;
            });
        }
    }

    public interface OnRowClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }
}
