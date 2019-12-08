package com.minhtzy.moneytracker.view.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.utilities.ResourceUtils;
import com.minhtzy.moneytracker.view.OnIconInteractionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CategoryIconListAdapter extends RecyclerView.Adapter<CategoryIconListAdapter.GridItemViewHolder> {

    private List<String> imageList;

    private Context context;

    OnIconInteractionListener mListener;

    public class GridItemViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public GridItemViewHolder(View view) {
            super(view);
            mImageView = view.findViewById(R.id.icon);
        }
    }

    public CategoryIconListAdapter(Context context,OnIconInteractionListener listener,String folder) {
        this.context = context;
        try {
            imageList = Arrays.asList(context.getAssets().list(folder));
        } catch (IOException e) {
            e.printStackTrace();
        }
         mListener = listener;
    }

    @Override
    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);

        return new GridItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GridItemViewHolder holder, int position) {
        final String path = imageList.get(position);

        holder.mImageView.setImageDrawable(ResourceUtils.getCategoryIcon(path));

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle click event on image
                mListener.onCategoryIconClick(path);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}