package com.test.allandroidexamples.dragListViewGridView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;


import com.test.allandroidexamples.R;

import java.util.ArrayList;


public class DragGridDemo extends Activity {

    private DragGridView mListView;
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

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mListView.startDrag(position);
                return true;
            }
        });
        mListView.setDragItemListener(new DragGridView.SimpleAnimationDragItemListener() {
            @Override
            public boolean canExchange(int srcPosition, int position) {
                boolean result = gridAdapter.exchange(srcPosition, position);
                return result;
            }
        });
        gridAdapter = new DragGridAdapter(this, addedItem);
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
}
