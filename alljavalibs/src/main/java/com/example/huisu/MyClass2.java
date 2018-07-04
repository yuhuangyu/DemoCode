package com.example.huisu;

import java.util.ArrayList;
import java.util.List;

public abstract class MyClass2<TContext, TStrategy2> {
    public static void main(String[] args) {
        SubContext subContext = new SubContext();
        boolean booleen[] = {false,false,false,false,false};
        subContext.flag = booleen;
        subContext.arr= new int[]{1,8,3,9,5};
        subContext.arrNumm = new int[]{-1,-1,-1,-1,-1};

        new ArrangeContext().backtrace(subContext,0);
    }

    public static class SubContext {
        int [] arr;
        int [] arrNumm;
        boolean [] flag;
    }

    public static class ArrangeContext extends MyClass2<SubContext,Integer> {

        @Override
        protected List<Integer> findNextGoal(SubContext context) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < context.arr.length; i++) {
                if (!context.flag[i]) {
                    list.add(i);
                }
            }
            return list;
        }

        @Override
        protected void handle(SubContext context, Integer i, int dp) {
            context.arrNumm[dp] = i;
        }

        @Override
        protected void unmark(SubContext context, Integer i, int dp) {
            context.flag[i] = false;
        }

        @Override
        protected void mark(SubContext context, Integer i, int dp) {
            context.flag[i] = true;
        }

        @Override
        protected boolean isExcepetedGoal(SubContext context, int dp) {
            return context.arr.length == dp && context.arr[context.arrNumm[0]]>-1;
        }

        @Override
        protected void printAllGoal(SubContext context) {
            int[] arr = context.arr;
            int[] arrNumm = context.arrNumm;
            for (int i = 0; i < arrNumm.length; i++) {
                System.out.print(arr[arrNumm[i]]+"  ");
            }
            System.out.println();
        }

    }


//    public abstract class BaseBackTrace<TContext, TStrategy2>{
        public void backtrace(TContext context, int dp){
            if (isExcepetedGoal(context,dp)) {
                printAllGoal(context);
                return;
            }
            List<TStrategy2> nextGoal = findNextGoal(context);
            for (TStrategy2 i : nextGoal) {
                handle(context,i,dp);
                mark(context,i,dp);
                backtrace(context,dp+1);
                unmark(context,i,dp);
            }
        }

        protected abstract List<TStrategy2> findNextGoal(TContext context);

        protected void handle(TContext context, TStrategy2 i, int dp) {

        }

        protected void unmark(TContext context, TStrategy2 i, int dp) {

        }

        protected void mark(TContext context, TStrategy2 i, int dp) {

        }

        protected boolean isExcepetedGoal(TContext context, int dp) {
            return true;
        }

        protected void printAllGoal(TContext context) {

        }
//    }
}

