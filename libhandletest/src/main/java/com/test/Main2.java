package com.test;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by fj on 2019/1/23.
 */

public class Main2 {

    public static void main(String[] args) {
//        int[] arr = new int[]{2,3,2,4,7,10,4};
//        String[] arr2 = new String[]{"一","二","三","四","五","六","七"};
//
//        int i = get(arr);
//        System.out.println(""+arr2[i]);

        Scanner scanner = new Scanner(System.in);
//        while (true) {
            if (!scanner.hasNext()) {
                return;
            }

            String[] inputs = new String[10000];
            int index = 0;
            while (true) {
                String input = scanner.nextLine();
                if ("0".equals(input))
                    break;
                inputs[index++] = input;
            }
            String[] name = new String[index];
            int[] arr = new int[index];

            for (int i = 0; i < index; i++) {
                String[] split = inputs[i].split(",");
                if (split.length == 2) {
                    name[i] = split[0];
                    arr[i] = Integer.parseInt(split[1]);
                }
            }
//            int[] arr = new int[inputs[0]];
//            for (int i = 0; i < inputs[0]; i++) {
//                arr[i] = inputs[i];
//            }
        HashMap<String,Integer> hashMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            int j = get(arr);
            System.out.println(""+name[j]);
            if (hashMap.get(name[j]) == null) {
                hashMap.put(name[j], 1);
            }else {
                hashMap.put(name[j], hashMap.get(name[j])+1);
            }

        }
        System.out.println(""+hashMap);

//        }
    }

    private static int get(int[] arr) {
        int num = 0;
        for (int i = 0; i < arr.length; i++) {
            num += arr[i];
        }
        int rand = randInt(1, num);
        System.out.println(""+rand);
        int mm = 0;
        for (int j = 0; j < arr.length; j++) {
            if (rand >= (mm+1) && rand <= (mm+arr[j])) {
//                System.out.println(""+arr2[j]);
                return j;
            }
            mm += arr[j];
        }
        return -1;
    }

    public static int randInt(int min, int max)
    {
        Random _rand = new Random();
        return _rand.nextInt(max - min + 1) + min;
    }


}
