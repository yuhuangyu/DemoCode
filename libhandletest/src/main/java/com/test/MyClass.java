package com.test;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {
    private static final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(1);
    public static void main(String[] args) {


//        try {
//            boolean sss = queue.offer("sss");
//            System.out.println(" 11111 "+sss);
//            boolean aaa = queue.offer("aaa");
//            System.out.println(" 22222 "+aaa);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String str = "Is is the cost of of gasoline going up up";
//        String patt1 = "/\\b([a-z]+) \\1\\b/ig";
        String pattern = "(\\b([a-z]+) \\1\\b)gi";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        System.out.println(m.matches());



    }

}
