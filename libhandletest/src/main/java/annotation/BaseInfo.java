package annotation;


import java.lang.reflect.Field;

/**
 * Created by fj on 2018/8/7.
 */

public class BaseInfo implements Cloneable, EncryptDecryptInterface {
    /**
     * 拷贝一个对象，并对新对象进行加密
     * 该方法主要用在日志打印上，可防止原对象被加密而影响程序执行
     * @param <T>
     * @return
     */
    public <T extends BaseInfo> T cloneAndEncrypt() {
        T cloneT = null;
        try {
            cloneT = (T) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        if(cloneT !=null)
            return cloneT.encryptSelf();
        throw new RuntimeException("拷贝对象异常");
    }

    /**
     * 重写clone方法
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 实现自加密
     *
     * @param <T>
     * @return
     */
    public <T> T encryptSelf() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(EncryptFiled.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(this);
                        if (fieldValue!=null && !fieldValue.isEmpty()) {
                            field.set(this, MySqlUtils.getInstance().mysqlAESEncrypt(fieldValue));
                        }
                        field.setAccessible(false);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (T) this;
    }

    /**
     * 实现自解密
     *
     * @param <T>
     * @return
     */
    public <T> T decryptSelf() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        try {
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(DecryptFiled.class) && field.getType().toString().endsWith("String")) {
                        field.setAccessible(true);
                        String fieldValue = (String)field.get(this);

                        if(fieldValue!=null && !fieldValue.isEmpty()) {
                            field.set(this, MySqlUtils.getInstance().mysqlAESDecrypt(fieldValue));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (T) this;
    }
}
