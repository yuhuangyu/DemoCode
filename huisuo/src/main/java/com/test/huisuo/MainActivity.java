package com.test.huisuo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Environment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

//import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new MyView3(this));
        setContentView(R.layout.activity_main);
//        setContentView(new MView(this));
//        CrashReport.testJavaCrash();

//        test01();

//        View decorView = MainActivity.this.getWindow().getDecorView();
//        int viewHeight = decorView.getMeasuredHeight();
//        int viewWidth = decorView.getMeasuredWidth();
//
//        WindowManager manager = (WindowManager) decorView.getContext().getSystemService(Context.WINDOW_SERVICE);
//        int viewWidth2 = manager.getDefaultDisplay().getWidth();
//        int viewHeight2 = manager.getDefaultDisplay().getHeight();
//
//        Log.e("sdk", "viewHeight: "+viewHeight+" ,viewWidth: "+viewWidth+"viewHeight2: "+viewHeight2+" ,viewWidth2: "+viewWidth2+"---"+getAvailableScreenHeight(MainActivity.this));
//
//
////        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics displaysMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(displaysMetrics);
//
//        Point point = new Point();
//        manager.getDefaultDisplay().getRealSize(point);
//        DisplayMetrics dm = this.getResources().getDisplayMetrics();
//        double x = Math.pow(point.x / dm.xdpi, 2);
//        double y = Math.pow(point.y / dm.ydpi, 2);
//        double screenInches = Math.sqrt(x + y);
//        Log.e("sdk"," ,x= "+x+" ,y= "+y+" ,screenInches="+screenInches);
//        Log.e("sdk"," ,pointx= "+point.x+" ,pointy= "+point.y+" ,screenInches="+screenInches);


        ArrayList<String> strings = new ArrayList<>();
