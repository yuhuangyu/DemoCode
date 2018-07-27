package com.datastorage;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

public class IO
{
    static final int IOBuffLength = 1024 * 4;

    public static final int S_IRWXU = 00700;
    public static final int S_IRUSR = 00400;
    public static final int S_IWUSR = 00200;
    public static final int S_IXUSR = 00100;

    public static final int S_IRWXG = 00070;
    public static final int S_IRGRP = 00040;
    public static final int S_IWGRP = 00020;
    public static final int S_IXGRP = 00010;

    public static final int S_IRWXO = 00007;
    public static final int S_IROTH = 00004;
    public static final int S_IWOTH = 00002;
    public static final int S_IXOTH = 00001;

    //region Interface

    public interface IOProgress
    {
        void onChanged(long current);
    }

    public interface FileInputAccess
    {
        void onOpened(FileInputStream inputStream);
    }

    public interface FileOutputAccess
    {
        void onOpened(FileOutputStream outputStream) throws IOException;
    }

    public interface IOCancelProgress extends IOProgress
    {
        boolean isCancel();
    }

    //endregion

    //region Android Permission

    public static int setPermissions(String file, int mode)
    {
        return setPermissions(file, mode, -1, -1);
    }

    private static final Class<?>[] SIG_SET_PERMISSION = new Class<?>[]{
            String.class, int.class, int.class, int.class};

    public static int setPermissions(String file, int mode, int uid, int gid)
    {
        try
        {
            Class<?> clazz = Class.forName("android.os.FileUtils");
            Method method = clazz.getDeclaredMethod("setPermissions",
                    SIG_SET_PERMISSION);
            method.setAccessible(true);
            return (Integer) method.invoke(null, file, mode, uid, gid);
        }
        catch (Exception e)
        {
        }

        return -1;
    }

    //endregion

    //region Create File

    public static File create(String path) throws IOException
    {
        return create(new File(path));
    }

    public static File create(File file) throws IOException
    {
        if (!file.exists())
        {
            File parent = file.getParentFile();

            boolean mkdirs = false;
            boolean createFile;

            if (parent != null && !parent.exists())
                mkdirs = parent.mkdirs();

            createFile = file.createNewFile();

            if (!createFile)
                throw new IOException("newInstance file " + file + " failed because mkdirs:" + mkdirs);
        }

        return file;
    }

    //endregion

