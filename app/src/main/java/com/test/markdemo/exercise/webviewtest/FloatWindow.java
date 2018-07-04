package com.test.markdemo.exercise.webviewtest;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;

import java.util.Date;

public class FloatWindow
{
    public static final int Left = 0;
    public static final int Right = 1;
    public static final int Top = 2;
    public static final int None = 3;

    public static class ViewInfo
    {
        private static ViewInfo _viewInfo;

        public static ViewInfo get(Context context)
        {
            if (_viewInfo == null)
                _viewInfo = new ViewInfo(context);

            return _viewInfo;
        }

        private Context _context;
        private int _screenW;
        private int _screenH;
        private WindowManager _manager;

        ViewInfo(Context context)
        {
            _context = context;

            _manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            Display display = _manager.getDefaultDisplay();

            if (display.getWidth() < display.getHeight())
            {
                _screenW = display.getWidth();
                _screenH = display.getHeight();
            }
            else
            {
                _screenW = display.getHeight();
                _screenH = display.getWidth();
            }
        }

        public int diptopx(int dp)
        {
            final float scale = _context.getResources().getDisplayMetrics().density;
            return (int) (dp * scale + 0.5f);
        }

        public int diptopx(float dp)
        {
            final float scale = _context.getResources().getDisplayMetrics().density;
            return (int) (dp * scale + 0.5f);
        }

        public int getWidth()
        {
            return _manager.getDefaultDisplay().getWidth();
        }

        public int getWidth(double rate)
        {
            return (int) (_manager.getDefaultDisplay().getWidth() * rate);
        }

        public int getHeight()
        {
            return _manager.getDefaultDisplay().getHeight();
        }

        public int getHeight(double rate)
        {
            return (int) (_manager.getDefaultDisplay().getHeight() * rate);
        }

        public int getScreenWidth()
        {
            return _screenW;
        }

        public int getScreenWidth(double rate)
        {
            return (int) (getScreenWidth() * rate);
        }

        public int getScreenHeight()
        {
            return _screenH;
        }

        public int getScreenHeight(double rate)
        {
            return (int) (getScreenHeight() * rate);
        }

        public int getScaledTouchSlop()
        {
            return ViewConfiguration.get(_context).getScaledTouchSlop();
        }

        public int getStatusBarHeight()
        {
            Resources resources = _context.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        }

        public int getNavigationBarHeight()
        {
            Resources resources = _context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        }

        public boolean checkDeviceHasNavigationBar()
        {
            if (android.os.Build.VERSION.SDK_INT < 14)
                return false;

            boolean hasMenuKey = ViewConfiguration.get(_context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

            if (!hasMenuKey && !hasBackKey)
            {

                return true;
            }
            return false;
        }
    }

    public static class DragAlgorithm
    {
        public static Point fromRaw(MotionEvent event)
        {
            return new Point((int) event.getRawX(), (int) event.getRawY());
        }

        private boolean _isDrag;
        private Point _last;
        private Point _begin;
        private Date _beginTime;

        public DragAlgorithm()
        {
            _last = new Point();
            _begin = new Point();
        }

        public boolean isDrag()
        {
            return _isDrag;
        }

        public Point getBegin()
        {
            return _begin;
        }

        public Point getTogether()
        {
            return new Point(_last.x - _begin.x, _last.y - _begin.y);
        }

        public void onDargBegin(Point point)
        {
            _isDrag = true;
            _last = point;
            _begin = point;

            _beginTime = new Date();
        }

        public long getDragSpan()
        {
            return new Date().getTime() - _beginTime.getTime();
        }

        public Point onDarging(Point point)
        {
            if (_isDrag)
            {
                Point dp = new Point(point.x - _last.x, point.y - _last.y);

                _last = point;
                return dp;
            }

            return new Point();
        }

        public Point onDragEnd(Point point)
        {
            _isDrag = false;

            Point dp = new Point(point.x - _last.x, point.y - _last.y);
            _last = point;

            return dp;
        }

        public void complate()
        {
            _isDrag = false;
        }

        public void reset()
        {
            _isDrag = false;
            _last = new Point();
            _begin = new Point();
        }
    }

    public static abstract class ViewAction
    {
        public abstract void attach(FloatWindow win, View view);

        public abstract void detach();

        public abstract View getView();

        public abstract View getContentView();
    }

    public interface OnWindowListener
    {
        void onClose();
    }

    public static Rect getRect(View view)
    {
        return new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }

