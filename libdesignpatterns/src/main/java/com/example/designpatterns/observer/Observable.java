package com.example.designpatterns.observer;

import java.util.ArrayList;

/**
 * Created by ASUS on 2018/5/25.
 * 观察者模式
 */

public class Observable<T> {

    ArrayList<Observe<T>> list = new ArrayList<>();

    public void register(Observe observe){
        if (observe != null) {
            synchronized (this) {
                if (!list.contains(observe))
                    list.add(observe);
            }
        }
    }

    public synchronized void unRegister(Observe observe){
        list.remove(observe);
    }

    public void notifyObserve(T t){
        for (int i = 0; i < list.size(); i++) {
            list.get(i).upData(t);
        }
    }
    public interface Observe<T> {
        void upData(T t);
    }
}
