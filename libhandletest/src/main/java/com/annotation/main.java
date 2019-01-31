package com.annotation;
// NonNull的路径必须是这个
import android.support.annotation.NonNull;


import com.api.utils.annotationprocessor.annot.AntiBean;

import sun.reflect.generics.tree.TypeTree;
import sun.reflect.generics.visitor.TypeTreeVisitor;


/**
 * Created by fj on 2018/7/24.
 */

public class main {

    public static void main(String[] args) {
        String str = null;
        aaa(str);

        TypeTree typeTree = new TypeTree() {
            @Override
            public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
//                typeTreeVisitor.
            }
        };
//        sun.reflect.generics.factory.CoreReflectionFactory
//   自定義


    }


    public static void aaa(@NonNull String aaa) {

    }

    @AntiBean
    public void getUserName(){

    }


}
