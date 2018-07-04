package com.api.libgoogle;

import com.MarketSession;

import java.io.File;

public class myClass {
    public static void main(String[] args) {
        String pkg = "com.finwizard.fisdom";

        MarketSession session = new MarketSession();
        session.setAndroidId("3eb3b995166f623f");
        session.setPoxyUrl("149.129.218.75");
        session.setPoxyPort(8888);
//        session.setPoxyUserInfo("jojo_1", "123456");
        session.setPoxyUserInfo("jojo", "5IBYnvAdtLsan60J");
        try {
            session.login("kuaiweijian@gmail.com", "kuai@520");

            int versionCode = session.getVersionCode(pkg);

            System.out.println("versionCode "+versionCode+" -- "+pkg);
            session.startDownload(pkg, new File("D:\\test", pkg+".apk") );

        } catch (Exception e) {
            e.printStackTrace();

            try {//激活
                session.activate("kuaiweijian@gmail.com", "kuai@520", "3f1abe856b0fa7fd", pkg);
                session.startDownload(pkg, new File("D:\\test", pkg+".apk") );
            } catch (Exception e1) {
                e1.printStackTrace();
                System.out.println("222222222222 ");
            }
        }
    }
}
