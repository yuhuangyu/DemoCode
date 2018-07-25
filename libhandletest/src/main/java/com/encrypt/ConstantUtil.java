package com.encrypt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ConstantUtil {

    public static void writeFile(InputStream inputStream, OutputStream outputStream, int code) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int value = ~(dataInputStream.readInt() ^ code);

        Map<Integer, Integer> mapByte = decodeMap(value);

        // write
        byte[] buffWrite = new byte[4 * 1024];
        int readLen = 0;

        while ((readLen = inputStream.read(buffWrite, 0, buffWrite.length)) > 0) {
            for (int i = 0; i < readLen; i++) {
                if (mapByte.containsKey((int) buffWrite[i])) {
                    int mapValue = mapByte.get((int) buffWrite[i]);
                    buffWrite[i] = (byte) mapValue;
                }
            }
            outputStream.write(buffWrite, 0, readLen);
        }
    }

    private static Map<Integer, Integer> decodeMap(int value) {
        Map<Integer, Integer> mapByte = new HashMap<Integer, Integer>();
        Random randomTag = new Random(value);

        List<Integer> listValue = getListByte();

        while (listValue.size() > 1) {
            int index = randomTag.nextInt(listValue.size() - 1) + 1;
            mapByte.put(listValue.get(index), listValue.get(0));
            mapByte.put(listValue.get(0), listValue.get(index));
            listValue.remove(index);
            listValue.remove(0);
        }

        if (listValue.size() != 0) {
            mapByte.put(listValue.get(0), listValue.get(0));
        }
        return mapByte;
    }

    private static List<Integer> getListByte() {
        List<Integer> listValue = new ArrayList<Integer>();

        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            if (i != -1) {
                listValue.add((int) i);
            }
        }

        listValue.add((int) Byte.MAX_VALUE);
        return listValue;
    }


    /*
    *
    *      加密
    *
    * */

    public static Map<Integer, Integer> getMapping(int value){

        Map<Integer, Integer> map = new HashMap();
        Random random = new Random((long) value);
        List<Integer> charList = new ArrayList();
        for (byte i = -128; i < 127; ++i) {
            if (i != -1) {
                charList.add(Integer.valueOf(i));
            }
        }
        charList.add(Integer.valueOf(127));
        while (charList.size() > 1) {
            int index = random.nextInt(charList.size() - 1) + 1;
            map.put(charList.get(0), charList.get(index));
            map.put(charList.get(index), charList.get(0));
            charList.remove(index);
            charList.remove(0);
        }
        if (charList.size() != 0) {
            map.put(charList.get(0), charList.get(0));
        }
        return map;
    }

    public static int mapping(Map<Integer, Integer> map, int value){
        if (map.containsKey(Integer.valueOf(value))) {
            int mapValue = ((Integer) map.get(Integer.valueOf(value))).intValue();
            return mapValue;
        } else {
            return value;
        }
    }

    public static void write(OutputStream outputStream, Map<Integer, Integer> map, byte[] b, int off, int len) throws IOException {
        for (int i = off; i < len; ++i) {
            b[i] = (byte) mapping(map, b[i]);
        }
        outputStream.write(b, off, len);
    }

    public static void encrypt(InputStream inputStream, OutputStream outputStream, int code) throws IOException {

//        def subFile = project.buildFile.parentFile.parentFile
//        File srcFile = new File(subFile.getAbsolutePath(), "test01/build/libs/test01.app")
//        InputStream inputStream = new FileInputStream(srcFile);
//        File desFile = new File(subFile.getAbsolutePath(), "test01/build/assets/bbb_jiami")
//        if (!desFile.getParentFile().exists()) {
//            desFile.getParentFile().mkdirs();
//        } else {
//            File[] listFile = desFile.getParentFile().listFiles();
//            for (int i = 0; listFile != null && i < listFile.length; i++) {
//                listFile[i].delete();
//            }
//        }

//        OutputStream outputStream = new FileOutputStream(desFile);

        DataOutputStream dataOut = new DataOutputStream(outputStream);
        int v = (int) System.nanoTime();
        Map<Integer, Integer> mapByte = getMapping(v);
        v = ~v ^ 379;
        dataOut.writeInt(v);

        byte[] buff = new byte[4 * 1024];

        int buffLen;
        while ((buffLen = inputStream.read(buff, 0, buff.length)) != -1) {
            write(dataOut, mapByte, buff, 0, buffLen);
        }
        inputStream.close();
        dataOut.close();
    }

}



