package com.youpin.quickreply.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.youpin.quickreply.R;
import com.youpin.quickreply.model.Phrase;
import java.util.ArrayList;
import java.util.List;

public class FloatingPhraseAdapter extends RecyclerView.Adapter<FloatingPhraseAdapter.ViewHolder> {
    
    private List<Phrase> phrases = new ArrayList<>();
    private OnPhraseClickListener listener;
    
    public interface OnPhraseClickListener {
        void onPhraseClick(Phrase phrase);
    }
    
    public void setOnPhraseClickListener(OnPhraseClickListener listener) {
        this.listener = listener;
    }
    
    public void setPhrases(List<Phrase> phrases) {
        this.phrases = phrases;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_floating_phrase, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Phrase phrase = phrases.get(position);
        holder.tvTitle.setText(phrase.getTitle());
        holder.tvContent.setText(phrase.getContent());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhraseClick(phrase);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return phrases.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        
        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}
