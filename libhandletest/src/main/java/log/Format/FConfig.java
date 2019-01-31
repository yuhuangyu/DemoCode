package log.Format;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ASUS on 2018/7/6.
 */
/*
*  FConfig 方法配置
*  配置的方法参数类型 ：
*  可以使用：    Integer，Double，String, Character, Boolean
*  不能使用：    int, float, double, char, Object, boolean
*
*  mDefault的参数Object做了适配
*
*  使用包装类作为方法参数类型
* */
public class FConfig {

    public Object mDefault(Object object){
        return  object;
    }

    public String nowTime(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }



}
