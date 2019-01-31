package com.example.designpatterns.download;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileLock;

public class IOManager
{
    static final int IOBuffLength = 1024 * 4;

    public interface IOProgress
    {
        void onChanged(long current);
    }

    public interface IOCancelProgress extends IOProgress
    {
        boolean isCancel();
    }

    public static File create(String path) throws IOException
    {
        File file = new File(path);
        if (!file.exists())
        {
            File parent = file.getParentFile();

            if (parent != null && !parent.exists())
                parent.mkdirs();

            file.createNewFile();
        }

        return file;
    }

    public static void write(InputStream in, OutputStream out) throws IOException
    {
        byte[] buff = new byte[IOBuffLength];
        int buffLen;

        while ((buffLen = in.read(buff, 0, buff.length)) != -1)
        {
            out.write(buff, 0, buffLen);
        }
    }

    public static void write(InputStream in, OutputStream out, IOProgress progress) throws IOException
    {
        byte[] buff = new byte[IOBuffLength];
        int buffLen;
        long current = 0;
        IOCancelProgress cancel = null;
        if (progress instanceof IOCancelProgress)
            cancel = (IOCancelProgress) progress;

        if (cancel == null)
        {
            while ((buffLen = in.read(buff, 0, buff.length)) != -1)
            {
                current += buffLen;
                out.write(buff, 0, buffLen);
                if (progress != null)
                    progress.onChanged(current);
            }
        }
        else
        {
            while (!cancel.isCancel() && (buffLen = in.read(buff, 0, buff.length)) != -1)
            {
                current += buffLen;
                out.write(buff, 0, buffLen);
                if (progress != null)
                    progress.onChanged(current);
            }
        }
    }

    public static void write(InputStream in, OutputStream out, long len, IOProgress progress) throws IOException
    {
        byte[] buff = new byte[IOBuffLength];
        int buffLen = -1;
        long current = 0;
        int readLen = (int) (len > IOBuffLength ? IOBuffLength : len);
        IOCancelProgress cancel = null;
        if (progress instanceof IOCancelProgress)
            cancel = (IOCancelProgress) progress;

        if (cancel == null)
        {
            while ((buffLen = in.read(buff, 0, readLen)) != -1)
            {
                current += buffLen;
                len -= buffLen;
                readLen = (int) (len > IOBuffLength ? IOBuffLength : len);
                out.write(buff, 0, buffLen);
                if (progress != null)
                    progress.onChanged(current);
            }
        }
        else
        {
            while (!cancel.isCancel() && (buffLen = in.read(buff, 0, readLen)) != -1)
            {
                current += buffLen;
                len -= buffLen;
                readLen = (int) (len > IOBuffLength ? IOBuffLength : len);
                out.write(buff, 0, buffLen);
                if (progress != null)
                    progress.onChanged(current);
            }
        }
    }

    public static BufferedReader openRead(File file) throws FileNotFoundException
    {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    }

    public static void writeTo(byte[] data, File file) throws IOException
    {
        InputStream in = new ByteArrayInputStream(data);

        moveTo(in, file);

        in.close();
    }

    public static void writeTo(byte[] data, File file, boolean append) throws IOException
    {
        InputStream in = new ByteArrayInputStream(data);

        moveTo(in, file, append);

        in.close();
    }

    public static BufferedReader reader(File file) throws FileNotFoundException
    {
        return reader(new FileInputStream(file));
    }

    public static BufferedReader reader(InputStream io)
    {
        return new BufferedReader(new InputStreamReader(io));
    }

    public static BufferedReader reader(InputStream io, String en) throws UnsupportedEncodingException
    {
        return new BufferedReader(new InputStreamReader(io, en));
    }

