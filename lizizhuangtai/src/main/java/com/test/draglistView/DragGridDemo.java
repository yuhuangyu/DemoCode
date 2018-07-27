package com.test.draglistView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.test.lizizhuangtai.R;

import java.util.ArrayList;


public class DragGridDemo extends Activity {

    private DragGridView mListView;
    private PluginListAdapter mAdapter;
    private DragGridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);
        init();

        // 模拟数据
        ArrayList<PluginItem> addedItem = new ArrayList<PluginItem>();

        for (int i = 0; i < 20; i++) {
            addedItem.add(new PluginItem("item:" + i));
        }

        mListView.setDragItemListener(new DragGridView.SimpleAnimationDragItemListener() {
            private Rect mFrame = new Rect();
            @Override
            public boolean canExchange(int srcPosition, int position) {
                boolean result = gridAdapter.exchange(srcPosition, position);
                return result;
            }

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
            public void beforeDrawingCache(View itemView) {

            }

            @Override
            public Bitmap afterDrawingCache(View itemView, Bitmap bitmap) {
                return null;
            }
        });
        gridAdapter = new DragGridAdapter(this, addedItem, new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
//                mListView.canDrag(true);
                Log.e("sdk", "=== onLongClick ");
                return true;
            }
        });
        mListView.setAdapter(gridAdapter);
    }

    private void init() {
        mListView = (DragGridView) findViewById(R.id.draglist);
    }

    public static class PluginItem {

        public String mName;

        public PluginItem() {

        }

        public PluginItem(String name) {
            mName = name;
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
