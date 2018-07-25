package com.example.ObjectPool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by fj on 2018/7/12.
 */

public class ObjectPool<T> {

    private Queue<T> queue = new ConcurrentLinkedQueue<T>();
    private IObject<T> obj;
    private Object objectSy = new Object();
//    private AtomicInteger count = new AtomicInteger();
    private int count = 0;
    private int max;

    public ObjectPool(int max){
        this.max = max;
    }

    public void set(IObject<T> obj){
        this.obj = obj;
    }

    public T obtain() {
        synchronized (objectSy){
            if (queue.size() == 0) {
                if (count < max) {
                    count++;
//                    count.incrementAndGet();
                    return createObject();
                }
            }
            return getObject();
        }
    }

    private T getObject() {
        long time1 = System.currentTimeMillis();
        while(queue.size() == 0 && (System.currentTimeMillis()-time1)<200){
            waitTime(2);
        }
        return queue.poll();
    }

    private T createObject() {
        return obj.setObject();
    }

    public void release(T t){
        if (t != null) {
            queue.offer(t);
        }
    }

    private void waitTime(long delay) {
        synchronized (objectSy) {
            try {
                objectSy.wait(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    interface IObject<T>{
        T setObject();
    }
}
