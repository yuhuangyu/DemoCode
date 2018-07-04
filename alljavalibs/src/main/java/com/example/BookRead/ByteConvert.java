package com.example.BookRead;

public abstract class ByteConvert
{
    public static ByteConvert Big = new BigByteConvert();
    public static ByteConvert Small = new SmallByteConvert();


    public abstract void toBytes(short s, byte b[], int index);

    public abstract void toBytes(int x, byte[] bb, int index);

    public abstract void toBytes(long x, byte[] bb, int index);

    public abstract void toBytes(char ch, byte[] bb, int index);

    public abstract void toBytes(float x, byte[] bb, int index);

    public abstract void toBytes(double x, byte[] bb, int index);

    public abstract short toShort(byte[] b, int index);

    public abstract int toInt(byte[] bb, int index);

    public abstract long toLong(byte[] bb, int index);

    public abstract char toChar(byte[] b, int index);

    public abstract float toFloat(byte[] b, int index);

    public abstract double toDouble(byte[] b, int index);

    public static class SmallByteConvert extends ByteConvert
    {
        public void toBytes(short s, byte b[], int index)
        {
            b[index + 0] = (byte) (s >> 8);
            b[index + 1] = (byte) (s >> 0);
        }

        public short toShort(byte[] b, int index)
        {
            return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
        }

        public void toBytes(int x, byte[] bb, int index)
        {
            bb[index + 0] = (byte) (x >> 24);
            bb[index + 1] = (byte) (x >> 16);
            bb[index + 2] = (byte) (x >> 8);
            bb[index + 3] = (byte) (x >> 0);
        }

        public int toInt(byte[] bb, int index)
        {
            return  ((((bb[index + 0] & 0xff) << 24) | ((bb[index + 1] & 0xff) << 16) | ((bb[index + 2] & 0xff) << 8) | ((bb[index + 3] & 0xff) << 0)));
        }

        public void toBytes(long x, byte[] bb, int index)
        {
            bb[index + 0] = (byte) (x >> 56);
            bb[index + 1] = (byte) (x >> 48);
            bb[index + 2] = (byte) (x >> 40);
            bb[index + 3] = (byte) (x >> 32);
            bb[index + 4] = (byte) (x >> 24);
            bb[index + 5] = (byte) (x >> 16);
            bb[index + 6] = (byte) (x >> 8);
            bb[index + 7] = (byte) (x >> 0);
        }

        public long toLong(byte[] bb, int index)
        {
            return ((((long) bb[index + 0] & 0xff) << 56) | (((long) bb[index + 1] & 0xff) << 48) | (((long) bb[index + 2] & 0xff) << 40) | (((long) bb[index + 3] & 0xff) << 32)
                    | (((long) bb[index + 4] & 0xff) << 24) | (((long) bb[index + 5] & 0xff) << 16) | (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));
        }

        public void toBytes(char ch, byte[] bb, int index)
        {
            int temp = (int) ch;
            // byte[] b = new byte[2];
            for (int i = 0; i < 2; i++)
            {
                bb[index + i] = new Integer(temp & 0xff).byteValue();
                temp = temp >> 8;
            }
        }

        public char toChar(byte[] b, int index)
        {
            int s = 0;
            if (b[index + 1] > 0)
                s += b[index + 1];
            else s += 256 + b[index + 0];
            s *= 256;
            if (b[index + 0] > 0)
                s += b[index + 1];
            else s += 256 + b[index + 0];
            char ch = (char) s;
            return ch;
        }

        public void toBytes(float x, byte[] bb, int index)
        {
            // byte[] b = new byte[4];
            int l = Float.floatToIntBits(x);
            for (int i = 0; i < 4; i++)
            {
                bb[index + 3 - i] = new Integer(l).byteValue();
                l = l >> 8;
            }
        }

        public float toFloat(byte[] b, int index)
        {
            int l;
            l = b[index + 3];
            l &= 0xff;
            l |= ((long) b[index + 2] << 8);
            l &= 0xffff;
            l |= ((long) b[index + 1] << 16);
            l &= 0xffffff;
            l |= ((long) b[index + 0] << 24);
            return Float.intBitsToFloat(l);
        }

        public void toBytes(double x, byte[] bb, int index)
        {
            // byte[] b = new byte[8];
            long l = Double.doubleToLongBits(x);
            for (int i = 0; i < 8; i++)
            {
                bb[index + 7 - i] = new Long(l).byteValue();
                l = l >> 8;
            }
        }

