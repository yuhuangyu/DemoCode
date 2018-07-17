package com.example;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

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
