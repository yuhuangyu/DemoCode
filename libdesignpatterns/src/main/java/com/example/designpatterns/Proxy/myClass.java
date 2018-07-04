package com.example.designpatterns.Proxy;

public class myClass {
    public static void main(String[] args) {
        SubjectProxy subjectProxy = new SubjectProxy(new SubjectImpl());
        subjectProxy.operation();
    }
}
