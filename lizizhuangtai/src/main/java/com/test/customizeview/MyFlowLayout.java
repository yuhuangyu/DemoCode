package com.test.customizeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

/**
 * Created by fj on 2018/10/30.
 */

public class MyFlowLayout extends ViewGroup {
    private ArrayList<Line> lineList = new ArrayList<>();
    /**
     * childVIew之间的水平间隔
     */
    private int childViewHorizontalPadding = 0;

    /**
     * childView之间的垂直间隔
     */

    private int childViewVerticalPadding = 0;


    public MyFlowLayout(Context context) {

        super(context);

    }


    public MyFlowLayout(Context context, AttributeSet attrs) {

        super(context, attrs);

    }


    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 很多情况下onMeasure方法都会被执行，要确保集合为空，才不会影响以下代码的执行结果。

        lineList.clear();


        // 获取flowLayout的宽度，并且去掉padding

        int width = MeasureSpec.getSize(widthMeasureSpec);

        int contentWidth = width - getPaddingLeft() - getPaddingRight();


        // 遍历flowLayout的子控件,创建line的实例

        Line line = new Line();

        for (int i = 0; i < getChildCount(); i++) {

            View childView = getChildAt(i);

            childView.measure(0, 0);// 确保能得到控件的宽高

            // 如果Line为空，则直接添加，保证每行至少有一个view

            if (line.getList().size() == 0) {

                line.addLineView(childView);

            } else if (line.getWidth() + childViewHorizontalPadding

                    + childView.getMeasuredWidth() > contentWidth) {

                // 如果line的宽度加上子控件的宽度大于flowLayout的容量宽度，则重新创建一行

                lineList.add(line);

                line = new Line();

                line.addLineView(childView);

            } else {

                line.addLineView(childView);

            }

        }

        // 保存最后一个line，避免丢失

        lineList.add(line);


        // 计算flowLayout的高度

        int height = getPaddingTop() + getPaddingBottom()

                + (lineList.size() - 1) * childViewVerticalPadding;// 先把所有的间距相加

        for (Line l : lineList) {

            // 加入每个line的高度

            height += l.getHeight();

        }


        // 设置当前控件的宽高

        setMeasuredDimension(width, height);

    }


    @Override

    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        int left = 0;

        int top = getPaddingTop();

        int right = 0;

        int bottom = 0;


        // 遍历line集合

        for (int k = 0; k < lineList.size(); k++) {

            // 获取到line的实例

            Line line = lineList.get(k);


            // 得到当前line的left和top

            if (k > 0) {

                top += lineList.get(k - 1).getHeight() + childViewVerticalPadding;

            }


            ArrayList<View> viewlist = line.getList(); //   获取line中view的集合


            // 计算每个childView需要填充的宽度

            int perSpacing = 0;

            if (viewlist.size() > 0) {

                perSpacing = getLineRemainSpacing(line) / viewlist.size();

            }


            for (int i = 0; i < viewlist.size(); i++) { //  遍历view的集合

                View childView = viewlist.get(i);


                // 重置控件的宽度，填充留白的部分

                int measureWidth = MeasureSpec.makeMeasureSpec(

                        childView.getMeasuredWidth() + perSpacing,

                        MeasureSpec.EXACTLY);

                childView.measure(measureWidth, 0);


                // 重置line的宽度

                line.reSetWidth();


                if (i == 0) {  //   第一个view靠左摆放

                    left = getPaddingLeft();

                    right = left + childView.getMeasuredWidth();

                    bottom = top + childView.getMeasuredHeight();

                    childView.layout(left, top, right, bottom);

                } else {    //  其余的view参考前一个view的位置摆放

                    // 当前view的left是前一个view的left + 水平间距

                    View preView = viewlist.get(i - 1);

                    left = preView.getRight() + childViewHorizontalPadding;

                    right = left + childView.getMeasuredWidth();

                    bottom = top + childView.getMeasuredHeight();

                    childView.layout(left, top, right, bottom);

                }

            }

        }

    }


    private int getLineRemainSpacing(Line line) {

        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - line.getWidth();

    }


    /**
     * 设置子控件的水平间距
     *
     * @param childViewHorizontalPadding 子控件的间距
     */

    public void setChildViewHorizontalPadding(int childViewHorizontalPadding) {

        this.childViewHorizontalPadding = childViewHorizontalPadding;

    }


    /**
     * 设置line的垂直间距
     *
     * @param childViewVerticalPadding 垂直间距
     */

    public void setChildViewVerticalPadding(int childViewVerticalPadding) {

        this.childViewVerticalPadding = childViewVerticalPadding;

    }


    /**
     * 一行的封装类
     */

    public class Line {


        private ArrayList<View> viewListOfLine = new ArrayList<>();


        /**
         * 当前行宽度
         */

        private int width = 0;


        /**
         * 当前行高度
         */

        private int height = 0;


        public void addLineView(View childView) {

            if (!viewListOfLine.contains(childView)) {

                viewListOfLine.add(childView);


                // 设置当前行的宽度，如果为第一个childView，则不添加childadding

                if (viewListOfLine.size() == 1) {

                    width = childView.getMeasuredWidth();

                } else {

                    width += childView.getMeasuredWidth() + childViewHorizontalPadding;

                }


                // 设置当前行的高度，总是取最大值

                height = Math.max(height, childView.getMeasuredHeight());

            }

        }


        /**
         * 获取当前行的宽度
         *
         * @return width
         */

        public int getWidth() {

            return width;

        }


        /**
         * 获取当前行的高度
         *
         * @return height
         */

        public int getHeight() {

            return height;

        }


        /**
         * 获取控件的集合
         *
         * @return 控件的集合
         */

        public ArrayList<View> getList() {

            return viewListOfLine;

        }


        /**
         * 重新计算line的宽度，在其子控件的宽度发生变化后使用。
         */

        public void reSetWidth() {

            width = 0;

            for (View view : viewListOfLine) {

                width += view.getMeasuredWidth();

            }

            width += (viewListOfLine.size() - 1) * childViewHorizontalPadding;

        }

    }

}
