package com.example.designpatterns.factory;

public class myClass {
    public static void main(String[] args) {
        IAnimal animal = new WhiteAnimal();
        animal.showCat();
        animal.showFish();

        animal = new BlackAnimal();
        animal.showCat();
        animal.showFish();
    }
}
