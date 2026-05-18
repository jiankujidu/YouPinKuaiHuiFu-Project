package com.quickreply.wechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quickreply.wechat.R;
import com.quickreply.wechat.model.Phrase;

import java.util.ArrayList;
import java.util.List;

public class FloatingPhraseAdapter extends RecyclerView.Adapter<FloatingPhraseAdapter.ViewHolder> {
    
    private Context context;
    private List<Phrase> phrases = new ArrayList<>();
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(Phrase phrase);
    }
    
    public FloatingPhraseAdapter(Context context) {
        this.context = context;
    }
    
    public void setPhrases(List<Phrase> phrases) {
        this.phrases = phrases != null ? phrases : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_floating_phrase, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Phrase phrase = phrases.get(position);
        
        holder.tvContent.setText(phrase.getContent());
        holder.tvCategory.setText(phrase.getCategoryName());
        
        // 设置分类颜色
        if (phrase.getColor() != 0) {
            holder.tvCategory.setTextColor(phrase.getColor());
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(phrase);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return phrases.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvCategory;
        
        ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvCategory = itemView.findViewById(R.id.tv_category);
        }
    }
}
