package com.example.huisu;

import java.util.Arrays;

/**
 * Created by fj on 2018/11/27.
 */

public class MyClass3 {
    /**
     * 回溯法
     *
     */
    static int[] nums = {1,2,3,4,5};
    static int target = 9;
    static int n = nums.length;
    static int[] currentX = new int[n];
    public static void main(String[] args) {
        backTrack(0);
    }

    static int iter = 0;
    static void backTrack(int i ) {
        if(i == n) {
            //结束
            return;
        }
        //求和
        int currentSum = sum(currentX);
        if(currentSum+nums[i] < target) {
            //满足约束条件
            currentX[i] = 1;
            backTrack(i+1);
        }else if(currentSum+nums[i]  == target) {
            //满足约束条件
            currentX[i] = 1;
            System.out.println(Arrays.toString(currentX));
            return;
        }
        //准备进入右子树，不一定能进入
        currentX[i] = 0;
        //不满足约束条件,加入剪枝函数，减低遍历次数
        //设计规则：当前和只加上右子树的最大值都无法达到target，则不用进入右子树
        if(currentSum + bound(i+1) >= target) {
            System.out.println(++iter);
            backTrack(i+1);
        }

    }
    static int bound(int i) {
        int sum = 0;
        for(;i<n;i++) {
            sum+=nums[i];
        }
        return sum;
    }
    static int sum(int[] x) {
        int sum = 0;
        for (int i = 0; i < x.length; i++) {
            if(x[i] == 1) {
                sum+=nums[i];
            }
        }
        return sum;
    }

}
