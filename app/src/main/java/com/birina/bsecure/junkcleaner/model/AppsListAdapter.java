package com.birina.bsecure.junkcleaner.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.birina.bsecure.R;

import java.util.List;

public class AppsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    public static final int VIEW_TYPE_ITEM = 1;


    private List<AppsListItem> mFilteredItems ;


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private ImageView mIcon;
        private TextView mName, mSize;
        private String mPackageName;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            mName = (TextView) itemView.findViewById(R.id.app_name);
            mSize = (TextView) itemView.findViewById(R.id.app_size);

            itemView.setOnClickListener(this);

        }

        public void setIcon(Drawable drawable) {
            mIcon.setImageDrawable(drawable);
        }

        public void setName(String name) {
            mName.setText(name);
        }

        public void setPackageName(String packageName) {
            mPackageName = packageName;
        }

        public void setSize(long size) {
            mSize.setText(Formatter.formatShortFileSize(mSize.getContext(), size));
        }

        @Override
        public void onClick(View view) {
            if (mPackageName != null) {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + mPackageName));

                view.getContext().startActivity(intent);
            }
        }
    }



    public AppsListAdapter( List<AppsListItem> FilteredItems) {
        this.mFilteredItems = FilteredItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.list_item, viewGroup, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            AppsListItem item = mFilteredItems.get(position);

            ((ItemViewHolder) viewHolder).setIcon(item.getApplicationIcon());
            ((ItemViewHolder) viewHolder).setName(item.getApplicationName());
            ((ItemViewHolder) viewHolder).setPackageName(item.getPackageName());
            ((ItemViewHolder) viewHolder).setSize(item.getCacheSize());
    }


    @Override
    public int getItemCount() {
        return mFilteredItems.size();
    }



    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }



  public void updateList(List<AppsListItem> mFilteredItems){
        this.mFilteredItems = mFilteredItems;

        notifyDataSetChanged();
  }
}
