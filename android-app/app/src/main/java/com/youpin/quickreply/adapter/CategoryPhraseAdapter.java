package com.youpin.quickreply.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.youpin.quickreply.R;
import com.youpin.quickreply.model.Category;
import com.youpin.quickreply.model.Phrase;
import java.util.ArrayList;
import java.util.List;

public class CategoryPhraseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private static final int TYPE_CATEGORY_LEVEL1 = 0;  // 一级分类
    private static final int TYPE_CATEGORY_LEVEL2 = 1;  // 二级分类
    private static final int TYPE_PHRASE = 2;           // 话术
    
    private List<Object> items = new ArrayList<>();     // 混合列表
    private OnPhraseClickListener phraseClickListener;
    private OnCategoryClickListener categoryClickListener;
    
    public interface OnPhraseClickListener {
        void onPhraseClick(Phrase phrase);
        void onPhraseLongClick(Phrase phrase);
    }
    
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
        void onCategoryExpandClick(Category category);
    }
    
    public void setOnPhraseClickListener(OnPhraseClickListener listener) {
        this.phraseClickListener = listener;
    }
    
    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.categoryClickListener = listener;
    }
    
    public void setData(List<Category> categories, List<Phrase> phrases) {
        items.clear();
        
        for (Category cat1 : categories) {
            if (cat1.getParentId() == null) {  // 一级分类
                items.add(cat1);
                
                if (cat1.isExpanded()) {
                    // 添加二级分类
                    for (Category cat2 : categories) {
                        if (cat2.getParentId() != null && cat2.getParentId() == cat1.getId()) {
                            items.add(cat2);
                            
                            if (cat2.isExpanded()) {
                                // 添加该二级分类下的话术
                                for (Phrase phrase : phrases) {
                                    if (phrase.getCategoryId() != null && 
                                        phrase.getCategoryId() == cat2.getId()) {
                                        items.add(phrase);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof Category) {
            Category cat = (Category) item;
            return cat.getParentId() == null ? TYPE_CATEGORY_LEVEL1 : TYPE_CATEGORY_LEVEL2;
        }
        return TYPE_PHRASE;
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_CATEGORY_LEVEL1:
                return new Level1ViewHolder(inflater.inflate(R.layout.item_category_level1, parent, false));
            case TYPE_CATEGORY_LEVEL2:
                return new Level2ViewHolder(inflater.inflate(R.layout.item_category_level2, parent, false));
            default:
                return new PhraseViewHolder(inflater.inflate(R.layout.item_phrase_with_attachment, parent, false));
        }
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        
        if (holder instanceof Level1ViewHolder) {
            bindLevel1((Level1ViewHolder) holder, (Category) item);
        } else if (holder instanceof Level2ViewHolder) {
            bindLevel2((Level2ViewHolder) holder, (Category) item);
        } else if (holder instanceof PhraseViewHolder) {
            bindPhrase((PhraseViewHolder) holder, (Phrase) item);
        }
    }
    
    private void bindLevel1(Level1ViewHolder holder, Category category) {
        holder.tvName.setText(category.getName());
        holder.tvCount.setText(String.valueOf(category.getPhraseCount()));
        
        // 设置色条颜色
        if (category.getColor() != null && !category.getColor().isEmpty()) {
            try {
                holder.vColorBar.setBackgroundColor(android.graphics.Color.parseColor(category.getColor()));
            } catch (Exception e) {
                holder.vColorBar.setBackgroundColor(android.graphics.Color.parseColor("#07C160"));
            }
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (categoryClickListener != null) {
                categoryClickListener.onCategoryClick(category);
            }
        });
    }
    
    private void bindLevel2(Level2ViewHolder holder, Category category) {
        holder.tvName.setText(category.getName());
        holder.tvCount.setText(String.valueOf(category.getPhraseCount()));
        holder.ivArrow.setImageResource(category.isExpanded() ? 
            android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        
        holder.itemView.setOnClickListener(v -> {
            if (categoryClickListener != null) {
                categoryClickListener.onCategoryExpandClick(category);
            }
        });
    }
    
    private void bindPhrase(PhraseViewHolder holder, Phrase phrase) {
        holder.tvTitle.setText(phrase.getTitle());
        holder.tvContent.setText(phrase.getContent());
        holder.tvUsage.setText("使用 " + phrase.getUsageCount() + " 次");
        
        // 显示附件图标
        if (phrase.hasAttachment()) {
            holder.ivAttachment.setVisibility(View.VISIBLE);
            if ("image".equals(phrase.getAttachmentType())) {
                holder.ivAttachment.setImageResource(android.R.drawable.ic_menu_gallery);
            } else {
                holder.ivAttachment.setImageResource(android.R.drawable.ic_menu_save);
            }
        } else {
            holder.ivAttachment.setVisibility(View.GONE);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (phraseClickListener != null) {
                phraseClickListener.onPhraseClick(phrase);
            }
        });
        
        holder.itemView.setOnLongClickListener(v -> {
            if (phraseClickListener != null) {
                phraseClickListener.onPhraseLongClick(phrase);
                return true;
            }
            return false;
        });
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    // ViewHolders
    static class Level1ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCount;
        View vColorBar;
        
        Level1ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category_name);
            tvCount = itemView.findViewById(R.id.tv_category_count);
            vColorBar = itemView.findViewById(R.id.v_color_bar);
        }
    }
    
    static class Level2ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCount;
        ImageView ivArrow;
        
        Level2ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category_name);
            tvCount = itemView.findViewById(R.id.tv_category_count);
            ivArrow = itemView.findViewById(R.id.iv_arrow);
        }
    }
    
    static class PhraseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvUsage;
        ImageView ivAttachment;
        
        PhraseViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvUsage = itemView.findViewById(R.id.tv_usage);
            ivAttachment = itemView.findViewById(R.id.iv_attachment);
        }
    }
}