package com.minhtzy.moneytracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    public CategoryIconListAdapter(Context context) {
        this.context = context;
        try {
            imageList = Arrays.asList(context.getAssets().list("category"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(context instanceof OnIconInteractionListener)
        {
            mListener = (OnIconInteractionListener) context;
        }
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