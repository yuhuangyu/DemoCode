package com.test.draglistView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by fj on 2018/7/26.
 */

public class DragGridView extends GridView {

    private Bitmap mBitmap; // 拖拽的itemView图像
    private int mLastX, mLastY;
    private int mDownX, mDownY;
    private float mMoveY, mMoveX;
    private int mDragViewOffsetY;
    private int mDragViewOffsetX;
    private boolean mHasStart = false;
    private View mItemView;
    private int mLastPosition; // 上次的位置，用于判断是否跟当前交换位置
    private int mCurrentPosition; // 手指点击准备拖动的时候,当前拖动项在列表中的位置.
    private DragItemListener mDragItemListener;

    public DragGridView(Context context) {
        super(context);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                if (mBitmap != null) {
                    mLastX = (int) ev.getX();
                    mLastY = (int) ev.getY();
                    stopDrag();
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mBitmap != null) {
                    if (!mHasStart) {
                        if (mItemView != null) { // 隐藏view
                            mItemView.setVisibility(View.INVISIBLE);
                        }
                        mHasStart = true;
                    }
                    mMoveY = ev.getY();
                    mMoveX = ev.getX();
//                    if (moveY < 0) { // 限制触摸范围在ＬｉｓｔＶｉｅｗ中
//                        moveY = 0;
//                    } else if (moveY > getHeight()) {
//                        moveY = getHeight();
//                    }
//                    mMoveY = moveY;
                    onMove((int)mMoveX,(int)mMoveY);
                    mLastY = (int) mMoveY;
                    mLastX = (int) mMoveX;
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_DOWN: // 判断是否进拖拽
                stopDrag();
                mDownX = (int) ev.getX(); // 获取相对与ListView的x坐标
                mDownY = (int) ev.getY(); // 获取相应与ListView的y坐标
                int temp = pointToPosition(mDownX, mDownY);
                if (temp == AdapterView.INVALID_POSITION) { // 无效不进行处理
                    return super.dispatchTouchEvent(ev);
                }
                mLastPosition = mCurrentPosition = temp;

                // 获取当前位置的视图(可见状态)
                ViewGroup itemView = (ViewGroup) getChildAt(mCurrentPosition - getFirstVisiblePosition());

                if (itemView != null && mDragItemListener.canDrag(itemView, mDownX, mDownY)) {

                    // 触摸点在item项中的高度
                    mDragViewOffsetY = mDownY - itemView.getTop();
                    mDragViewOffsetX = mDownX - itemView.getLeft();
                    itemView.setDrawingCacheEnabled(true); // 开启cache.
                    mBitmap = Bitmap.createBitmap(itemView.getDrawingCache()); // 根据cache创建一个新的bitmap对象.
                    itemView.setDrawingCacheEnabled(false);
                    mHasStart = false;
                    mLastY = mDownY;
                    mLastX = mDownX;

                    mItemView = itemView;
                    invalidate();
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void onMove(int mMoveX, int moveY) {
        int endPos = pointToPosition(mMoveX, moveY);
        if (endPos == INVALID_POSITION) { // 当itemView不可见时也会返回-１
            return;
        }
        int mask = mLastPosition > endPos ? -1 : 1;
        // 扫描从mLastPosition到endPos的所有ItemView，交换他们的位置
        // 不能只扫描endPos，否则会在快速拖动的时候跳过中间的itemView
        Log.e("sdk", "============ ");
        for (int i = mLastPosition; mask > 0 ? i <= endPos : i >= endPos; i += mask) {
            int index = i - getFirstVisiblePosition();
            if (index >= getChildCount() || index < 0) {
                continue;
            }
            int x = getChildAt(index).getLeft()+getChildAt(index).getWidth()/2;
            int y = getChildAt(index).getTop()+getChildAt(index).getHeight()/2;
            int tempPosition = pointToPosition(x, y);
            if (tempPosition != INVALID_POSITION) {
                mCurrentPosition = tempPosition;
            }
            if (y < getChildAt(0).getTop()) { // 超出边界处理(如果向上超过第二项Top的话，那么就放置在第一个位置)
                mCurrentPosition = 0;
            } else if (y > getChildAt(getChildCount() - 1).getBottom()) { // // 如果拖动超过最后一项的最下边那么就防止在最下边
                mCurrentPosition = getAdapter().getCount() - 1;
            }

            if (mCurrentPosition != mLastPosition) {
                Log.e("sdk", "mLastPosition "+mLastPosition+"  ,mCurrentPosition  "+mCurrentPosition);
                mDragItemListener.canExchange(mLastPosition, mCurrentPosition);

                View lastView = mItemView;
                mItemView = getChildAt(mCurrentPosition - getFirstVisiblePosition());
                // 通知交换数据成功，可在此时设置交换的动画效果
                mDragItemListener.onExchange(mLastPosition, mCurrentPosition, lastView, mItemView);
                mLastPosition = mCurrentPosition;
            }
        }
        checkScroller(moveY); // listview移动.
    }

    private void checkScroller(int y) {
        int offset = 0;
        Log.e("sdk", "mAutoScrollUpY "+mAutoScrollUpY+" ,mAutoScrollDownY "+mAutoScrollDownY+" ,y "+y);
        if (y < mAutoScrollUpY) { // 拖动到顶部，ListView需要下滑
            offset = dp2px(getContext(), 50); // 滑动的距离
            setSelection(mCurrentPosition-3);
        } else if (y > mAutoScrollDownY) { // 拖动到底部，ListView需要上滑
            offset = -dp2px(getContext(), 50); // 滑动的距离
            setSelection(mCurrentPosition+3);
        }

        /*if (offset != 0) {
//            View view = getChildAt(mCurrentPosition - getFirstVisiblePosition());
//            if (view != null) {
//                // 滚动列表
//                setSelectionFromTop(mCurrentPosition, view.getTop() + offset);
//            }

            smoothScrollBy(offset, 300);
        }*/
    }

    private int mAutoScrollUpY; // 拖动的时候，开始向上滚动的边界
    private int mAutoScrollDownY; // 拖动的时候，开始向下滚动的边界
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mAutoScrollUpY = dp2px(getContext(), 80); // 取得向上滚动的边际，大概为该控件的1/3
        mAutoScrollDownY = h - mAutoScrollUpY; // 取得向下滚动的边际，大概为该控件的2/3
    }

    public int dp2px(Context context, float dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    private void stopDrag() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
            if (mDragItemListener != null) {
                mDragItemListener.onRelease(mCurrentPosition, mItemView, mLastY - mDragViewOffsetY, mLastX, mLastY);
            }
        }
        if (mItemView != null) {
            mItemView = null;
        }
    }



    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // 绘制拖拽的itemView
        if (mBitmap != null && !mBitmap.isRecycled()) {
            canvas.drawBitmap(mBitmap, mLastX-mDragViewOffsetX, mLastY - mDragViewOffsetY, null);
        }
    }

