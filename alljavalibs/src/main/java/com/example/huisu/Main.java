package com.example.huisu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static HashMap<String, Boolean> map;
    private static ArrayList<String> list;
    private static boolean isOk = false;

    public static void main(String[] args) {
//        String words[] = {"so","soon","river","goes","them","got","moon","begin","big","0"};


        Scanner scanner = new Scanner(System.in);

        while (true){
            if (!scanner.hasNext()) {
                return;
            }
            ArrayList<String> listWords = new ArrayList<>();
            map = new HashMap<>();
            list = new ArrayList<>();
            isOk = false;
            while (true){
                String next = scanner.next();
                if ("0".equals(next)) {
                    break;
                }
                listWords.add(next);
            }


            for (int i = 0; i < listWords.size(); i++) {
                map.put(listWords.get(i),false);
            }

            new Main().backtrace(listWords,'b');
            if (!isOk) {
                System.out.println("No.");
            }else {
                System.out.println("Yes.");
            }

        }
    }

    public void backtrace(List<String> listWords, char begin){
        if (isExcepetedGoal()) {
            isOk = true;
//            System.out.println("Yes.");
//            printAllGoal();
            return;
        }
        List<String> nextGoal = findNextGoal(listWords,begin);
        for (String i : nextGoal) {
            mark(i);
            backtrace(listWords,i.charAt(i.length()-1));
            unmark(i);
        }
    }

    private void handle(String word) {

    }

    private void unmark(String word) {
        map.put(word,false);
        list.remove(word);
    }

    private void mark(String word) {
        map.put(word,true);
        list.add(word);
    }

    private List<String> findNextGoal(List<String> words, char begin) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).charAt(0) == begin && !map.get(words.get(i))) {
                list.add(words.get(i));
            }
        }
        return list;
    }

    private void printAllGoal() {
        StringBuilder flag = new StringBuilder("");
        for (int i = 0; i < list.size(); i++) {
            flag.append(list.get(i)+"-");
        }
        System.out.println(flag.substring(0,flag.length()-1));
    }

    public boolean isExcepetedGoal() {
        return list.size() > 1 && list.get(list.size()-1).endsWith("m");
    }

}

