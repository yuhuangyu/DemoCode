package com.datastorage;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;

import static org.junit.Assert.*;

/**
 * Created by fj on 2018/7/27.
 */
public class StorageTest {
    @Test
    public void test() throws Storage.StorageException
    {
        Storage.Container container = Storage.builder("test", Storage.Object)
//                .dir(new File("./build/obj"))
                .dir(new File("D:\\encry\\obj"))
                .build();

        container.put("test", "anye");

        Assert.assertEquals("anye", container.value("test"));

        Data data = new Data();
        data.setName("anye");
        data.setAge(28);
        data.setMan(true);
        container.put("data", data);

        assertEquals(data, container.value("data"));

        assertTrue(container.containsKey("data"));

        container.remove("data");

        assertFalse(container.containsKey("data"));
    }

    @Test
    public void testProp() throws Storage.StorageException
    {
        new File("D:\\encry\\proptest").delete();
        Storage.Container container = Storage.builder("test", Storage.Properties)
                .dir(new File("D:\\encry\\prop"))
                .build();

        container.put("test", "anye");

        Assert.assertEquals("anye", container.value("test", String.class));

        Data data = new Data();
        data.setName("anye");
        data.setAge(28);
        data.setMan(true);

        String dataName = "data";

        container.put(dataName, data);

        assertEquals(data, container.get(dataName, Data.class));

        assertTrue(container.containsKey(dataName));

        container.remove(dataName);

        assertFalse(container.containsKey(dataName));
    }

    public static class Data implements Serializable
    {
        private String name;
        private int age;
        private boolean isMan;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getAge()
        {
            return age;
        }

        public void setAge(int age)
        {
            this.age = age;
        }

        public boolean isMan()
        {
            return isMan;
        }

        public void setMan(boolean man)
        {
            isMan = man;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Data data = (Data) o;

            if (age != data.age) return false;
            if (isMan != data.isMan) return false;
            return name.equals(data.name);
        }

        @Override
        public int hashCode()
        {
            int result = name.hashCode();
            result = 31 * result + age;
            result = 31 * result + (isMan ? 1 : 0);
            return result;
        }
    }
}