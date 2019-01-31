package filetobyte;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.Security;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

/**
 * Created by fj on 2018/8/14.
 */

public class EncryptionDecryption {

    private static String strDefaultKey = "wfkey";

    /** 加密具工 */
    private Cipher encryptCipher = null;

    /** 密解具工 */
    private Cipher decryptCipher = null;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * 将byte数组转换为表现16进制的字符串
     * @param arrB 须要转换的byte数组
     * @return 16进制表现的字符串
     * @throws Exception
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception{
        int bLen = arrB.length;
        //每一个字符占用两个字节，所以字符串的度长需是数组度长的2倍
        StringBuffer strBuffer = new StringBuffer(bLen*2);
        for(int i=0; i != bLen; ++i){
            int intTmp = arrB[i];
            //把正数转化为正数
            while(intTmp < 0){
                intTmp = intTmp + 256;//因为字一个字节是8位，从低往高数，第9位为符号为，加256，相当于在第九位加1
            }
            //小于0F的数据须要在后面补0，(因为原来是一个字节，在现成变String是两个字节，如果小于0F的话，明说大最也盛不满第一个字节。第二个需弥补0)
            if(intTmp < 16){
                strBuffer.append("0");
            }
            strBuffer.append(Integer.toString(intTmp,16));
        }
        return strBuffer.toString();
    }


    /**
     * 将表现16进制的字符串转化为byte数组
     * @param hexStr
     * @return
     * @throws Exception
     */
    public static byte[] hexStr2ByteArr(String hexStr) throws Exception{
        byte[] arrB = hexStr.getBytes();
        int bLen = arrB.length;
        byte[] arrOut = new byte[bLen/2];
        for(int i=0; i<bLen; i = i+2){
            String strTmp = new String(arrB,i,2);
            arrOut[i/2] = (byte)Integer.parseInt(strTmp,16);
        }
        return arrOut;
    }

    /**
     * 认默构造器，应用认默密匙
     * @throws Exception
     */
    public EncryptionDecryption() throws Exception {
        this(strDefaultKey);
    }



    /**
     * 指定密匙构造方法
     * @param strKey 指定的密匙
     * @throws Exception
     */
    @SuppressWarnings("restriction")
    public EncryptionDecryption(String strKey) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());

        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * 加密字节数组
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) throws Exception{
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String strIn) throws Exception{
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 密解字节数组
     * @param arrB 需密解的字节数组
     * @return 密解后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) throws Exception{
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 密解字符串
     * @param strIn 需密解的字符串
     * @return 密解后的字符串
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception{
        try{
            return new String(decrypt(hexStr2ByteArr(strIn)));
        }catch (Exception e) {
            return "";
        }
    }


    /**
     * 从指定字符串生成密匙，密匙所需的字节数组度长为8位，缺乏8位时，面后补0，超越8位时，只取后面8位
     * @param arrBTmp 成构字符串的字节数组
     * @return 生成的密匙
     * @throws Exception
     */
    private Key getKey(byte[] arrBTmp) throws Exception{
        byte[] arrB = new byte[8]; //认默为0
        for(int i=0; i<arrBTmp.length && i < arrB.length; ++i){
            arrB[i] = arrBTmp[i];
        }

        //生成密匙
        Key key = new javax.crypto.spec.SecretKeySpec(arrB,"DES");
        return key;
    }


    @SuppressWarnings("static-access")
    //文件加密的实现方法
    public static void encryptFile(String fileName, String encryptedFileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            FileOutputStream fos = new FileOutputStream(encryptedFileName);
            //秘钥自动生成
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            Key key = keyGenerator.generateKey();
            byte[] keyValue = key.getEncoded();
            fos.write(keyValue);//记录输入的加密密码的消息摘要
            SecretKeySpec encryKey = new SecretKeySpec(keyValue, "AES");//加密秘钥
            byte[] ivValue = new byte[16];
            Random random = new Random(System.currentTimeMillis());
            random.nextBytes(ivValue);
            IvParameterSpec iv = new IvParameterSpec(ivValue);//获取系统时间作为IV
            fos.write("MyFileEncryptor".getBytes());//文件标识符
            fos.write(ivValue);    //记录IV
            Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            cipher.init(cipher.ENCRYPT_MODE, encryKey, iv);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, n);
            }
            cis.close();
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-access")
    //文件解密的实现代码
    public static void decryptedFile(String encryptedFileName, String decryptedFileName) {
        try {
            FileInputStream fis = new FileInputStream(encryptedFileName);
            FileOutputStream fos = new FileOutputStream(decryptedFileName);
            byte[] fileIdentifier = new byte[15];
            byte[] keyValue = new byte[16];
            fis.read(keyValue);//读记录的文件加密密码的消息摘要
            fis.read(fileIdentifier);
            if (new String(fileIdentifier).equals("MyFileEncryptor")) {
                SecretKeySpec key = new SecretKeySpec(keyValue, "AES");
                byte[] ivValue = new byte[16];
                fis.read(ivValue);//获取IV值
                IvParameterSpec iv = new IvParameterSpec(ivValue);
                Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
                cipher.init(cipher.DECRYPT_MODE, key, iv);
                CipherInputStream cis = new CipherInputStream(fis, cipher);
                byte[] buffer = new byte[1024];
                int n = 0;
                while ((n = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, n);
                }
                cis.close();
                fos.close();
                JOptionPane.showMessageDialog(null, "解密成功");
            } else {
                JOptionPane.showMessageDialog(null, "文件不是我加密的，爱找谁着谁去");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
