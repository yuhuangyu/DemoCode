package com.example.BookRead;

import java.io.IOException;
import java.io.InputStream;

public class BinaryReader
{
    InputStream _stream;
    byte[] _readBuf;
    ByteConvert _convert;

    public BinaryReader(InputStream stream, ByteConvert convert)
    {
        _stream = stream;
        _readBuf = new byte[1024];
        _convert = convert;
    }

    public void close() throws IOException
    {
        _stream.close();
    }

    public void skip(long n) throws IOException
    {
        _stream.skip(n);
    }


    public int read(byte[] cbuf, int off, int len) throws IOException
    {
        return _stream.read(cbuf, off, len);
    }

    public int readInt() throws IOException
    {
        int len;
        len = _stream.read(_readBuf, 0, 4);
        if (len != 4)
            throw new IOException("stream is end");

        return _convert.toInt(_readBuf, 0);
    }

    public long readLong() throws IOException
    {
        int len;
        len = _stream.read(_readBuf, 0, 8);
        if (len != 8)
            throw new IOException("stream is end");

        return _convert.toLong(_readBuf, 0);
    }

    public short readShort() throws IOException
    {
        int len;
        len = _stream.read(_readBuf, 0, 2);
        if (len != 2)
            throw new IOException("stream is end");

        return _convert.toShort(_readBuf, 0);
    }

    public float readFloat() throws IOException
    {
        int len;
        len = _stream.read(_readBuf, 0, 4);
        if (len != 4)
            throw new IOException("stream is end");

        return _convert.toFloat(_readBuf, 0);
    }

    public double readDouble() throws IOException
    {
        int len;
        len = _stream.read(_readBuf, 0, 8);
        if (len != 8)
            throw new IOException("stream is end");

        return _convert.toDouble(_readBuf, 0);
    }

    public String readString(int strlen) throws IOException
    {
        int len;
        len = _stream.read(_readBuf, 0, strlen);
        if (len != strlen)
            throw new IOException("stream is end");

        return new String(_readBuf, 0, len, "Utf-8");
    }

    public String readString(int strlen, String str) throws IOException
    {
        int len;
        len = _stream.read(_readBuf, 0, strlen);
        if (len != strlen)
            throw new IOException("stream is end");

        return new String(_readBuf, 0, len, str);
    }

    public byte read() throws IOException
    {
        int len;
        len = _stream.read(_readBuf, 0, 1);
        if (len != 1)
            throw new IOException("stream is end");

        return _readBuf[0];
    }

    public byte readByte() throws IOException
    {
        int res = _stream.read();
        if (res == -1)
            throw new IOException("stream is end");

        return (byte) res;
    }

    public int readULeb128() throws IOException
    {
        int value = 0;
        byte b = readByte();

        value = value | (b & 0x7f);

        if (b > 0x7f)
        {
            b = readByte();
            value = value | ((b & 0x7f) << 7);

            if (b > 0x7f)
            {
                b = readByte();
                value = value | ((b & 0x7f) << 14);

                if (b > 0x7f)
                {
                    b = readByte();
                    value = value | ((b & 0x7f) << 21);

                    if (b > 0x7f)
                    {
                        b = readByte();
                        value = value | ((b & 0x7f) << 28);
                    }
                }
            }
        }

        return value;
    }


    public boolean available() {
        try {
            if (_stream.available() != 0) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
