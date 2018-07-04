package com.example.BookRead;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MyClass {

    /*|0x0002     |标题 |
|0x0003     |作者|
|0x0004     |出版年份|
|0x0005     |出版月份|
|0x0006     |出版日|
|0x0007     |小说类型|
|0x0008     |出版商|
|0x0009     |零售商|
|0x000b     |小说未压缩时的内容总长度（字节|
|0x0081     |正文结束|
|0x0082     |封面|
|0x0083     |章节|
|0x0084     |标题|*/

    private static int count;
    private static int num = 1;
    private static int type = 0;
    private static int length = 0;

    public static void main(final String[] args)
    {

        try {
            InputStream fs = new FileInputStream(new File("D:\\test\\mfb.umd"));
            BinaryReader binaryReader = new BinaryReader(fs,ByteConvert.Big);
            binaryReader.readInt();
            while(binaryReader.available()){

                byte b = binaryReader.readByte();
                if (b == '#') {
                    short i = binaryReader.readShort();
                    System.out.println("===---"+i);
                    switch (i){
                        case 0x0001:
                            binaryReader.readByte();
                            binaryReader.readByte();
                            binaryReader.readByte();
                            binaryReader.readShort();
                            break;
                        case 0x0002:
                            ;
                        case 0x0003:
                            ;
                        case 0x0004:
                            ;
                        case 0x0005:
                            ;
                        case 0x0006:
                            ;
                        case 0x0007:
                            ;
                        case 0x0008:
                            ;
                        case 0x0009:
                            ;
                        case 0x000b:
                            binaryReader.readByte();
                            byte size1 = binaryReader.readByte();
                            String s = binaryReader.readString(size1 - 5, "utf-16le");
                            System.out.println("===: "+s);
                            break;
                        case 0x0c:
                            binaryReader.readByte();
                            byte size00 = binaryReader.readByte();
                            binaryReader.skip(size00);
                            System.out.println("0x0c");
                            break;
                        case 0x0a:
                            binaryReader.readByte();
                            byte size000 = binaryReader.readByte();
                            binaryReader.skip(size000);
                            System.out.println("0x0a");
                            break;
                        case 0x81:
                            binaryReader.readByte();
                            byte size2 = binaryReader.readByte();
                            byte[] Data = new byte[(int) (size2 - 5)];
                            binaryReader.read(Data, 0,Data.length);
                            binaryReader.readInt();
                            System.out.println("0x81");
                            break;
                        case 0x82:
                            binaryReader.readByte();
                            int size3 = binaryReader.readByte();
                            byte[] Data2 = new byte[(int) (size3 - 5)];
                            binaryReader.read(Data2, 0,Data2.length);
                            binaryReader.readByte();
                            binaryReader.readInt();
                            int i1 = binaryReader.readInt();
                            binaryReader.skip(i1-9);
                            System.out.println("0x82");
                            break;
                        case 0x83:
                            binaryReader.readByte();
                            byte size4 = binaryReader.readByte();
                            binaryReader.readString(size4 - 5, "utf-16le");
                            for (int j = 0; j < (size4 - 5)/4; j++) {
                                binaryReader.readInt();
                            }
                            binaryReader.readInt();
                            int i2 = binaryReader.readInt();
                            binaryReader.skip(i2-9);
                            System.out.println("0x83");
                            break;
                        case 0x84:
                            binaryReader.readByte();
                            byte size5 = binaryReader.readByte();
                            binaryReader.readString(size5 - 5, "utf-16le");
                            binaryReader.readByte();
                            binaryReader.readInt();
                            int i3 = binaryReader.readInt();
//                            binaryReader.skip(i3-9);
                            while(true){
                                int len = binaryReader.readByte();
                                String s1 = binaryReader.readString(len, "utf-16le");
                                System.out.println("章节===: "+s1);
                                length += len+1;
                                if (length >= (i3-9)) {
                                    break;
                                }
                            }

                            break;
                    }
                }else {
                    binaryReader.readInt();
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void nowName(int type, StringBuffer st) {
        if (type == 4) {
            StringBuffer title = st;
            System.out.println("标题:"+title);
        }
    }


}
