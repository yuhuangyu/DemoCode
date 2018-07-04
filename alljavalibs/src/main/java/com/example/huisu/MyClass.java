package com.example.huisu;

import java.util.ArrayList;
import java.util.List;

public class MyClass {
    public static void main(String[] args) {
        SubContext subContext = new SubContext();
        boolean booleen[] = {false,false,false,false,false};
        subContext.flag = booleen;
        subContext.arr= new int[]{1,8,3,9,5};
        subContext.arrNumm = new int[]{-1,-1,-1,-1,-1};

        new MyClass().backtrace(subContext,0);
    }
    public void backtrace(SubContext context, int dp){
        if (isExcepetedGoal(context,dp)) {
            printAllGoal(context);
            return;
        }
        List<Integer> nextGoal = findNextGoal(context);
        for (int i : nextGoal) {
            handle(context,i,dp);
            mark(context,i,dp);
            backtrace(context,dp+1);
            unmark(context,i,dp);
        }
    }

    private void handle(SubContext context, int i, int dp) {
        context.arrNumm[dp] = i;
    }

    private void unmark(SubContext context, int i, int dp) {
        context.flag[i] = false;
    }

    private void mark(SubContext context, int i, int dp) {
        context.flag[i] = true;
    }

    private List<Integer> findNextGoal(SubContext context) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < context.arr.length; i++) {
            if (!context.flag[i]) {
                list.add(i);
            }
        }
        return list;
    }

    private void printAllGoal(SubContext context) {
        int[] arr = context.arr;
        int[] arrNumm = context.arrNumm;
        for (int i = 0; i < arrNumm.length; i++) {
            System.out.print(arr[arrNumm[i]]+"  ");
        }
        System.out.println();
    }

    public boolean isExcepetedGoal(SubContext context, int dp) {
        return context.arr.length == dp && context.arr[context.arrNumm[0]]>-1;
    }

    public static class SubContext {
        int [] arr;
        int [] arrNumm;
        boolean [] flag;
    }
}

