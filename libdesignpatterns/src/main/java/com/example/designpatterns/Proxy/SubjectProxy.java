package com.example.designpatterns.Proxy;

/**
 * Created by ASUS on 2018/5/25.
 */

public class SubjectProxy implements Subject{
    private Subject subject;
    public SubjectProxy(Subject subject){
        this.subject = subject;
    }

    @Override
    public void operation() {
        if (subject != null) {
            subject.operation();
        }else {

        }
    }
}
