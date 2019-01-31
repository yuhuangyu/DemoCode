package com.test;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by fj on 2019/1/22.
 */

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!scanner.hasNext()) {
                return;
            }

            int[] inputs = new int[10000];
            int index = 0;
            while (true) {
                int input = Integer.parseInt(scanner.next());
                inputs[index++] = input;
                if (index != 0 && (index-1) == inputs[0])
                    break;
            }
            for (int i = 1; i <= inputs[0]; i++) {
                doMath(inputs[i]);
            }


        }

//        long t1 = System.currentTimeMillis();
//        System.out.println(t1);
//        int num1 = 20;
//        int num = 40;
//        doMath(num1);
//        doMath(num);
//        long t2 = System.currentTimeMillis();
//        System.out.println(t2);
//        System.out.println(t2-t1);
    }

    private static void doMath(int num) {
        int [] arr = new int[num+1];
        arr[0] = 0;
        for (int i = 1; i <= num; i++) {
            arr[i] = i;
        }
        int cccc = 0;
        while (cccc== 0 || cccc>3){
            if (cccc != 0 && cccc <= 6) {
                cccc = 0;
                int count = 0;
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] > 0) {
                        count++;
                        cccc++;
                        if (count%2 == 0) {
                            cccc--;
                            arr[i] = 0;
                        }
                    }
                }
                break;
            }
            cccc = 0;
            int count = 0;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] > 0) {
                    count++;
                    cccc++;
                    if (count%2 == 0 || count%6 == 5) {
                        cccc--;
                        arr[i] = 0;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 0) {
                sb.append(i+" ");
            }
        }
        System.out.println(sb.substring(0,sb.length()-1));
    }


}
