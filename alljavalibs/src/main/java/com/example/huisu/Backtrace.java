package com.example.huisu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by anye on 2018/3/14.
 */

public abstract class Backtrace<TContext, TStrategy>
{
    public static void main(String... args)
    {
//        SubsetContext context = new SubsetContext(new int[]{1, 2, 3, 4, 5, 6, 7});
//        new SubsetBacktrace().backtrace(context, 0);

        ArrangeContext acontext = new ArrangeContext(new int[]{1, 2, 3, 4, 5, 6, 7});
        new ArrangeBacktrace().backtrace(acontext, 0);
    }

    public void backtrace(TContext context, int dp)
    {
        if (isExpectedGoal(context, dp))
        {
            process(context);
            return;
        }

        Iterable<TStrategy> strategies = feasibleStrategy(context);

        for (TStrategy strategy : strategies)
        {
            handle(context, dp, strategy);
            mark(context, strategy);
            backtrace(context, dp + 1);
            unmark(context, strategy);
        }
    }

    protected void handle(TContext context, int dp, TStrategy strategy)
    {

    }

    protected void mark(TContext context, TStrategy strategy)
    {

    }

    protected void unmark(TContext context, TStrategy strategy)
    {

    }

    protected abstract Iterable<TStrategy> feasibleStrategy(TContext context);

    protected boolean isExpectedGoal(TContext context, int dp)
    {
        return true;
    }

    protected void process(TContext context)
    {

    }

    public static class SubsetContext
    {
        public int[] inputs;
        public boolean[] flags;

        public SubsetContext(int[] inputs)
        {
            this.inputs = inputs;
            flags = new boolean[inputs.length];
        }
    }

    public static class SubsetBacktrace extends Backtrace<SubsetContext, Boolean>
    {
        @Override
        protected Iterable<Boolean> feasibleStrategy(SubsetContext context)
        {
            return Arrays.asList(new Boolean[]{true, false});
        }

        @Override
        protected void handle(SubsetContext context, int dp, Boolean aBoolean)
        {
            context.flags[dp] = aBoolean;
        }

        @Override
        protected boolean isExpectedGoal(SubsetContext context, int dp)
        {
            return dp == context.inputs.length;
        }

        @Override
        protected void process(SubsetContext context)
        {
            for (int i = 0; i < context.inputs.length; i++)
            {
                if (context.flags[i])
                {
                    System.out.print(context.inputs[i] + " ");
                }
            }

            System.out.println();
        }
    }

    public static class ArrangeContext
    {
        public int[] inputs;
        public int[] arrange;
        public boolean[] flags;

        public ArrangeContext(int[] inputs)
        {
            this.inputs = inputs;
            arrange = new int[inputs.length];
            flags = new boolean[inputs.length];
        }
    }

    public static class ArrangeBacktrace extends Backtrace<ArrangeContext, Integer>
    {
        @Override
        protected Iterable<Integer> feasibleStrategy(ArrangeContext context)
        {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < context.inputs.length; i++)
            {
                if (!context.flags[i])
                    list.add(i);
            }

            return list;
        }

        @Override
        protected void mark(ArrangeContext arrangeContext, Integer integers)
        {
            arrangeContext.flags[integers] = true;
        }

        @Override
        protected void unmark(ArrangeContext arrangeContext, Integer integers)
        {
            arrangeContext.flags[integers] = false;
        }

        @Override
        protected void handle(ArrangeContext context, int dp, Integer i)
        {
            context.arrange[dp] = context.inputs[i];
        }

        @Override
        protected boolean isExpectedGoal(ArrangeContext context, int dp)
        {
            return dp == context.inputs.length;
        }

        @Override
        protected void process(ArrangeContext context)
        {
            for (int i = 0; i < context.arrange.length; i++)
            {
                System.out.print(context.arrange[i] + " ");
            }

            System.out.println();
        }
    }
}
