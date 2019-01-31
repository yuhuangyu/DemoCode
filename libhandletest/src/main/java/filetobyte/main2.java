package filetobyte;

import com.datastorage.Base64;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;

/**
 * Created by fj on 2018/8/15.
 */

public class main2 {

    public static void main(String[] args) {

        String var1 = "D:\\encry\\ccc.dex";
        String[] var0 = code2.c;
        StringBuilder var17 = new StringBuilder();
        int var5;
        for(var5 = 0; var5 < var0.length; ++var5) {
            var17.append(var0[var5]);
        }
        try {
            byte[] var20 = Base64.decode(var17.toString(), 0);
            FileOutputStream var19 = new FileOutputStream(var1);
            ByteArrayInputStream var15 = new ByteArrayInputStream(var20);
            if(var15 != null) {
                byte[] var21 = new byte[1024];
                int var22;
                while((var22 = var15.read(var21)) > 0) {
                    var19.write(var21, 0, var22);
                }
                var19.close();
                var15.close();
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }

//        String[][] var12 = new String[][]{code2.a, code2.b, code2.d, code2.e, code2.f, code2.g, code2.h, code2.k, code2.l, code3.m, code2.i, code2.j};
//        String[] aa = new String[]{"aaa"+ StringUtils.join(code2.a)};
    }

    public static boolean a(String[] paramArrayOfString, String paramString)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < paramArrayOfString.length; i++)
            localStringBuilder.append(paramArrayOfString[i]);
        FileOutputStream localFileOutputStream;
        ByteArrayInputStream localByteArrayInputStream;
        try
        {
            byte[] arrayOfByte1 = Base64.decode(localStringBuilder.toString(), 0);
            localFileOutputStream = new FileOutputStream(paramString);
            localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte1);
            if (localByteArrayInputStream != null)
            {
                byte[] arrayOfByte2 = new byte[1024];
                while (true)
                {
                    int j = localByteArrayInputStream.read(arrayOfByte2);
                    if (j <= 0)
                        break;
                    localFileOutputStream.write(arrayOfByte2, 0, j);
                }
            }
            localFileOutputStream.close();
            localByteArrayInputStream.close();
            return true;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return false;
    }


}
