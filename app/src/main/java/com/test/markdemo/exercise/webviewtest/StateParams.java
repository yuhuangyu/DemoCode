package com.test.markdemo.exercise.webviewtest;

/**
 * Created by anye6488 on 2016/9/20.
 */
public class StateParams
{
    public final static String Start = "State-Start";
    public final static String WaitLong = "State-Wait-Long";
    public final static String WaitShort = "State-Wait-Short";
    public final static String Hook = "State-Hook";
    public final static String End = "State-End";

    public final static int TimeoutLong = 1;
    public final static int PageFinished = 2;
    public final static int TimeoutShort = 3;
    public final static int Notify = 4;
    public final static int Close = 5;

    public static String getTypeName(int type)
    {
        switch (type)
        {
            case TimeoutLong:
                return "TimeoutLong";
            case PageFinished:
                return "PageFinished";
            case TimeoutShort:
                return "TimeoutShort";
            case Notify:
                return "Notify";
            case Close:
                return "Close";
        }

        return "";
    }
}
