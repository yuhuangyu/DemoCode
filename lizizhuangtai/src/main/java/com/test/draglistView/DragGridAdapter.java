package com.test.draglistView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.lizizhuangtai.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by fj on 2018/7/26.
 */

public class DragGridAdapter extends BaseAdapter{

    public static final int TYPE_PLUGIN_ADDED = 2;
    private Context mContext;
    private View.OnLongClickListener mListener;
    private ArrayList<DragGridDemo.PluginItem> mAddedPlugins; // 已添加

    public DragGridAdapter(Context context, ArrayList<DragGridDemo.PluginItem> added, View.OnLongClickListener listener){
        mContext = context;
        mAddedPlugins = added;
        mListener = listener;
    }

    private DragGridDemo.PluginItem getPlugin(int position) {
        return mAddedPlugins.get(position);
    }

    @Override
    public int getCount() {
        return mAddedPlugins.size();
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
    public int getItemViewType(int position) {
        return TYPE_PLUGIN_ADDED;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DragGridAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case TYPE_PLUGIN_ADDED:
                    convertView = View.inflate(mContext, R.layout.dl_added_plugin_item2, null);
                    viewHolder = new DragGridAdapter.ViewHolder();
                    viewHolder.mName = (TextView) convertView.findViewById(R.id.dl_plugin_name);
                    viewHolder.mMoveIcon = (ImageView) convertView.findViewById(R.id.dl_plugin_move);
                    convertView.setTag(viewHolder);
                    break;
            }
        } else {
            viewHolder = (DragGridAdapter.ViewHolder) convertView.getTag();
        }

        DragGridDemo.PluginItem item = getPlugin(position);

        switch (getItemViewType(position)) {
            case TYPE_PLUGIN_ADDED:
                viewHolder.mName.setText(item.mName);
                convertView.setOnLongClickListener(mListener);
                break;
        }
        ((ViewGroup) convertView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        return convertView;
    }

    public boolean exchange(int src, int dst) {
        boolean success = false;
        DragGridDemo.PluginItem srcItem = getPlugin(src);
        DragGridDemo.PluginItem dstItem = getPlugin(dst);
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

    private class ViewHolder {
        public TextView mName;
        public ImageView mMoveIcon;
    }
}
