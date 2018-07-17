package com.xposed.api.libtest;

import com.MarketSession;

import java.io.File;

public class myClass {
    public static void main(String[] args) {
        String pkg = "com.tunaikita.cashloan";

        MarketSession session = new MarketSession();
        session.setAndroidId("3eb3b995166f623f");
//        session.setPoxyUrl("149.129.218.75");
//        session.setPoxyPort(8888);
//        session.setPoxyUserInfo("jojo_1", "123456");
//        session.setPoxyUserInfo("jojo", "5IBYnvAdtLsan60J");
        try {
//            session.login("kuaiweijian@gmail.com", "kuai@520");
//            session.login("kuaijianjian@gmail.com", "xiaoshuo001");
            session.login("xiayutianlezenmeban@gmail.com", "ll123456");
//            session.login("aili1022q@gmail.com", "19970224YW");  // 登录错误

            int versionCode = session.getVersionCode(pkg);
            System.out.println("versionCode "+versionCode);
            String[] downloadInfo = session.getDownloadInfo(pkg);
            System.out.println("downloadInfo  "+downloadInfo);
        } catch (Exception e) {
            e.printStackTrace();

//            try {//激活
//                session.activate("kuaijianjian@gmail.com", "xiaoshuo001", "3f1abe856b0fa7fd", pkg);
//                String[] downloadInfo = session.getDownloadInfo(pkg);
//            } catch (Exception e1) {
//                e1.printStackTrace();
//                System.out.println("222222222222 ");
//            }
        }
    }
}
