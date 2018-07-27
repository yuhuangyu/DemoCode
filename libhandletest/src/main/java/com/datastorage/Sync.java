package com.datastorage;

public class Sync
{
    public static <T extends Runnable> T run(final T runnable)
    {
        new Thread(runnable).start();

        return runnable;
    }
}