    private Context _context;
    private ViewAction _action;
    private WindowManager _manager;
    private LayoutParams _layoutParams;
    private ViewInfo _viewInfo;
    private boolean _isShow = false;
    private boolean _canDrag = false;

    private OnClickListener _clickListener;
    private OnClickListener _outsideListener;
    private OnTouchListener _onViewTouchListener;
    private OnLongClickListener _onLongClickListener;
    private OnWindowListener _onWindowListener;

    public FloatWindow(Context context)
    {
        _context = context;
        _manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        _layoutParams = new LayoutParams();
        _layoutParams.width = LayoutParams.MATCH_PARENT;
        _layoutParams.height = LayoutParams.MATCH_PARENT;

        _layoutParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
        _layoutParams.flags = LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_DISMISS_KEYGUARD;
        _layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        _layoutParams.format = PixelFormat.RGBA_8888;
        _layoutParams.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        _viewInfo = ViewInfo.get(context);
    }

    public void setScreenOrientation(int screenOrientation)
    {
        _layoutParams.screenOrientation = screenOrientation;
    }

    public void setGravity(int gravity)
    {
        _layoutParams.gravity = gravity;
    }

    public void setDimAmount(float d)
    {
        _layoutParams.dimAmount = d;
    }

    public void setScreenBrightness(float b)
    {
        _layoutParams.screenBrightness = b;
    }

    public void setButtonBrightness(float b)
    {
        _layoutParams.buttonBrightness = b;
    }

    public void setType(int type)
    {
        _layoutParams.type = type;
    }

    public void setFlag(int flag)
    {
        _layoutParams.flags = flag;
    }

    ViewInfo getViewInfo()
    {
        return _viewInfo;
    }

    public void setOnWindowListener(OnWindowListener listener)
    {
        _onWindowListener = listener;
    }

    public OnWindowListener getOnWindowListener()
    {
        return _onWindowListener;
    }

    void onWindowClosed()
    {
        if (getOnWindowListener() != null)
            getOnWindowListener().onClose();
    }

