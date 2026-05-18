package com.quickreply.wechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.quickreply.wechat.R;
import com.quickreply.wechat.model.Phrase;

import java.util.ArrayList;
import java.util.List;

public class PhraseAdapter extends RecyclerView.Adapter<PhraseAdapter.ViewHolder> {
    
    private Context context;
    private List<Phrase> phrases = new ArrayList<>();
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(Phrase phrase);
        void onItemLongClick(Phrase phrase);
        void onCopyClick(Phrase phrase);
    }
    
    public PhraseAdapter(Context context) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_phrase, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Phrase phrase = phrases.get(position);
        
        holder.tvContent.setText(phrase.getContent());
        holder.tvCategory.setText(phrase.getCategoryName());
        holder.tvUseCount.setText(String.valueOf(phrase.getUseCount()) + "次使用");
        
        // 设置分类颜色
        if (phrase.getColor() != 0) {
            holder.tvCategory.setTextColor(phrase.getColor());
        }
        
        // 点击事件
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(phrase);
            }
        });
        
        holder.cardView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onItemLongClick(phrase);
            }
            return true;
        });
        
        holder.btnCopy.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCopyClick(phrase);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return phrases.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvContent;
        TextView tvCategory;
        TextView tvUseCount;
        ImageButton btnCopy;
        
        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvUseCount = itemView.findViewById(R.id.tv_use_count);
            btnCopy = itemView.findViewById(R.id.btn_copy);
        }
    }
}