    public static String readToEnd(InputStream io) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(io));

        StringBuffer lines = new StringBuffer();

        String line;
        while ((line = reader.readLine()) != null)
            lines.append(line + "\n");

        return lines.toString();
    }

    public static String readToEnd(InputStream io, String en) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(io, en));

        StringBuffer lines = new StringBuffer();

        String line;
        while ((line = reader.readLine()) != null)
            lines.append(line + "\n");

        return lines.toString();
    }

    public static byte[] readAll(File file) throws IOException
    {
        InputStream in = new FileInputStream(file);

        byte[] data = readAll(in);

        in.close();

        return data;
    }

    public static byte[] readAll(InputStream io) throws IOException
    {
        byte[] buff = new byte[io.available()];

        io.read(buff);

        return buff;
    }

    public static byte[] readBytes(InputStream io) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        write(io, out);

        return out.toByteArray();
    }


    public static void moveTo(InputStream in, File to) throws IOException
    {
        FileOutputStream tarStream = null;

        try
        {
            if (!to.exists())
            {
                if (!to.getParentFile().exists())
                    to.getParentFile().mkdirs();
                to.createNewFile();
            }

            if (!to.canWrite())
                throw new IOException("file " + to.getPath() + " can not write");

            InputStream srcStream = in;
            tarStream = new FileOutputStream(to);

            write(srcStream, tarStream);

        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
        finally
        {
            if (tarStream != null)
                tarStream.close();
        }
    }

    public static void appendTo(InputStream in, File to) throws IOException
    {
        moveTo(in, to, true);
    }


    public static void moveTo(InputStream in, File to, boolean append) throws IOException
    {
        FileOutputStream tarStream = null;

        try
        {
            if (!to.exists())
            {
                if (!to.getParentFile().exists())
                    to.getParentFile().mkdirs();
                to.createNewFile();
            }

            if (!to.canWrite())
                throw new IOException("file " + to.getPath() + " can not write");

            InputStream srcStream = in;
            tarStream = new FileOutputStream(to, append);

            write(srcStream, tarStream);

        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
        finally
        {
            if (tarStream != null)
                tarStream.close();
        }
    }

    public static void appendTo(InputStream in, File to, long len, IOProgress progress) throws IOException
    {
        moveTo(in, to, true, len, progress);
    }

    public static void moveTo(InputStream in, File to, IOProgress progress) throws IOException
    {
        moveTo(in, to, false, progress);
    }

    public static void appendTo(InputStream in, File to, IOProgress progress) throws IOException
    {
        moveTo(in, to, true, progress);
    }

    public static void moveTo(InputStream in, File to, boolean append, long len, IOProgress progress) throws IOException
    {
        FileOutputStream tarStream = null;

        try
        {
            if (!to.exists())
            {
                if (to.getParentFile() != null && !to.getParentFile().exists())
                    to.getParentFile().mkdirs();
                to.createNewFile();
            }

            if (!to.canWrite())
                throw new IOException("file:" + to.getPath() + " can not write");

            InputStream srcStream = in;
            tarStream = new FileOutputStream(to, append);

            write(srcStream, tarStream, len, progress);

            tarStream.close();
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
        finally
        {
            if (in != null)
                in.close();
            if (to != null && tarStream != null)
                tarStream.close();
        }
    }

    public static void moveTo(InputStream in, File to, boolean append, IOProgress progress) throws IOException
    {
        FileOutputStream tarStream = null;

        try
        {
            if (!to.exists())
            {
                if (!to.getParentFile().exists())
                    to.getParentFile().mkdirs();
                to.createNewFile();
            }

            if (!to.canWrite())
                throw new IOException("file:" + to.getPath() + " can not write");

            InputStream srcStream = in;
            tarStream = new FileOutputStream(to, append);

            write(srcStream, tarStream, progress);

            tarStream.close();
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
        finally
        {
            if (in != null)
                in.close();
            if (to != null && tarStream != null)
                tarStream.close();
        }
    }

    /*
     * 移动文件src到tar
     */
    public static void moveTo(File src, File tar) throws IOException
    {
        if (src.getPath().equals(tar.getPath()))
            return;

        if (!src.exists())
        {
            throw new IOException("file:" + src.getPath() + "not exists");
        }

        if (!src.canRead())
        {
            throw new IOException("file:" + src.getPath() + "cannot read");
        }

        FileInputStream srcStream = null;
        FileOutputStream tarStream = null;

        try
        {
            if (!tar.exists())
                tar.createNewFile();

            if (!tar.canWrite())
            {
                throw new IOException("file:" + tar.getPath() + "cannot write");
            }

            srcStream = new FileInputStream(src);
            tarStream = new FileOutputStream(tar);

            write(srcStream, tarStream);

        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
        finally
        {
            if (srcStream != null)
                srcStream.close();

            if (tarStream != null)
                tarStream.close();
        }
    }

    public static InputStream toStream(String text)
    {
        return new ByteArrayInputStream(text.getBytes());
    }

    public static void copy(File src, File tar) throws IOException
    {
        if (src.getPath().equals(tar.getPath()))
            return;

        if (src.isDirectory())
        {
            if (!tar.exists())
            {
                tar.mkdirs();
            }

            if (!tar.isDirectory())
                throw new IOException("tar path not is Directory");

            for (File file : src.listFiles())
            {
                if (file.isFile())
                    copyFile(file, tar);
                else copy(file, new File(tar, file.getName()));
            }
        }
        else copyFile(src, tar);
    }

    public static void copyFile(File src, File tar) throws IOException
    {
        if (tar.exists())
        {
            if (tar.isDirectory())
                IOManager.moveTo(src, new File(tar, src.getName()));
            else IOManager.moveTo(src, tar);
        }
        else
        {
            String path = tar.getPath();
            if (File.separator.equals(path.charAt(path.length() - 1) + ""))
                IOManager.moveTo(src, new File(tar, src.getName()));
            else IOManager.moveTo(src, tar);
        }
    }

    public static boolean delete(File file)
    {
        if (file.isDirectory())
        {
            String[] children = file.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = delete(new File(file, children[i]));
                if (!success)
                {
                    return false;
                }
            }
        }

        return file.delete();
    }

    public static void safeRelease(File src, File tar) throws IOException
    {
        InputStream in = new FileInputStream(src);
        try
        {
            safeRelease(in, tar);
        }
        finally
        {
            in.close();
        }
    }

    public synchronized static void safeRelease(InputStream src, File tar) throws IOException
    {
        InputStream in = src;

        if (!tar.exists())
        {
            if (tar.getParentFile() != null && !tar.getParentFile().exists())
                tar.getParentFile().mkdirs();

            tar.createNewFile();
        }

        if (tar.length() != in.available())
        {
            FileOutputStream out = new FileOutputStream(tar);
            try
            {
                FileLock lock = out.getChannel().lock();

                if (tar.length() != in.available())
                {
                    IOManager.write(in, out);
                    lock.release();
                }
            }
            finally
            {
                out.close();
            }
        }
    }
}
