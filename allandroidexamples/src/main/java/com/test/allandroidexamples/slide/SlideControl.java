package com.test.allandroidexamples.slide;

import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by ASUS on 2018/5/14.
 */

public class SlideControl {
    private int lastX;
    private int lastY;
    private int downX;
    private int downY;
    private int menuWidth;
    private boolean isOpen;
    private long lastTime;
    private ViewGroup content;
    private ViewGroup menu;
    private SlideView2 slideView2;
    public SlideControl(SlideView2 slideView2, ViewGroup menu1, ViewGroup content1){
        this.content = content1;
        this.menu = menu1;
        this.slideView2 = slideView2;
    }
    public SlideControl(SlideView2 slideView2, ViewGroup menu1){
        this.menu = menu1;
        this.slideView2 = slideView2;
    }

    public void doSilde(int menuWidth1){

        this.menuWidth = menuWidth1;
        ViewGroup.LayoutParams menuParams = menu.getLayoutParams();
        menuParams.width = menuWidth;
        menu.setTranslationX(-menuWidth);
        menu.setLayoutParams(menuParams);

        slideView2.onSlide(new SlideView2.ISlide() {
            @Override
            public void onDragBegin(int x, int y) {
                downX = x;
                downY = y;
            }

            @Override
            public void onDrag(int x, int y, int lx, int ly, long time) {
                lastX = lx;
                lastY = ly;
                lastTime = time;
                if (!isOpen) {
                    if ((x-downX) < menuWidth) {
                        if ((x - lastX) > (menuWidth/3) && System.currentTimeMillis()-lastTime<500) {
                            openMenu();
                        }else {
                            menu.setTranslationX(x-downX-menuWidth);
                            if (content != null) {
                                content.setTranslationX(x-downX);
                            }
                        }
                    }
                } else {
                    if (x-downX<0) {
                        if ((lastX - x) > (menuWidth/3) && System.currentTimeMillis()-lastTime<500) {
                            closeMenu();
                        }else {
                            menu.setTranslationX(x-downX+0);
                            if (content != null) {
                                content.setTranslationX(x-downX+menuWidth);
                            }
                        }
                    }
                }
            }

            @Override
            public void onDragEnd(int x, int y, int lx, int ly, long time) {
                Log.e("sdk","=="+(x-downX)+" == "+menuWidth/2);
                lastX = lx;
                lastY = ly;
                lastTime = time;
                if (!isOpen) {
                    if ((x - lastX) > (menuWidth/3) && System.currentTimeMillis()-lastTime<500) {
                        openMenu();
                    }else if ((x-downX) < menuWidth/2) {
                        closeMenu();
                    }else {
                        openMenu();
                    }
                } else {
                    if ((lastX - x) > (menuWidth/3) && System.currentTimeMillis()-lastTime<500) {
                        closeMenu();
                    }else if ((downX-x) > menuWidth/2) {
                        closeMenu();
                    }else {
                        openMenu();
                    }
                }
            }
        });
    }

    public void closeMenu() {
        menu.animate().translationX(-menuWidth).setDuration(500).start();
        if (content != null) {
            content.animate().translationX(0).setDuration(500).start();
        }
        isOpen = false;
        slideView2.setIsOpen(false);
    }

    public void openMenu() {
        menu.animate().translationX(0).setDuration(500).start();
        if (content != null) {
            content.animate().translationX(menuWidth).setDuration(500).start();
        }
        isOpen = true;
        slideView2.setIsOpen(true);
    }
    public void setStartLimit(int length){
        slideView2.setStartLimit(length);
    }
}
