package Opt;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by fj on 2018/8/28.
 */

public class mClasss {
    public void main(String[] args)
    {
        workdemo workdemo = newProxyInstance(workdemo.class);
        String result = workdemo.getWether("101010100");


        String s = (String) Proxy.newProxyInstance(String.class.getClassLoader(), new Class[]{String.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return null;
            }
        });

    }



    public <T> T newProxyInstance(Class<T> mclass){
        return (T)Proxy.newProxyInstance(mclass.getClassLoader(), new Class[]{mclass}, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                Annotation[] annotations = method.getAnnotations();
                for (Annotation annotation : annotations)
                {

                }
                for (int i = 0; i < annotations.length; i++) {
                    if (annotations[i] instanceof GET) {
                        String v1= ((GET)annotations[i]).value1();
                    }
                }

                Annotation[][] annotations2 = method.getParameterAnnotations();
                int count = annotations2.length;
//                for (int i)
                for (int i = 0; i < count; i++) {
                    Annotation[] an = annotations2[i];
//                    if (an ) {
//
//                    }
                    for (Annotation ant : an) {

                    }
//                    if (an instanceof GET) {
//                        String v1= ((PATH)an).value();
//                    }
                }

                String httppath = "";
                if (objects.length == 1) {
                    String cityid = (String) objects[0];
                    httppath = "http://www.weather.com.cn/adat/sk/"+cityid+".html";
                }

                return httppath;
            }
        });
    }
}
