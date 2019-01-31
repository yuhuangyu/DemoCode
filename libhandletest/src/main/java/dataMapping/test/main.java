package dataMapping.test;

import dataMapping.DataMapping;
import dataMapping.json.JsonSource;

/**
 * Created by fj on 2018/10/11.
 */

public class main {

    public static void main(String[] args) {
        try {

            Person person = new Person();
            person.setAge(11);
            person.setName("aaa");
            person.setSex(true);

            String s = DataMapping.instance(JsonSource.format()).convert(person).toString();
            System.out.println("==== "+s);


            Person convert = (Person) DataMapping.instance(JsonSource.format()).convert(JsonSource.object("{\"sex\":true,\"name\":\"aaa\",\"age\":11}"), Person.class);
            System.out.println("==== "+convert.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