        public double toDouble(byte[] b, int index)
        {
            long l;
            l = b[7];
            l &= 0xff;
            l |= ((long) b[6] << 8);
            l &= 0xffff;
            l |= ((long) b[5] << 16);
            l &= 0xffffff;
            l |= ((long) b[4] << 24);
            l &= 0xffffffffl;
            l |= ((long) b[3] << 32);
            l &= 0xffffffffffl;
            l |= ((long) b[2] << 40);
            l &= 0xffffffffffffl;
            l |= ((long) b[1] << 48);
            l &= 0xffffffffffffffl;
            l |= ((long) b[0] << 56);
            return Double.longBitsToDouble(l);
        }
    }

    public static class BigByteConvert extends ByteConvert
    {
        public void toBytes(short s, byte b[], int index)
        {
            b[index + 1] = (byte) (s >> 8);
            b[index + 0] = (byte) (s >> 0);
        }

        public short toShort(byte[] b, int index)
        {
            return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
        }

        public void toBytes(int x, byte[] bb, int index)
        {
            bb[index + 3] = (byte) (x >> 24);
            bb[index + 2] = (byte) (x >> 16);
            bb[index + 1] = (byte) (x >> 8);
            bb[index + 0] = (byte) (x >> 0);
        }

        public int toInt(byte[] bb, int index)
        {
            return (int) ((((bb[index + 3] & 0xff) << 24) | ((bb[index + 2] & 0xff) << 16) | ((bb[index + 1] & 0xff) << 8) | ((bb[index + 0] & 0xff) << 0)));
        }

        public void toBytes(long x, byte[] bb, int index)
        {
            bb[index + 7] = (byte) (x >> 56);
            bb[index + 6] = (byte) (x >> 48);
            bb[index + 5] = (byte) (x >> 40);
            bb[index + 4] = (byte) (x >> 32);
            bb[index + 3] = (byte) (x >> 24);
            bb[index + 2] = (byte) (x >> 16);
            bb[index + 1] = (byte) (x >> 8);
            bb[index + 0] = (byte) (x >> 0);
        }

        public long toLong(byte[] bb, int index)
        {
            return ((((long) bb[index + 7] & 0xff) << 56) | (((long) bb[index + 6] & 0xff) << 48) | (((long) bb[index + 5] & 0xff) << 40) | (((long) bb[index + 4] & 0xff) << 32)
                    | (((long) bb[index + 3] & 0xff) << 24) | (((long) bb[index + 2] & 0xff) << 16) | (((long) bb[index + 1] & 0xff) << 8) | (((long) bb[index + 0] & 0xff) << 0));
        }

        public void toBytes(char ch, byte[] bb, int index)
        {
            int temp = (int) ch;
            // byte[] b = new byte[2];
            for (int i = 0; i < 2; i++)
            {
                bb[index + 1 - i] = new Integer(temp & 0xff).byteValue();
                temp = temp >> 8;
            }
        }

        public char toChar(byte[] b, int index)
        {
            int s = 0;
            if (b[index + 0] > 0)
                s += b[index + 0];
            else s += 256 + b[index + 1];
            s *= 256;
            if (b[index + 1] > 0)
                s += b[index + 0];
            else s += 256 + b[index + 1];
            char ch = (char) s;
            return ch;
        }

        public void toBytes(float x, byte[] bb, int index)
        {
            // byte[] b = new byte[4];
            int l = Float.floatToIntBits(x);
            for (int i = 0; i < 4; i++)
            {
                bb[index + i] = new Integer(l).byteValue();
                l = l >> 8;
            }
        }

        public float toFloat(byte[] b, int index)
        {
            int l;
            l = b[index + 0];
            l &= 0xff;
            l |= ((long) b[index + 1] << 8);
            l &= 0xffff;
            l |= ((long) b[index + 2] << 16);
            l &= 0xffffff;
            l |= ((long) b[index + 3] << 24);
            return Float.intBitsToFloat(l);
        }

        public void toBytes(double x, byte[] bb, int index)
        {
            // byte[] b = new byte[8];
            long l = Double.doubleToLongBits(x);
            for (int i = 0; i < 8; i++)
            {
                bb[index + i] = new Long(l).byteValue();
                l = l >> 8;
            }
        }

        public double toDouble(byte[] b, int index)
        {
            long l;
            l = b[0];
            l &= 0xff;
            l |= ((long) b[1] << 8);
            l &= 0xffff;
            l |= ((long) b[2] << 16);
            l &= 0xffffff;
            l |= ((long) b[3] << 24);
            l &= 0xffffffffl;
            l |= ((long) b[4] << 32);
            l &= 0xffffffffffl;
            l |= ((long) b[5] << 40);
            l &= 0xffffffffffffl;
            l |= ((long) b[6] << 48);
            l &= 0xffffffffffffffl;
            l |= ((long) b[7] << 56);
            return Double.longBitsToDouble(l);
        }
    }

}
