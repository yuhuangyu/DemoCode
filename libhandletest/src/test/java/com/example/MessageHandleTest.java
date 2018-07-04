package com.example;

import junit.framework.TestCase;

/**
 * Created by ASUS on 2018/4/25.
 */
public class MessageHandleTest extends TestCase {
    private static MessageHandle messageHandle;
    private int num = 0;
    public void testGetInstance() throws Exception {
        messageHandle = MessageHandle.getInstance(new MessageHandle.IRequestMessage() {
            @Override
            public void request(String type, String key, String value) {
                num++;
                System.out.println(type+" : "+key + "---" + value);
                assertEquals("1",type);
                assertEquals("aaa",key);
                assertEquals("123",value);
            }
        });
        for (int i = 0; i < 11; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    messageHandle.sendMessage("1","aaa","123");
                }
            }).start();
        }


    }

    public void testSendMessage() throws Exception {

    }

}