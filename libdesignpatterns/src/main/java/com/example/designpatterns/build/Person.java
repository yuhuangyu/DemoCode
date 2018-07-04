package com.example.designpatterns.build;

/**
 * Created by ASUS on 2018/5/25.
 *
 *  建造者模式
 *
 */

public class Person {

    private String name;
    private String id;
    private String age;

    private Person(PersonBuild build){
        this.name = build.name;
        this.id = build.id;
        this.age = build.age;
    }

    public void show(){
        System.out.println("Person: name-"+name+" ,id-"+id+" ,age-"+age);
    }


    static class PersonBuild{
        private String name;
        private String id;
        private String age;

        public PersonBuild(){

        }
        public PersonBuild name(String name){
            this.name = name;
            return this;
        }
        public PersonBuild id(String id){
            this.id = id;
            return this;
        }
        public PersonBuild age(String age){
            this.age = age;
            return this;
        }
        public Person build(){
            return new Person(this);
        }
    }
}
