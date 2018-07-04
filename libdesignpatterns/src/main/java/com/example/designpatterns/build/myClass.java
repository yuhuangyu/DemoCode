package com.example.designpatterns.build;

public class myClass {
    public static void main(String[] args) {

        Person zhangsan = new Person.PersonBuild()
                .name("zhangsan")
                .id("123")
                .age("12")
                .build();

        zhangsan.show();
//        Person.PersonBuild personBuild = new Person.PersonBuild();
//        personBuild.
    }
}
