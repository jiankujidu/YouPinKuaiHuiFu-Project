package com.quickreply.wechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quickreply.wechat.R;
import com.quickreply.wechat.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    
    private Context context;
    private List<Category> categories = new ArrayList<>();
    private OnItemClickListener clickListener;
    private OnDeleteClickListener deleteListener;
    
    public interface OnItemClickListener {
        void onItemClick(Category category);
    }
    
    public interface OnDeleteClickListener {
        void onDeleteClick(Category category);
    }
    
    public CategoryAdapter(Context context) {
        this.context = context;
    }
    
    public void setCategories(List<Category> categories) {
        this.categories = categories != null ? categories : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }
    
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);
        
        holder.tvName.setText(category.getName());
        holder.tvType.setText(category.getTypeDisplayName());
        holder.tvCount.setText(category.getPhraseCount() + "条话术");
        
        // 设置颜色标识
        if (category.getColor() != 0) {
            holder.colorIndicator.setBackgroundColor(category.getColor());
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(category);
            }
        });
        
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(category);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return categories.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        View colorIndicator;
        TextView tvName;
        TextView tvType;
        TextView tvCount;
        ImageButton btnDelete;
        
        ViewHolder(View itemView) {
            super(itemView);
            colorIndicator = itemView.findViewById(R.id.color_indicator);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
            tvCount = itemView.findViewById(R.id.tv_count);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
