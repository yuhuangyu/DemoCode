package com.example.designpatterns.observer;

public class myClass {
    public static void main(String[] args) {
        Observable observable = new Observable();

        Observable.Observe<Weather> observe = new Observable.Observe<Weather>(){
            @Override
            public void upData(Weather weather) {
                String description = weather.getDescription();
                System.out.println("1===Weather.getDescription: "+description);
                weather.show();
            }
        };
        Observable.Observe<Weather> observe2 = new Observable.Observe<Weather>(){
            @Override
            public void upData(Weather weather) {
                String description = weather.getDescription();
                System.out.println("2===Weather.getDescription: "+description);
                weather.show();
            }
        };
        observable.register(observe);
        observable.register(observe2);


        observable.notifyObserve(new Weather("1111"));
        observable.notifyObserve(new Weather("2222"));

        observable.unRegister(observe);

        observable.notifyObserve(new Weather("3333"));
    }
}
