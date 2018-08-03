package com.annotation;
// NonNull的路径必须是这个
import android.support.annotation.NonNull;

import sun.reflect.generics.tree.TypeTree;
import sun.reflect.generics.tree.Tree;
import sun.reflect.generics.visitor.TypeTreeVisitor;


/**
 * Created by fj on 2018/7/24.
 */

public class main {
    // JAVABEAN
//    private Trees _trees;
//    private TreeMakes _makes;
//    private Names _names;

    public static void main(String[] args) {
        String str = null;
        aaa(str);

//        Tree tree = new Tree();
        TypeTree typeTree = new TypeTree() {
            @Override
            public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
//                typeTreeVisitor.
            }
        };
//        _trees = Trees.instance(processingEnv);
//        _makes = Trees.instance(content);
//        _names = Trees.instance(content);
//        sun.reflect.generics.factory.CoreReflectionFactory
//   自定義
    }


    public static void aaa(@NonNull String aaa) {

    }


}
