package com.test.draglistView;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.test.lizizhuangtai.R;

import java.util.ArrayList;
import java.util.Collections;


public class PluginListAdapter extends BaseAdapter {

    public static final int TYPE_PLUGIN_ADDED = 2;

    private Context mContext;
    private ArrayList<DragListViewDemo.PluginItem> mAddedPlugins; // 已添加
    private DragListViewDemo.OnStateBtnClickListener mListener;


    public PluginListAdapter(Context context, ArrayList<DragListViewDemo.PluginItem> added, DragListViewDemo.OnStateBtnClickListener listener) {
        mContext = context;
        mAddedPlugins = added;
        mListener = listener;
    }

    private DragListViewDemo.PluginItem getPlugin(int position) {
        return mAddedPlugins.get(position);
    }

    public boolean isAdded(DragListViewDemo.PluginItem item) {
        return mAddedPlugins.contains(item);
    }

    /**
     * 交换数据的位置
     * @param src
     * @param dst
     * @return
     */
    public boolean exchange(int src, int dst) {
        boolean success = false;
        DragListViewDemo.PluginItem srcItem = getPlugin(src);
        DragListViewDemo.PluginItem dstItem = getPlugin(dst);
        int srcIndex = mAddedPlugins.indexOf(srcItem);
        int dstIndex = mAddedPlugins.indexOf(dstItem);
        if (srcIndex != -1 && dstIndex != -1) {
            Collections.swap(mAddedPlugins, srcIndex, dstIndex);
            success = true;
        }
        if (success) {
            notifyDataSetChanged();
        }
        return success;
    }

    public void removeItem(DragListViewDemo.PluginItem item) {
        mAddedPlugins.remove(item);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mAddedPlugins.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_PLUGIN_ADDED;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case TYPE_PLUGIN_ADDED:
                    convertView = View.inflate(mContext, R.layout.dl_added_plugin_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.mIcon = (ImageView) convertView.findViewById(R.id.dl_plugin_icon);
                    viewHolder.mName = (TextView) convertView.findViewById(R.id.dl_plugin_name);
                    viewHolder.mState = (Button) convertView.findViewById(R.id.dl_plugin_state);
                    viewHolder.mMoveIcon = (ImageView) convertView.findViewById(R.id.dl_plugin_move);
                    convertView.setTag(viewHolder);
                    break;
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DragListViewDemo.PluginItem item = getPlugin(position);

        switch (getItemViewType(position)) {
            case TYPE_PLUGIN_ADDED:
                viewHolder.mName.setText(item.mName);
                viewHolder.mState.setTag(item);
                viewHolder.mState.setOnClickListener(mListener);
                break;
        }
        ((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        return convertView;
    }

    private class ViewHolder {
        public ImageView mIcon;
        public TextView mName;
        public Button mState;
        public ImageView mMoveIcon;
    }
}