//        "123".

        String json = "{\"aaa\" :  \"111\" ,\"bbb\" : 222  ,\"ccc\" :  33.33 }";
        try {
            JSONObject jsonObject = new JsonResolve().getJsonObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 返回屏幕可用高度
     * 当显示了虚拟按键时，会自动减去虚拟按键高度
     */
    public int getAvailableScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    private void test01() {
        File file = new File(this.getDir("apps", Context.MODE_PRIVATE), "EventJS.zip");

//        String s = "jsevent.co.as.zip";
//        String str = md5(s.getBytes());
//        Log.e("Sdk", "str1 "+str);
//        String s2 = "jsevent.co.as.zip";
//        String str2 = md5(s2.getBytes());
//        Log.e("Sdk", "str2 "+str2);

//        String path = Environment.getExternalStorageDirectory().getPath() + "/test/"+s;
//        String zipFile = path+".zip";
//        Unzip(zipFile,str);

        /*IntentFilter filter2 = new IntentFilter("com.XNJresult.RECEIVER");
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if ("com.XNJresult.RECEIVER".equals(intent.getAction())) {
                    Log.e("Sdk", "onReceive");
                    Bundle bundle = intent.getExtras();
                    String str = bundle.getString("str");
                    Log.e("Sdk", "str--"+str);
                    SerializableMap serializableMap = (SerializableMap) bundle.get("map");
                    Map<String, String> map = serializableMap.getMap();
                    for (String key:map.keySet()) {
                        Log.e("Sdk", key+"--"+map.get(key));
                    }
                }
            }
        }, filter2);*/


//        Bundle bundle = new Bundle();
//        bundle.putString("str","aaa");
//        final SerializableMap myMap=new SerializableMap();
//        Map<String, String> map = new HashMap<>();
//        map.put("1", "a");
//        map.put("2", "b");
//        map.put("3", "c");
//        myMap.setMap(map);//将map数据添加到封装的myMap<span></span>中
//        bundle.putSerializable("map", myMap);
//        Intent intent = new Intent("com.XNJresult.RECEIVER");
////        intent.setPackage(context.getPackageName());
//        intent.putExtras(bundle);
//        this.getApplicationContext().sendBroadcast(intent);
        Map<String, String> map = new HashMap<>();
        map.put("1", "a");
        map.put("2", "b");
        map.put("3", "c");

        List<String> list = new ArrayList<>();
        for (String key:map.keySet()) {
            list.add(key);
            list.add(map.get(key));
        }
//        Log.e("Sdk", "list "+list.toString()+"=="+list.get(0));
        Map<String, String> map2 = new HashMap<>();
        if (list.size()%2==0) {
            for (int i = 0; i < list.size(); i=i+2) {
                map2.put(list.get(i),list.get(i+1));
            }
        }
        for (String key:map2.keySet()) {
            Log.e("Sdk", "== "+key+"--"+map2.get(key));
        }
        Log.e("Sdk", "2== "+map2.get("2"));
        Log.e("Sdk", "4== "+map2.get("4"));
        Log.e("Sdk", "map2 "+map2.toString());

        File fileaa = new File(this.getDir("apps", Context.MODE_PRIVATE), "aaa");
        Log.e("Sdk", "fileaa "+fileaa.getPath());
    }

    /*
    *
    * zipFile 为zip原路径，targetDir为zip原路径上层路径新建targetDir的文件夹
     *  */
    private static void Unzip(String zipFile, String targetDir) {
        int BUFFER = 4096; //这里缓冲区我们使用4KB，
        String strEntry; //保存每个zip的条目名称
        try {
            BufferedOutputStream dest = null; //缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; //每个zip条目的实例
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    Log.i("Unzip: ","="+ entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();
                    String s = new File(zipFile).getParent() + "/" + targetDir + "/";
                    File entryFile = new File(s+ strEntry);
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
            if (new File(zipFile).exists()) {
                new File(zipFile).delete();
            }
        } catch (Exception cwj) {
            cwj.printStackTrace();
        }
    }

    /*public static void unzip(String zipFilePath, String targetPath)
            throws IOException {
        OutputStream os = null;
        InputStream is = null;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFilePath,"gbk");
            String directoryPath = "";
            if (null == targetPath || "".equals(targetPath)) {
                directoryPath = zipFilePath.substring(0, zipFilePath
                        .lastIndexOf("."));
            } else {
                directoryPath = targetPath+getFileName(zipFilePath);
            }
            Enumeration entryEnum = zipFile.getEntries();
            if (null != entryEnum) {
                ZipEntry zipEntry = null;
                while (entryEnum.hasMoreElements()) {
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.getSize() > 0) {
                        // 文件
                        File targetFile = buildFile(directoryPath
                                + File.separator + zipEntry.getName(), false);
                        os = new BufferedOutputStream(new FileOutputStream(
                                targetFile));
                        is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[4096];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                            os.write(buffer, 0, readLen);
                        }


                        os.flush();
                        os.close();
                    } else {
                        // 空目录
                        buildFile(directoryPath + File.separator
                                + zipEntry.getName(), true);
                    }
                }
            }
        } catch (IOException ex) {
            Log.d("hck", "IOExceptionIOException: "+ex.toString());
            throw ex;
        } finally {
            if(null != zipFile){
                zipFile = null;
            }
            if (null != is) {
                is.close();
            }
            if (null != os) {
                os.close();
            }
        }
    }*/

    //创建目录or文件
    public static File buildFile(String fileName, boolean isDirectory) {


        File target = new File(fileName);


        if (isDirectory) {


            target.mkdirs();


        } else {


            if (!target.getParentFile().exists()) {


                target.getParentFile().mkdirs();


                target = new File(target.getAbsolutePath());


            }


        }


        return target;


    }
    //获取文件名字
    public static String getFileName(String pathandname) {


        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }


    }


    static char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static final String md5(byte[] s) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(s);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var9) {
            var9.printStackTrace();
            return null;
        }
    }
}
