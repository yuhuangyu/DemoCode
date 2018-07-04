package com.example.MathCalculate;

import java.util.Stack;

public class MyClass {
    public static void main(String[] args)
    {
        Stack<String> stack1 = new Stack<>();
        Stack<String> stack2 = new Stack<>();
        String str = "3.25+0.5+2*(3-0.3)";
        char[] cStr = str.toCharArray();
        StringBuffer atrb = new StringBuffer("");
        for (int i = 0; i < cStr.length; i++) {
            System.out.print(cStr[i]+" ");

            if (cStr[i] >= '0' && cStr[i] <= '9' || cStr[i] == '.') {
                atrb.append(cStr[i]);
            }else if (cStr[i] == '(') {
                stack2.push(String.valueOf(cStr[i]));
                atrb = new StringBuffer("");
            } else if (cStr[i] == ')') {
                stack1.push(atrb.toString());
                while (stack2.size() != 0) {
                    if (!"(".equals(stack2.peek()+"")) {
                        stack1.push(stack2.peek());
                        stack2.pop();
                    }else {
                        stack2.pop();
                    }
                }
                atrb = new StringBuffer("");
            } else if (cStr[i] == '+'  || cStr[i] == '-' ) {
                if (stack2.size() != 0 && ("+".equals(stack2.peek()+"")  || "-".equals(stack2.peek()+"") || "*".equals(stack2.peek()+"")  || "/".equals(stack2.peek()+""))) {
                    stack1.push(atrb.toString());
                    stack1.push(stack2.peek());
                    stack2.pop();
                    stack2.push(String.valueOf(cStr[i]));
                }else {
                    stack1.push(atrb.toString());
                    stack2.push(String.valueOf(cStr[i]));
                }
                atrb = new StringBuffer("");
            } else if (cStr[i] == '*'  || cStr[i] == '/' ) {
                if (stack2.size() != 0 && ("*".equals(stack2.peek()+"")  || "/".equals(stack2.peek()+""))) {
                    stack1.push(atrb.toString());
                    stack1.push(stack2.peek());
                    stack2.pop();
                    stack2.push(String.valueOf(cStr[i]));
                }else {
                    stack1.push(atrb.toString());
                    stack2.push(String.valueOf(cStr[i]));
                }
                atrb = new StringBuffer("");
            }
        }
        System.out.println("-------");
        String[] strnum = new String[stack1.size()];
        int size = stack1.size();
        System.out.print(stack1);
        while (stack1.size() != 0) {
//            System.out.print(stack1.peek()+" ");
            size--;
            strnum[size] = stack1.peek();
            stack1.pop();
        }
        System.out.println("-------");
        Stack<Double> stack3 = new Stack<>();
//        for (int i = 0; i < strnum.length; i++) {
//            stack3.push(strnum[i]);
//            System.out.print(strnum[i]+" ");
//        }
        int flag = 0;
        int nowsize = 0;
        while(strnum.length > nowsize){
            if ("+".equals(strnum[nowsize])) {
                flag = 1;
                doMath(stack3, flag);
            }else if ("-".equals(strnum[nowsize])) {
                flag = 2;
                doMath(stack3, flag);
            }else if ("*".equals(strnum[nowsize])) {
                flag = 3;
                doMath(stack3, flag);
            }else if ("/".equals(strnum[nowsize])) {
                flag = 4;
                doMath(stack3, flag);
            }else {
                stack3.push(Double.parseDouble(strnum[nowsize]));
            }
            nowsize++;
        }

        System.out.print(stack3.peek());

    }

    private static void doMath(Stack<Double> stack3, int flag) {
        Double right = stack3.pop();
        Double left = stack3.pop();
        Double num = 0.0;
        if (flag == 1) {
            num = left + right;
        }else if (flag == 2) {
            num = left - right;
        }else if (flag == 3) {
            num = left * right;
        }else if (flag == 4) {
            num = left / right;
        }
        stack3.push(num);
    }
}
