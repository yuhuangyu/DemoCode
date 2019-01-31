package annotation;

/**
 * Created by fj on 2018/8/7.
 */

public class Client {
    public static void main(String[] args) {

        int num = 0;
        for (int i = 0; i < 100; i++) {
            num = num + i;
        }

        // ActivityManager  system_process
        SimpleDomain sd = new SimpleDomain();//要进行加密解密的实体类
        sd.setId("6029131988005021537");//注入身份证号
        System.out.println(sd.encryptSelf().toString());//执行自加密后输出
        System.out.println(sd.decryptSelf().toString());//执行自解密后输出
//        System.out.println(JSON.toJSONString(sd.encryptSelf()));//执行自加密后输出
//        System.out.println(JSON.toJSONString(sd.decryptSelf()));//执行自解密后输出
    }
}