    public void setClickOutClose(boolean flag)
    {
        if (flag)
            _layoutParams.flags |= LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        else _layoutParams.flags &= (~LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
    }

    public void setFocusable(boolean flag)
    {
        if (!flag)
            _layoutParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        else _layoutParams.flags &= (~LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    public WindowManager getWindowManager()
    {
        return _manager;
    }

    public Context getContext()
    {
        return _context;
    }

    public void setView(View view)
    {
        if (_action != null)
            _action.detach();

        _action = create(view);

        _action.attach(this, view);
    }

    public int getWidth()
    {
        if (_layoutParams.width == LayoutParams.MATCH_PARENT)
            return getViewInfo().getScreenWidth();

        return _layoutParams.width;
    }

    public int getHeight()
    {
        if (_layoutParams.height == LayoutParams.MATCH_PARENT)
            return _viewInfo.getScreenHeight();

        return _layoutParams.height;
    }

    public void setWidth(int w)
    {
        _layoutParams.width = w;
    }

    public void setHeight(int h)
    {
        _layoutParams.height = h;
    }

    public int getDirection()
    {
        int w = getViewInfo().getScreenWidth();
        int h = getViewInfo().getScreenHeight();
        int touchSlop = getViewInfo().getScaledTouchSlop();
        if (_layoutParams.x <= touchSlop && _layoutParams.y > touchSlop)
            return Left;

        if (_layoutParams.x >= w - getView().getWidth() - touchSlop)
            return Right;

        if (_layoutParams.y <= touchSlop)
            return Top;

        if (_layoutParams.y >= h - touchSlop - getView().getHeight())
            return None;

        return None;
    }

    public View getView()
    {
        if (_action == null)
            return null;

        return _action.getContentView();
    }

    public Rect getRect()
    {
        return new Rect(_layoutParams.x, _layoutParams.y, _layoutParams.x + _layoutParams.width, _layoutParams.y + _layoutParams.height);
    }

    private void setRect(Rect rect)
    {
        _layoutParams.x = rect.left;
        _layoutParams.y = rect.top;
        _layoutParams.width = rect.width();
        _layoutParams.height = rect.height();
    }

    LayoutParams getLayoutParams()
    {
        return _layoutParams;
    }

    public void setOnClickListener(OnClickListener listener)
    {
        _clickListener = listener;
    }

    public OnClickListener getOnClickListener()
    {
        return _clickListener;
    }

    public void setOnOutsideListenter(OnClickListener listener)
    {
        _outsideListener = listener;
    }

    public void setOnLongClickListener(OnLongClickListener listener)
    {
        _onLongClickListener = listener;
    }

    public void setViewTouchListener(OnTouchListener listener)
    {
        _onViewTouchListener = listener;
    }

    public boolean isShowing()
    {
        return _isShow;
    }

    public synchronized void show()
    {
        if (_action != null && !_isShow)
        {
            getWindowManager().addView(_action.getView(), _layoutParams);
            _isShow = true;
        }
    }

    public synchronized void show(Point point)
    {
        if (_action != null && !_isShow)
        {
            _layoutParams.x = point.x;
            _layoutParams.y = point.y;
            getWindowManager().addView(_action.getView(), _layoutParams);
            _isShow = true;
        }
    }

    public void updateTo(Point point)
    {
        if (_action != null && _isShow)
        {
            ValueAnimator animator = new ValueAnimator();
            animator.setDuration(300);
            animator.setObjectValues(new Point(_layoutParams.x, _layoutParams.y), point);
            animator.setEvaluator(new TypeEvaluator<Point>()
            {
                @Override
                public Point evaluate(float value, Point start, Point end)
                {
                    _layoutParams.x = (int) (start.x + (end.x - start.x) * value);
                    _layoutParams.y = (int) (start.y + (end.y - start.y) * value);

                    update();
                    return new Point(_layoutParams.x, _layoutParams.y);
                }
            });

            animator.start();
        }
    }

    public synchronized void update(Rect rect)
    {
        setRect(rect);
        if (_action != null && _isShow)
        {
            getWindowManager().updateViewLayout(_action.getView(), _layoutParams);
        }
    }

    public void canDrag(boolean flag)
    {
        _canDrag = flag;
    }

    public void toLeft()
    {
        _layoutParams.x = 0;

        if (_action != null && _isShow)
        {
            getWindowManager().updateViewLayout(_action.getView(), _layoutParams);
        }
    }

    public void toRight()
    {
        _layoutParams.x = _manager.getDefaultDisplay().getWidth() - getView().getWidth();

        if (_action != null && _isShow)
        {
            getWindowManager().updateViewLayout(_action.getView(), _layoutParams);
        }
    }

    public void toTop()
    {
        _layoutParams.y = 0;

        if (_action != null && _isShow)
        {
            getWindowManager().updateViewLayout(_action.getView(), _layoutParams);
        }
    }

    public synchronized void update(Point point)
    {
        _layoutParams.x = point.x;
        _layoutParams.y = point.y;

        if (_action != null && _isShow)
        {
            getWindowManager().updateViewLayout(_action.getView(), _layoutParams);
        }
    }

    public synchronized void update()
    {
        if (_action != null && _isShow)
        {
            getWindowManager().updateViewLayout(_action.getView(), _layoutParams);
        }
    }

    public int getX()
    {
        return _layoutParams.x;
    }

    public int getY()
    {
        return _layoutParams.y;
    }

    private void removeView()
    {
        try
        {
            getWindowManager().removeView(_action.getView());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void close()
    {
        if (_action != null && _isShow)
        {
            removeView();
            _isShow = false;

            onWindowClosed();
        }
    }

    ViewAction create(View view)
    {
        ViewAction action;

        if (_canDrag)
            action = new DragViewAction();
        else action = new EmptyViewAction();
        return action;
    }

    public class EmptyViewAction extends ViewAction
    {
        private View _view;
        private View _content;

        @Override
        public void attach(FloatWindow win, View view)
        {
            RelativeLayout layout = new RelativeLayout(view.getContext());
            if (view.getLayoutParams() == null)
            {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                layout.addView(view, layoutParams);
            }
            else layout.addView(view);

            _content = view;
            _view = layout;

            _view.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View arg0)
                {
                    if (_clickListener != null)
                        _clickListener.onClick(arg0);
                }
            });

            _view.setOnLongClickListener(new OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    if (_onLongClickListener != null)
                        return _onLongClickListener.onLongClick(view);

                    return false;
                }
            });

            _view.setOnTouchListener(new OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        if (_outsideListener != null)
                            _outsideListener.onClick(v);
                    }

                    return true;
                }
            });
        }

        @Override
        public void detach()
        {

        }

        @Override
        public View getView()
        {
            return _view;
        }

        @Override
        public View getContentView()
        {
            return _content;
        }
    }

    public class DragViewAction extends ViewAction
    {
        private RelativeLayout _view;
        private DragAlgorithm _dragAlgorithm;
        private FloatWindow _win;
        private View _content;
        private boolean _isLongClick = false;
        private int _navBar = 0;
        private int _statusBar = 0;

        private int getX(MotionEvent event)
        {
            return (int) (event.getRawX() - getView().getWidth() / 2);
        }

        private int getY(MotionEvent event)
        {
            return (int) (event.getRawY() - getView().getHeight() / 2);
        }

        private Point getPoint(MotionEvent event)
        {
            return new Point(getX(event), getY(event));
        }

        @Override
        public void attach(FloatWindow win, View view)
        {
            _view = new RelativeLayout(view.getContext());
            _content = view;
            _dragAlgorithm = new DragAlgorithm();
            _win = win;

            _layoutParams.flags |= LayoutParams.FLAG_NOT_TOUCH_MODAL;

            _view.addView(view, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            _view.setLongClickable(true);
            _view.setOnLongClickListener(new OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int touchSlop = ViewConfiguration.get(getView().getContext()).getScaledTouchSlop();
                    if (Math.abs(_dragAlgorithm.getTogether().x) < touchSlop && Math.abs(_dragAlgorithm.getTogether().y) < touchSlop)
                    {
                        _isLongClick = true;
                        if (_onLongClickListener != null)
                            return _onLongClickListener.onLongClick(v);
                    }

                    return false;
                }
            });

            _view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
            {
                @Override
                public void onSystemUiVisibilityChange(int i)
                {
                    if (i == View.SYSTEM_UI_FLAG_VISIBLE)
                    {
                        _statusBar = 0;
                    }
                    else
                    {
                        _statusBar = getViewInfo().getStatusBarHeight();
                    }
                }
            });

            _view.setOnTouchListener(new OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (_onViewTouchListener != null)
                        _onViewTouchListener.onTouch(v, event);

                    Rect viableRect = new Rect();
                    _view.getWindowVisibleDisplayFrame(viableRect);
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            if (getViewInfo().checkDeviceHasNavigationBar())
                                _navBar = getViewInfo().getNavigationBarHeight();
                            else _navBar = 0;

                            _isLongClick = false;
                            _dragAlgorithm.onDargBegin(getPoint(event));

                            // _win.update(new Point(getX(event), getY(event) -
                            // viableRect.top));
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (_dragAlgorithm.isDrag() && !_isLongClick && _canDrag)
                            {
                                Point dp = _dragAlgorithm.onDarging(getPoint(event));
                                _win.update(new Point(_layoutParams.x + dp.x, _layoutParams.y + dp.y));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!_canDrag && !_isLongClick)
                            {
                                if (_clickListener != null)
                                    _clickListener.onClick(getView());
                            }

                            if (_dragAlgorithm.isDrag() && !_isLongClick)
                            {
                                Point dp = _dragAlgorithm.onDragEnd(getPoint(event));

                                int touchSlop = _viewInfo.getScaledTouchSlop();
                                if (Math.abs(_dragAlgorithm.getTogether().x) < touchSlop && Math.abs(_dragAlgorithm.getTogether().y) < touchSlop)
                                {
                                    if (_clickListener != null)
                                        _clickListener.onClick(getView());
                                }

                                int w = getViewInfo().getWidth();
                                int h = viableRect.height();

                                int x = 0;
                                int y = 0;
                                int limit = h / 6;

                                int dy = _layoutParams.y + dp.y;
                                int dx = _layoutParams.x + dp.x;

                                boolean isBottom = dy >= h - getView().getHeight() - limit;
                                boolean isTop = dy <= limit;
                                if (!isBottom && !isTop)
                                {
                                    if (dx < w / 2)
                                        x = 0;
                                    else x = w - _view.getWidth();
                                    y = dy;
                                }
                                else
                                {
                                    x = dx;
                                    if (isBottom)
                                        y = h - _view.getHeight();
                                    else y = 0;// getViewInfo().getScaledTouchSlop();
                                }

                                _win.updateTo(new Point(x, y));
                            }
                            break;
                    }
                    return false;
                }
            });
        }

        @Override
        public void detach()
        {
            _view.setOnTouchListener(null);
        }

        @Override
        public View getView()
        {
            return _view;
        }

        @Override
        public View getContentView()
        {
            return _content;
        }

    }
}
