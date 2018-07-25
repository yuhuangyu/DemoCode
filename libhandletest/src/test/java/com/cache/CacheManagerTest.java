package com.cache;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by fj on 2018/7/19.
 */
public class CacheManagerTest {
    @Test
    public void testLru() throws Exception
    {
        CacheManager.Access<String, Integer> access = CacheManager
                .builder()
                .lru(50).apply()
                .build();
        for (int i = 0; i < 55; i++)
        {
            access.put("key" + i, i);
        }

        Assert.assertEquals(50, (int) access.get("key50", -1));
        Assert.assertEquals(-1, (int) access.get("key1", -1));
        Assert.assertEquals(5, (int) access.get("key5", -1));
        Assert.assertEquals(-1, (int) access.get("key0", -1));
        Assert.assertEquals(7, (int) access.get("key7", -1));
        access.put("key100",100);
        Assert.assertEquals(100, (int) access.get("key100", -1));
        Assert.assertEquals(-1, (int) access.get("key6", -1));
    }

}