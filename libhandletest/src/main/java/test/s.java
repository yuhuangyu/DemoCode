package test;

/**
 * Created by fj on 2018/10/11.
 */

public final class s
{
    /*public static byte[] r(String paramString, int paramInt)
    {
        return r(paramString.getBytes(), paramInt);
    }

    public static byte[] r(byte[] paramArrayOfByte, int paramInt)
    {
        return r(paramArrayOfByte, 0, paramArrayOfByte.length, paramInt);
    }

    private static byte[] r(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
    {
        r localr = new r(paramInt3, new byte[paramInt2 * 3 / 4]);
        if (!localr.r(paramArrayOfByte, paramInt1, paramInt2, true))
            throw new IllegalArgumentException("bad base-64");
        if (localr.s == localr.r.length)
            return localr.r;
        byte[] arrayOfByte = new byte[localr.s];
        System.arraycopy(localr.r, 0, arrayOfByte, 0, localr.s);
        return arrayOfByte;
    }

    private static class r extends ss
    {
        private static final int[] m = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
        private static final int[] v = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
        private final int[] a;
        private int b;
        private int e;

        private r(int paramInt, byte[] paramArrayOfByte)
        {
            this.r = paramArrayOfByte;
            int[] arrayOfInt;
            if ((paramInt & 0x8) == 0)
                arrayOfInt = v;
            else
                arrayOfInt = m;
            this.a = arrayOfInt;
            this.e = 0;
            this.b = 0;
        }

        public boolean r(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
        {
            if (this.e == 6)
                return false;
            int i = paramInt2 + paramInt1;
            int j = this.e;
            int k = this.b;
            byte[] arrayOfByte = this.r;
            int[] arrayOfInt = this.a;
            int n = k;
            int i1 = 0;
            while (paramInt1 < i)
            {
                if (j == 0)
                {
                    while (true)
                    {
                        int i8 = paramInt1 + 4;
                        if (i8 > i)
                            break;
                        n = arrayOfInt[(0xFF & paramArrayOfByte[paramInt1])] << 18 | arrayOfInt[(0xFF & paramArrayOfByte[(paramInt1 + 1)])] << 12 | arrayOfInt[(0xFF & paramArrayOfByte[(paramInt1 + 2)])] << 6 | arrayOfInt[(0xFF & paramArrayOfByte[(paramInt1 + 3)])];
                        if (n < 0)
                            break;
                        arrayOfByte[(i1 + 2)] = ((byte)n);
                        arrayOfByte[(i1 + 1)] = ((byte)(n >> 8));
                        arrayOfByte[i1] = ((byte)(n >> 16));
                        i1 += 3;
                        paramInt1 = i8;
                    }
                    if (paramInt1 >= i)
                        break;
                }
                int i4 = paramInt1 + 1;
                int i5 = arrayOfInt[(0xFF & paramArrayOfByte[paramInt1])];
                switch (j)
                {
                    default:
                        break;
                    case 5:
                        if (i5 == -1)
                            break label519;
                        this.e = 6;
                        return false;
                    case 4:
                        if (i5 == -2)
                        {
                            j++;
                            break label519;
                        }
                        if (i5 == -1)
                            break label519;
                        this.e = 6;
                        return false;
                    case 3:
                        if (i5 >= 0)
                        {
                            int i7 = i5 | n << 6;
                            arrayOfByte[(i1 + 2)] = ((byte)i7);
                            arrayOfByte[(i1 + 1)] = ((byte)(i7 >> 8));
                            arrayOfByte[i1] = ((byte)(i7 >> 16));
                            i1 += 3;
                            n = i7;
                            j = 0;
                            break label519;
                        }
                        if (i5 == -2)
                        {
                            arrayOfByte[(i1 + 1)] = ((byte)(n >> 2));
                            arrayOfByte[i1] = ((byte)(n >> 10));
                            i1 += 2;
                            j = 5;
                            break label519;
                        }
                        if (i5 == -1)
                            break label519;
                        this.e = 6;
                        return false;
                    case 2:
                        if (i5 < 0)
                        {
                            if (i5 == -2)
                            {
                                int i6 = i1 + 1;
                                arrayOfByte[i1] = ((byte)(n >> 4));
                                i1 = i6;
                                j = 4;
                                break label519;
                            }
                            if (i5 == -1)
                                break label519;
                            this.e = 6;
                            return false;
                        }
                    case 1:
                        if (i5 >= 0)
                        {
                            i5 |= n << 6;
                            break label495;
                        }
                        if (i5 == -1)
                            break label519;
                        this.e = 6;
                        return false;
                    case 0:
                }
                if (i5 >= 0)
                {
                    label495: j++;
                    n = i5;
                }
                else if (i5 != -1)
                {
                    this.e = 6;
                    return false;
                }
                label519: paramInt1 = i4;
            }
            if (!paramBoolean)
            {
                this.e = j;
                this.b = n;
            }
            while (true)
            {
                this.s = i1;
                return true;
                switch (j)
                {
                    default:
                        break;
                    case 4:
                        this.e = 6;
                        return false;
                    case 3:
                        int i3 = i1 + 1;
                        arrayOfByte[i1] = ((byte)(n >> 10));
                        i1 = i3 + 1;
                        arrayOfByte[i3] = ((byte)(n >> 2));
                        break;
                    case 2:
                        int i2 = i1 + 1;
                        arrayOfByte[i1] = ((byte)(n >> 4));
                        i1 = i2;
                        break;
                    case 1:
                        this.e = 6;
                        return false;
                    case 0:
                }
                this.e = j;
            }
        }
    }

    static abstract class ss
    {
        byte[] r;
        int s;
    }*/
}
