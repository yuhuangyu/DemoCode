package com.test.markdemo.exercise.webviewtest;


import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by anye6488 on 2016/9/20.
 */
public class WebStateManager {
    private Map<String, WebState> _stateMap = new HashMap<String, WebState>();
    private static WebStateManager webStateManager;
    private WebState currentWebState;

    private WebStateManager(){}
    public static WebStateManager getWebStateManager(){
        webStateManager = new WebStateManager();
        return webStateManager;
    }

    public static abstract class WebState {
        public abstract void exec(int stata);

        protected abstract void init();

        protected abstract void release();
    }

    public static abstract class WebStateBase extends WebState {
        @Override
        public void exec(int stata) {
            String state = judge(stata);
            if (state != null) {
                Log.i("123","state "+state);
                WebState webState = webStateManager.get(state);
                if (state != null) {
                    release();

                    webState.init();
                }
            }
        }

        protected void init() {
        }

        protected void release() {

        }

        protected abstract String judge(int stata);
    }


    public void define(String name, WebState state) {
        _stateMap.put(name, state);
    }

    public WebState get(String name) {
        currentWebState = _stateMap.get(name);
        return currentWebState;
    }
    public void setInitState(String name) {
        WebState state = get(name);
        if (state != null) {
            state.init();
        }
    }
    public void doExec(int state){
        currentWebState.exec(state);
    }
}
