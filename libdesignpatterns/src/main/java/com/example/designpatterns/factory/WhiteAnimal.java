package com.example.designpatterns.factory;

/**
 * Created by ASUS on 2018/5/25.
 */

public class WhiteAnimal implements IAnimal {
    @Override
    public ICat showCat() {
        return new WhiteCat();
    }

    @Override
    public Ifish showFish() {
        return new WhiteFish();
    }
}
