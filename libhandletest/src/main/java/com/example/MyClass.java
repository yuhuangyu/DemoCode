package com.example;



public class MyClass {

    private static MessageHandle messageHandle;
    private static int num;
    private static MessageHandle instance;

    public static void main(String[] args) {

        messageHandle = MessageHandle.getInstance(new MessageHandle.IRequestMessage() {
            @Override
            public void request(String type, String key, String value) {
                num++;
                System.out.println(type+" : "+key + " --- " + value+"       "+num);
            }
        });

        for (int i = 0; i < 12; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    messageHandle.sendMessage("1", "aaa","123 ");
                }
            }).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MessageHandle.sendMessage2("2", "bbb","666 ");
                }
            }).start();
        }
    }
}
