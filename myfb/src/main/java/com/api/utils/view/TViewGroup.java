package com.api.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by fj on 2018/10/24.
 */

public class TViewGroup extends ViewGroup {
    public TViewGroup(Context context) {
        super(context);
    }

    public TViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
