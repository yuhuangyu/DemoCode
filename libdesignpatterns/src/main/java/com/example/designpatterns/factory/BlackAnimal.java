package com.example.designpatterns.factory;

/**
 * Created by ASUS on 2018/5/25.
 */

public class BlackAnimal implements IAnimal {
    @Override
    public ICat showCat() {
        return new BlackCat();
    }

    @Override
    public Ifish showFish() {
        return new BlackFish();
    }
}
