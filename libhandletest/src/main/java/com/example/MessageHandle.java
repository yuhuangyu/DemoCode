package com.example;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by ASUS on 2018/4/25.
 */

public class MessageHandle {

    private Queue<MMessage> messageTasks = new ConcurrentLinkedDeque<MMessage>();
    private IRequestMessage iRequestMessage;
    private static MessageHandle messageHandle;

    private MessageHandle(IRequestMessage iRequestMessage){
        this.iRequestMessage = iRequestMessage;
        onLooper();
    }

    public static synchronized MessageHandle getInstance(IRequestMessage iRequestMessage) {
        if (messageHandle == null) {
            synchronized (MessageHandle.class) {
                if (messageHandle == null) {
                    messageHandle = new MessageHandle(iRequestMessage);
                }
            }
        }
        return messageHandle;
    }

    public void sendMessage(String type, String key, String value){
        messageTasks.offer(new MMessage(type,key,value));
    }

    public static void sendMessage2(String type, String key, String value){
        if (messageHandle != null) {
            messageHandle.sendMessage(type,key,value);
        }
    }

    private void onLooper(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    MMessage msg = messageTasks.poll();
                    if (msg != null) {
                        iRequestMessage.request(msg.type,msg.key,msg.value);
                    }
                }
            }
        }).start();
    }

    public interface IRequestMessage {
        void request(String type, String key, String value);
    }

    private class MMessage{
        private String key;
        private String value;
        private String type;
        public MMessage(String type, String key, String value){
            this.key = key;
            this.value = value;
            this.type = type;
        }
    }
}
