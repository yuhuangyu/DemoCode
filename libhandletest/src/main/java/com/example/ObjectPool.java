package com.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by fj on 2018/7/12.
 */

public class ObjectPool<T> {

    private Queue<T> queue = new ConcurrentLinkedQueue<T>();
    private IObject<T> obj;

    public void set(IObject<T> obj){
        this.obj = obj;
    }

    public T obtain() {
        if (queue.size() == 0) {
            return createObject();
        }
        return queue.poll();
    }

    private T createObject() {
        return obj.setObject();
    }

    public void release(T t){
        queue.offer(t);
    }

    interface IObject<T>{
        T setObject();
    }
}
