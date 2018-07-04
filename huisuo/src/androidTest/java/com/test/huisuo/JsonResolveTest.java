package com.test.huisuo;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ASUS on 2018/6/26.
 */
public class JsonResolveTest {
    @Test
    public void testGetJsonObject() throws Exception {
//        assertNotNull("aaa");
        String json = "{\"aaa\" :  \"1 11\" ,\"bbb\" : 222  ,\"ccc\" :  33.33 }";
        try {
            JSONObject jsonObject = new JsonResolve().getJsonObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}