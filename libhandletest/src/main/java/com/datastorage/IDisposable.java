package com.datastorage;

import java.io.Closeable;

/**
 * Created by anye6488 on 2016/5/27.
 */
public interface IDisposable extends Closeable
{
    IDisposable Empty = new IDisposable()
    {
        @Override
        public void close()
        {

        }

        @Override
        public boolean isDispose()
        {
            return false;
        }
    };

    void close();

    boolean isDispose();
}
