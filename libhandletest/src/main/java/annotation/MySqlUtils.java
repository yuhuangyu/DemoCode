package annotation;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by fj on 2018/8/7.
 */

public class MySqlUtils {
    private static final String ENCRYPTTYPE= "AES";//加密方式

    private static final String ENCODING = "UTF-8";//加密时编码

    private static String MYSQLUTILSKEY = "aaa";//加密密盐

    private static MySqlUtils mysqlUtils;//单例

    private static Cipher encryptCipher ;//加密cipher

    private static Cipher decryptChipher;//解密chipher

    /**
     * 该方法可用在spring项目中使用配置文件设置密盐，默认值为123
     * @param key
     */
//    @Value("${mysql.column.crypt.key:123}")
    public void setMysqlutilskey(String key){
        MySqlUtils.MYSQLUTILSKEY = key;
    }


    /**
     * encryptCipher、decryptChipher初始化
     */
    public static void init(){
        try {
            encryptCipher = Cipher.getInstance(ENCRYPTTYPE);
            decryptChipher = Cipher.getInstance(ENCRYPTTYPE);
            encryptCipher.init(Cipher.ENCRYPT_MODE, generateMySQLAESKey(MYSQLUTILSKEY, ENCODING));
            decryptChipher.init(Cipher.DECRYPT_MODE, generateMySQLAESKey(MYSQLUTILSKEY, ENCODING));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 单例获取方法实现
     * @return
     */
    public synchronized static MySqlUtils getInstance(){
        if(mysqlUtils == null){
            mysqlUtils = new MySqlUtils();
            init();
        }
        return mysqlUtils;
    }


    /**
     * 加密算法
     * @param encryptString
     * @return
     */
    public String mysqlAESEncrypt(String encryptString) {
        try{
            return new String(Hex.encodeHex(encryptCipher.doFinal(encryptString.getBytes(ENCODING)))).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密算法
     * @param decryptString
     * @return
     */
    public String mysqlAESDecrypt(String decryptString){
        try {
            return new String(decryptChipher.doFinal(Hex.decodeHex(decryptString.toCharArray())));
        } catch (Exception ike) {
            throw new RuntimeException(ike);
        }
    }

    /**
     * 产生mysql-aes_encrypt
     * @param key 加密的密盐
     * @param encoding  编码
     * @return
     */
    public static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) {
        try {
            final byte[] finalKey = new byte[16];
            int i = 0;
            for(byte b : key.getBytes(encoding))
                finalKey[i++%16] ^= b;
            return new SecretKeySpec(finalKey, "AES");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
