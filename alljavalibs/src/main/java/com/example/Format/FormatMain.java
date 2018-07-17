package com.example.Format;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ASUS on 2018/7/6.
 */

public class FormatMain {
    public static void main(String[] args) {
        Map<Object, Object> map = new HashMap<>();
        int sss = 123;
        map.put("sss",sss);
        map.put("aaa",111);
        map.put("a1",1.11);
//        map.put("a2",1.22);
        map.put("a123",true);

        String str = "111+123=${@aaa,@sss }  1.11+1.22=$add{@a1, 1.22 } 11${\"$\"}{aaaa} $splice{\"100\", true} }{{}@@   !${false}=${@a123}  ${\"time: \"}$nowTime{}";

        try {
            Format format = Format.get(str);
            String getformat = format.getformat(map, new FConfig2() {

                public int add1(Integer a, Integer b) {
                    return a - b;
                }

//                @Override
//                public double add(Double a, Double b) {
//                    return a * b;
//                }

                @Override
                public String splice(String a, Boolean b) {
                    if (b) {
                        return a + "+" + b;
                    }
                    return super.splice(a, b);
                }

                @Override
                public Object mDefault(Object object) {
                    return super.mDefault(object);
                }

                public Object mDefault(Integer a, Integer b) {
                    return a + b;
                }
            });

            System.out.println("======: "+getformat);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        List<String> list = new ArrayList<>();
//        Stream<String> words = list.stream();
//        List<String> collect = words.filter(w -> w.length() > 3)
//                .collect(Collectors.<String>toList());
//        new ArrayList<String>().stream().filter(aa -> !"".equals(aa)).collect(Collectors.<String>toList());


        /*FConfig format = new FConfig();
        try {
            int obj1 = 111;
            int obj2 = 333;
            Object invoke = ReflectAccess.invoke(format, "add", new Class[]{int.class,int.class}, new Object[]{obj1,obj2});
            System.out.println("invoke "+invoke);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Object obj1 = 11.11;
        System.out.println("invoke "+obj1.getClass().getCanonicalName()+" -- "+obj1.getClass().getName()+" -- "+double.class);

    }
    static class FConfig2 extends FConfig{
            // 对象
    }
}


