package com.example.ObjectPool;

/**
 * Created by fj on 2018/7/12.
 */

public class PoolsMain {

    private static ObjectPool<TestObject> objectPool;

    public static void main(String[] args){

        long t1 = System.currentTimeMillis();
        System.out.println("================1      "+t1);
        objectPool = new ObjectPool<TestObject>();
        objectPool.set(new ObjectPool.IObject<TestObject>(){
            @Override
            public TestObject setObject() {
                return new TestObject();
            }
        });

        for (int i = 0; i < 1000; i++) {
            TestObject obtain = objectPool.obtain();
//            System.out.println("obtain  "+obtain+" -- "+System.currentTimeMillis());
            objectPool.release(obtain);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("================2      "+(t2-t1));

        for (int i = 0; i < 1000; i++) {
            TestObject test = new TestObject();
        }
        System.out.println("================3      "+(System.currentTimeMillis()-t2));
    }
}
