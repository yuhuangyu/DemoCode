package com.test.huisuo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyClassHuisuo {

    private static HashMap<String, Boolean> map;
    private static ArrayList<String> list;

    public static void main(String[] args) {
        String words[] = {"so","soon","river","goes","them","got","moon","begin","big","0"};

        map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            map.put(words[i],false);
        }

        list = new ArrayList<>();

        new MyClassHuisuo().backtrace(words,'b');
    }
    public void backtrace(String[] words, char begin){
        if (isExcepetedGoal()) {
            printAllGoal();
            return;
        }
        List<String> nextGoal = findNextGoal(words,begin);
        for (String i : nextGoal) {
//            handle(i);
            mark(i);
            backtrace(words,i.charAt(i.length()-1));
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

    private List<String> findNextGoal(String[] words, char begin) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].charAt(0) == begin && !map.get(words[i])) {
                list.add(words[i]);
            }
        }
        return list;
    }

    private void printAllGoal() {
        StringBuilder flag = new StringBuilder("");
        for (int i = 0; i < list.size(); i++) {
            flag.append(list.get(i)+"  ");
        }
        System.out.println(flag);
    }

    public boolean isExcepetedGoal() {
        return list.get(list.size()-1).endsWith("m");
    }

}

