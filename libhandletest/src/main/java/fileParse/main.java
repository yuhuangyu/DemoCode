package fileParse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fj on 2018/9/19.
 */

public class main {

    private static ArrayList<String> list;
    private static String ssss;
    private static HashSet<String> listAll;

    public static void main(String[] args) {

//        testFile();
//        testFile2();

//        ssss = file2String("D:\\encry\\cc.txt");
//        testFile();

//        HashSet<String> list = new HashSet<String>();
//        list.add("sss");
//        list.add("aaa");
//        list.add("ccc");
//        HashSet<String> list2 = new HashSet<String>();
//        list2.add("bbb");
//        list2.add("aaa");
//        list2.add("ddd");
//        list.addAll(list2);
        listAll = new HashSet<String>();
        file2Set("D:\\encry\\words3.txt");
        file2Set("D:\\encry\\word.txt");

//        hashSet.addAll(hashSet1);

        try {
            FileWriter writer = new FileWriter("D:\\encry\\words4.txt");
            BufferedWriter bw = new BufferedWriter(writer);
            for (String str : listAll) {
//                System.out.println(str);
                bw.write(str+"\r\n");
            }

            bw.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void testFile2() {


    }

    private static String file2String(String path) {
        StringBuffer sb= new StringBuffer("");
        File file = new File(path);
        BufferedReader reader = null;
        String temp = null;
        int line = 1;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((temp = reader.readLine()) != null) {
//                System.out.println("line" + line + ":" + temp);
                sb.append(temp+",");
                line++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private static HashSet file2Set(String path) {
        HashSet<String> list = new HashSet<String>();
        File file = new File(path);
        BufferedReader reader = null;
        String temp = null;
        int line = 1;
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((temp = reader.readLine()) != null) {
//                System.out.println("line" + line + ":" + temp);
//                sb.append(temp+",");
                list.add(temp);
                listAll.add(temp);
                line++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private static void testFile() {



        try {
            // read file content from file
            StringBuffer sb= new StringBuffer("");
            FileReader reader = new FileReader("D:\\encry\\words2.txt");
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            while((str = br.readLine()) != null) {
                String word = mParse2(str);
                if (!"".equals(word)) {
                    sb.append(word+"\r\n");
                }

            }
            br.close();
            reader.close();
            // write string to file
            FileWriter writer = new FileWriter("D:\\encry\\words3.txt");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(sb.toString());
            bw.close();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static String mParse2(String str) {
        if (ssss.contains(str)) {
            System.out.println("java word: " + ssss);
            return "";
        }
        return str.toString();
    }

    private static String mParse(String str) {
        StringBuilder pase = new StringBuilder("");
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= 'a' && str.charAt(i) <= 'z') {
                pase.append(str.charAt(i));
            }else if (pase.length() > 0) {
                if (ssss.contains(pase)) {
                    System.out.println("java word: " + ssss);
                    return "";
                }
                return pase.toString();
            }
        }
//        list = new ArrayList<>();
//        if (!"".equals(pase)) {
//            list.add(pase.toString());
//        }

        return "";
    }
}
