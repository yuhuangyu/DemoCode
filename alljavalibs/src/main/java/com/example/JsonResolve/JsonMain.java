package com.example.JsonResolve;

import java.util.Map;

/**
 * Created by ASUS on 2018/6/25.
 */

public class JsonMain {
    public static void main(String[] args) {

        String json = "{\"111\":{\"qqq\":555,\"ttt\":{\"uuu\":888}},\"aaa\" :  \"1 11\" ,\"bbb\" : 222  ,\"ccc\" :  33.33 ,\"ddd\":{\"eee\":\"444\"}}";
        String json2 = "{\"aaa\" :  \"111\" ,\"bbb\" : 222  ,\"ccc\" :  33.33 }";
        String json3 = "{\"l1\": {\"l1_1\": [\"l1_1_1\",\"l1_1_2\",{\"aaa\":\"111\"},999],\"l1_2\": {\"l1_2_1\": 121}},\"l2\": {\"l2_1\": \"aaa\",\"l2_2\": true,\"l2_3\": null,\"l2_4\": \"\",\"l2_5\": {}}}";
        String json4 = "{\"l1\": {\"l1_1\": [\"l1_1_1\",{\"aaa\":\"111\",\"666\":[444,555]},999,[222,333],\"iii\"],\"l1_2\": 123},\"www\":false}";
        String json5 = "{\"l1\": {\"l1_1\": [\"l1_1_1\",{\"aaa\":\"111\"},999,[111,\"aaa\"]],\"l1_2\": 123},\"www\":false}";
        String json6 = "[{\"aaa\" :  \"111\" ,\"bbb\" : 222  ,\"ccc\" :  33.33 },2000]";

        try {
            Map<String,Object> jsonObject = new JsonResolve2(json3).getJsonObject();
            System.out.println("jsonObject "+jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*long start = System.currentTimeMillis();
        System.out.println(" "+start);
        for (int i = 0; i < 100000; i++) {
            try {
                Map<String,Object> jsonObject = new JsonResolve2().getJsonObject(json2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(" "+(System.currentTimeMillis()-start));*/
    }

}
