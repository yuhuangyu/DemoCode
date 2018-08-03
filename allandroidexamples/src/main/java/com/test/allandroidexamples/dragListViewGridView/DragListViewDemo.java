package com.test.allandroidexamples.dragListViewGridView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


import com.test.allandroidexamples.R;

import java.util.ArrayList;


public class DragListViewDemo extends Activity {

    private DragListView mListView;
    private PluginListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        init();

        mListView.setDragItemListener(new DragListView.SimpleAnimationDragItemListener() {

            private Rect mFrame = new Rect();
            private boolean mIsSelected;

            @Override
            public boolean canDrag(View dragView, int x, int y) {
                // 获取可拖拽的图标
                View dragger = dragView.findViewById(R.id.dl_plugin_move);
                if (dragger == null || dragger.getVisibility() != View.VISIBLE) {
                    return false;
                }
                float tx = x - getX(dragView);
                float ty = y - getY(dragView);
                dragger.getHitRect(mFrame);
                if (mFrame.contains((int) tx, (int) ty)) { // 当点击拖拽图标才可进行拖拽
                    return true;
                }
                return false;
            }


            @Override
            public void beforeDrawingCache(View dragView) {
                mIsSelected = dragView.isSelected();
                View drag = dragView.findViewById(R.id.dl_plugin_move);
                dragView.setSelected(true);
                if (drag != null) {
                    drag.setSelected(true);
                }
            }

            @Override
            public Bitmap afterDrawingCache(View dragView, Bitmap bitmap) {
                dragView.setSelected(mIsSelected);
                View drag = dragView.findViewById(R.id.dl_plugin_move);
                if (drag != null) {
                    drag.setSelected(false);
                }
                return bitmap;
            }

            @Override
            public boolean canExchange(int srcPosition, int position) {
                boolean result = mAdapter.exchange(srcPosition, position);
                return result;
            }

        });

        // 模拟数据
        ArrayList<PluginItem> addedItem = new ArrayList<PluginItem>();

        for (int i = 0; i < 20; i++) {
            addedItem.add(new PluginItem("item:" + i));
        }

        mAdapter = new PluginListAdapter(this, addedItem, new OnStateBtnClickListener());
        mListView.setAdapter(mAdapter);
    }

    private void init() {
        mListView = (DragListView) findViewById(R.id.draglist);
    }

    public static class PluginItem {

        public String mName;

        public PluginItem() {

        }

        public PluginItem(String name) {
            mName = name;
        }


    }

    /**
     * 点击已添加／未添加按钮
     */
    public class OnStateBtnClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View v) {
            PluginItem item = (PluginItem) v.getTag();
            if (item == null) {
                return;
            }
            if (mAdapter.isAdded(item)) {
                mAdapter.removeItem(item);
            }
        }
    }

    public float getX(View view) {
        if (Build.VERSION.SDK_INT >= 11) {
            return view.getX();
        } else {
            return view.getLeft() + view.getTranslationX();
        }
    }

    public float getY(View view) {
        if (Build.VERSION.SDK_INT >= 11) {
            return view.getY();
        } else {
            return view.getTop() + view.getTranslationY();
        }
    }
}