    public void setDragItemListener(DragItemListener listener) {
        mDragItemListener = listener;
    }

    public interface DragItemListener {

        /**
         * 是否进行数据交换
         *
         * @param srcPosition
         * @param position 当前拖拽的view的索引
         * @return 返回true，则确认数据交换;返回false则表示放弃
         */
        boolean canExchange(int srcPosition, int position);

        /**
         * 当完成数据交换时回调
         *
         * @param srcPosition
         * @param position    当前拖拽的view的索引
         * @param srcItemView
         * @param itemView 当前拖拽的view
         */
        void onExchange(int srcPosition, int position, View srcItemView, View itemView);

        /**
         * 释放手指
         *
         * @param position
         */
        void onRelease(int position, View itemView, int itemViewY, int releaseX, int releaseY);

        /**
         * 是否可以拖拽
         *
         * @param itemView
         * @param x        当前触摸的坐标
         * @param y
         * @return
         */
        boolean canDrag(View itemView, int x, int y);

        /**
         * 开始拖拽
         *
         * @param position
         */
        void startDrag(int position, View itemView);

        /**
         * 在生成拖影（itemView.getDrawingCache()）之前
         *
         * @param itemView
         */
        void beforeDrawingCache(View itemView);

        /**
         * 在生成拖影（itemView.getDrawingCache()）之后
         *
         * @param itemView
         * @param bitmap   由itemView.getDrawingCache()生成
         * @return 最终显示的拖影，如果返回为空则使用itemView.getDrawingCache()
         */
        Bitmap afterDrawingCache(View itemView, Bitmap bitmap);
    }

    public static abstract class SimpleAnimationDragItemListener implements DragItemListener {
        @Override
        public void onRelease(int positon, View itemView, int itemViewY, int releaseX, int releaseY) {
            itemView.setVisibility(View.VISIBLE);
        }

        @Override
        public void startDrag(int position, View itemView) {
            if (itemView != null) { // 隐藏view
                itemView.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * 设置交换换动画
         */
        @Override
        public void onExchange(int srcPosition, int position, View srcItemView, View itemView) {
            if (srcItemView != null) {
                srcItemView.setVisibility(View.VISIBLE);
            }
            if (itemView != null) {
                itemView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
