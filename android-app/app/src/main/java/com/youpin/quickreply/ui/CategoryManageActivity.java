package com.youpin.quickreply.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.youpin.quickreply.R;
import com.youpin.quickreply.database.AppDatabase;
import com.youpin.quickreply.model.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryManageActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private TextView tvEmpty;
    private Button btnAddLevel1;
    
    private ExecutorService executor;
    private String type = "company";
    private List<Category> categories = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manage);
        
        executor = Executors.newSingleThreadExecutor();
        type = getIntent().getStringExtra("type");
        if (type == null) type = "company";
        
        initViews();
        setupListeners();
        loadCategories();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        tvEmpty = findViewById(R.id.tv_empty);
        btnAddLevel1 = findViewById(R.id.btn_add_level1);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter();
        recyclerView.setAdapter(adapter);
    }
    
    private void setupListeners() {
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        
        btnAddLevel1.setOnClickListener(v -> {
            showAddCategoryDialog(null);
        });
        
        adapter.setOnCategoryClickListener(category -> {
            if (category.getLevel() == 1) {
                // 一级分类，显示添加二级分类选项
                showCategoryOptions(category);
            }
        });
        
        adapter.setOnCategoryLongClickListener(category -> {
            showEditDeleteOptions(category);
            return true;
        });
    }
    
    private void loadCategories() {
        executor.execute(() -> {
            try {
                categories = AppDatabase.getInstance(this).categoryDao().getCategoriesByType(type);
                
                runOnUiThread(() -> {
                    adapter.setCategories(categories);
                    tvEmpty.setVisibility(categories.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void showAddCategoryDialog(Category parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(parent == null ? "添加一级分类" : "添加二级分类");
        
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        EditText etName = view.findViewById(R.id.et_name);
        
        if (parent != null) {
            TextView tvParent = view.findViewById(R.id.tv_parent);
            tvParent.setText("父分类: " + parent.getName());
            tvParent.setVisibility(View.VISIBLE);
        }
        
        builder.setView(view);
        builder.setPositiveButton("添加", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "请输入分类名称", Toast.LENGTH_SHORT).show();
                return;
            }
            
            addCategory(name, parent);
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    
    private void addCategory(String name, Category parent) {
        executor.execute(() -> {
            try {
                Category category = new Category();
                category.setName(name);
                category.setType(type);
                category.setLevel(parent == null ? 1 : 2);
                category.setParentId(parent == null ? 0 : parent.getId());
                category.setColor(parent == null ? generateColor() : parent.getColor());
                
                AppDatabase.getInstance(this).categoryDao().insert(category);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                    loadCategories();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    private void showCategoryOptions(Category category) {
        new AlertDialog.Builder(this)
            .setTitle(category.getName())
            .setItems(new String[]{"添加子分类", "编辑", "删除"}, (dialog, which) -> {
                if (which == 0) {
                    showAddCategoryDialog(category);
                } else if (which == 1) {
                    showEditDialog(category);
                } else if (which == 2) {
                    confirmDelete(category);
                }
            })
            .show();
    }
    
    private void showEditDeleteOptions(Category category) {
        new AlertDialog.Builder(this)
            .setTitle(category.getName())
            .setItems(new String[]{"编辑", "删除"}, (dialog, which) -> {
                if (which == 0) {
                    showEditDialog(category);
                } else if (which == 1) {
                    confirmDelete(category);
                }
            })
            .show();
    }
    
    private void showEditDialog(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑分类");
        
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);
        EditText etName = view.findViewById(R.id.et_name);
        etName.setText(category.getName());
        
        builder.setView(view);
        builder.setPositiveButton("保存", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "请输入分类名称", Toast.LENGTH_SHORT).show();
                return;
            }
            
            updateCategory(category, name);
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    
    private void updateCategory(Category category, String newName) {
        executor.execute(() -> {
            try {
                category.setName(newName);
                AppDatabase.getInstance(this).categoryDao().update(category);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                    loadCategories();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void confirmDelete(Category category) {
        new AlertDialog.Builder(this)
            .setTitle("确认删除")
            .setMessage("确定要删除分类 \"" + category.getName() + "\" 吗？\n\n注意：删除分类将同时删除该分类下的所有子分类和话术！")
            .setPositiveButton("删除", (dialog, which) -> {
                deleteCategory(category);
            })
            .setNegativeButton("取消", null)
            .show();
    }
    
    private void deleteCategory(Category category) {
        executor.execute(() -> {
            try {
                AppDatabase.getInstance(this).categoryDao().delete(category);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                    loadCategories();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    private String generateColor() {
        String[] colors = {"#07C160", "#1890FF", "#FA8C16", "#F5222D", "#722ED1", "#13C2C2"};
        return colors[(int)(Math.random() * colors.length)];
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
    
    // 分类适配器
    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
        
        private List<Category> data = new ArrayList<>();
        private OnCategoryClickListener clickListener;
        private OnCategoryLongClickListener longClickListener;
        
        public void setCategories(List<Category> categories) {
            this.data = categories;
            notifyDataSetChanged();
        }
        
        public void setOnCategoryClickListener(OnCategoryClickListener listener) {
            this.clickListener = listener;
        }
        
        public void setOnCategoryLongClickListener(OnCategoryLongClickListener listener) {
            this.longClickListener = listener;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_manage, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Category category = data.get(position);
            holder.tvName.setText(category.getName());
            
            if (category.getLevel() == 1) {
                holder.tvLevel.setText("一级");
                holder.tvLevel.setBackgroundColor(Color.parseColor(category.getColor()));
                holder.tvLevel.setTextColor(Color.WHITE);
                holder.viewIndent.setVisibility(View.GONE);
            } else {
                holder.tvLevel.setText("二级");
                holder.tvLevel.setBackgroundColor(Color.LTGRAY);
                holder.tvLevel.setTextColor(Color.DKGRAY);
                holder.viewIndent.setVisibility(View.VISIBLE);
            }
            
            holder.itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onClick(category);
                }
            });
            
            holder.itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    return longClickListener.onLongClick(category);
                }
                return false;
            });
        }
        
        @Override
        public int getItemCount() {
            return data.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvLevel;
            View viewIndent;
            
            public ViewHolder(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
                tvLevel = itemView.findViewById(R.id.tv_level);
                viewIndent = itemView.findViewById(R.id.view_indent);
            }
        }
    }
    
    interface OnCategoryClickListener {
        void onClick(Category category);
    }
    
    interface OnCategoryLongClickListener {
        boolean onLongClick(Category category);
    }
}