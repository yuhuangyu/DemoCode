package com.test.data;

/**
 * Created by fj on 2018/7/27.
 */


public class ResultWrapper<T> {
    private T object;

    public ResultWrapper(T t) {
        this.object = t;
    }

    public ResultWrapper() {

    }

    public void set(T object) {
        this.object = object;
    }

    public T get() {
        return object;
    }
}

