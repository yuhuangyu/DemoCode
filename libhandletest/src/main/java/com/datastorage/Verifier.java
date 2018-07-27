package com.datastorage;



/**
 * Created by anye on 2018/3/27.
 */

public final class Verifier
{
    private static boolean _isVerify = false;

    public static class NotSupportException extends RuntimeException
    {
        public NotSupportException(String s)
        {
            super(s);
        }

        public NotSupportException(String s, Throwable throwable)
        {
            super(s, throwable);
        }

        public NotSupportException(Throwable throwable)
        {
            super(throwable);
        }
    }

    private static boolean isVerify()
    {
        return _isVerify;
    }

    public static <T> void verify(T value, String msg, Iterable<? extends T> iterable)
    {
        if (isVerify())
        {
            for (T obj : iterable)
            {
                if ((obj != null && obj.equals(value)))
                {
                    return;
                }
            }

            throw fillRuntime(new NotSupportException(msg), 3);
        }
        else
        {
            System.out.println(msg);
        }
    }

    private static RuntimeException fillRuntime(RuntimeException e, int len)
    {
        return fill(e, len + 1);
    }

    static <T extends Throwable> T fill(T e, int len) throws T
    {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length - len > 0)
        {
            StackTraceElement[] elements = new StackTraceElement[stackTraceElements.length - len];
            System.arraycopy(stackTraceElements, len, elements, 0, elements.length);
            e.setStackTrace(elements);
        }

        return e;
    }
}