    //region Write

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
                progress.onChanged(current);
            }
        }
    }

    //endregion

    //region WriteTo

    public static void writeTo(byte[] data, File file) throws IOException
    {
        InputStream in = new ByteArrayInputStream(data);

        writeTo(in, file);

        in.close();
    }

    public static void writeTo(byte[] data, OutputStream out) throws IOException
    {
        InputStream in = new ByteArrayInputStream(data);

        write(in, out);

        in.close();
    }

    public static void writeTo(byte[] data, File file, boolean append) throws IOException
    {
        InputStream in = new ByteArrayInputStream(data);

        writeTo(in, file, append);

        in.close();
    }

    public static void writeTo(InputStream in, File to) throws IOException
    {
        FileOutputStream tarStream = null;

        try
        {
            create(to);

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
        writeTo(in, to, true);
    }

    public static void writeTo(InputStream in, File to, boolean append) throws IOException
    {
        FileOutputStream tarStream = null;

        try
        {
            create(to);

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
        writeTo(in, to, true, len, progress);
    }

    public static void writeTo(InputStream in, File to, IOProgress progress) throws IOException
    {
        writeTo(in, to, false, progress);
    }

    public static void appendTo(InputStream in, File to, IOProgress progress) throws IOException
    {
        writeTo(in, to, true, progress);
    }

    public static void writeTo(InputStream in, File to, boolean append, long len, IOProgress progress) throws IOException
    {
        FileOutputStream tarStream = null;

        try
        {
            create(to);

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

    public static void writeTo(InputStream in, File to, boolean append, IOProgress progress) throws IOException
    {
        FileOutputStream tarStream = null;

        try
        {
            create(to);

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
    public static void writeTo(File src, File tar) throws IOException
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
            create(tar);

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

    //endregion

    //region Reader

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

    public static String readString(InputStream io) throws IOException
    {
        InputStreamReader reader = new InputStreamReader(io);

        StringBuffer lines = new StringBuffer();

        char[] buffer = new char[4 * 1024];
        int len = 0;
        while ((len = reader.read(buffer, 0, buffer.length)) > 0)
        {
            lines.append(buffer, 0, len);
        }

        return lines.toString();
    }

    public static String readString(InputStream io, String charset) throws IOException
    {
        InputStreamReader reader = new InputStreamReader(io, charset);

        StringBuffer lines = new StringBuffer();

        char[] buffer = new char[4 * 1024];
        int len = 0;
        while ((len = reader.read(buffer, 0, buffer.length)) > 0)
        {
            lines.append(buffer, 0, len);
        }

        return lines.toString();
    }

    public static byte[] readBytes(InputStream io) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        write(io, out);

        return out.toByteArray();
    }

    public static byte[] readBytes(File file) throws IOException
    {
        return readBytes(new FileInputStream(file));
    }

    //endregion

    //region Safe

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

        create(tar);

        if (tar.length() != in.available())
        {
            FileOutputStream out = new FileOutputStream(tar);

            try
            {
                FileLock lock = out.getChannel().lock();

                if (tar.length() != in.available())
                {
                    IO.write(in, out);
                    lock.release();
                }
            }
            finally
            {
                out.close();
            }
        }
    }

    public static void input(File file, FileInputAccess access) throws IOException
    {
        FileInputStream inputStream = null;

        try
        {
            inputStream = new FileInputStream(file);
            access.onOpened(inputStream);
        }
        finally
        {
            if (inputStream != null)
                inputStream.close();
        }
    }

    public static void output(File file, FileOutputAccess access) throws IOException
    {
        FileOutputStream outputStream = null;

        try
        {
            outputStream = new FileOutputStream(file);
            access.onOpened(outputStream);
        }
        finally
        {
            if (outputStream != null)
                outputStream.close();
        }
    }

    public static void safeInput(File file, FileInputAccess access) throws IOException
    {
        FileInputStream inputStream = null;
        FileLock lock = null;

        try
        {
            inputStream = new FileInputStream(file);
            lock = inputStream.getChannel().lock();
            access.onOpened(inputStream);
        }
        finally
        {
            if (lock != null)
                lock.release();

            if (inputStream != null)
                inputStream.close();
        }
    }

    public static void safeOutput(File file, FileOutputAccess access) throws IOException
    {
        FileOutputStream outputStream = null;
        FileLock lock = null;
        try
        {
            outputStream = new FileOutputStream(file);
            lock = outputStream.getChannel().lock();
            access.onOpened(outputStream);
        }
        finally
        {
            if (lock != null)
                lock.release();

            if (outputStream != null)
                outputStream.close();
        }
    }

    //endregion

    //region Writer

    public static BufferedWriter writer(File file) throws IOException
    {
        create(file);
        return writer(new FileOutputStream(file));
    }

    public static BufferedWriter writer(OutputStream outputStream) throws FileNotFoundException
    {
        return new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    public static InputStream toStream(String text)
    {
        return new ByteArrayInputStream(text.getBytes());
    }

    //endregion

    //region File

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

            File[] files = src.listFiles();

            if (files != null)
                for (File file : files)
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
                IO.writeTo(src, new File(tar, src.getName()));
            else IO.writeTo(src, tar);
        }
        else
        {
            String path = tar.getPath();
            if (File.separator.equals(path.charAt(path.length() - 1) + ""))
                IO.writeTo(src, new File(tar, src.getName()));
            else IO.writeTo(src, tar);
        }
    }

    public static boolean delete(File file)
    {
        if (file.isDirectory())
        {
            String[] children = file.list();
            if (children != null)
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

    public static List<File> list(File file, FileFilter fileFilter)
    {
        List<File> list = new ArrayList<File>();
        listFiles(file, list, fileFilter);
        return list;
    }

    private static void listFiles(@NonNull File file, List<File> files, FileFilter fileFilter)
    {
        if (file.isDirectory())
        {
            File[] fileList = file.listFiles(fileFilter);
            if (fileList != null)
                for (File f : fileList)
                    listFiles(f, files, fileFilter);
        }
        else
            files.add(file);
    }

    //endregion
}
