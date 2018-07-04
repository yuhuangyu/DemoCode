package com.example.FileList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anye6488 on 2017/6/1.
 */

public class Main
{
    public static class MatchInfo
    {
        File File;
        int Index;
    }

    public static void main(final String[] args)
    {

        final List<MatchInfo> list = new ArrayList<>();
        list(new File(args[0]), new FileAccess()
        {
            @Override
            public void access(File file)
            {
                try
                {
                    System.out.print("\rfind " + file);
                    int lineNum = findString(file, args[1]);

                    if (lineNum != -1)
                    {
                        MatchInfo info = new MatchInfo();
                        info.File = file;
                        info.Index = lineNum;
                        list.add(info);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        for (MatchInfo i : list)
        {
            System.out.println("find at " + i.File + " : " + (i.Index + 1));
        }
    }

    public interface FileAccess
    {
        void access(File file);
    }

    static void list(File file, FileAccess access)
    {
        if (file.isFile())
            access.access(file);
        else
        {
            for (File f : file.listFiles())
                list(f, access);
        }
    }

    static int findEnter(char[] buff, int index, int len)
    {
        int count = 0;
        for (int i = index; i < index + len; i++)
            if (buff[i] == '\n')
            {
                count++;
            }

        return count;
    }

    static int findString(File file, String text) throws IOException
    {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file));

        char[] buff = new char[text.length() + 4 * 1024];
        char[] lastBuff = null, newBuff = null;
        int count = 0;
        int lineCount = 0;

        try
        {
            while (true)
            {
                if (newBuff != null)
                    buff = newBuff;

                count = reader.read(buff, text.length(), buff.length - text.length());

                if (count > 0)
                {
                    if (lastBuff != null)
                    {
                        System.arraycopy(lastBuff, buff.length - text.length(), buff, 0, text.length());
                    }

                    int index = new String(buff, 0, count + text.length()).lastIndexOf(text);
                    if (index != -1)
                    {
                        if (index < text.length())
                            return lineCount;
                        else
                        {
                            lineCount += findEnter(buff, text.length(), index - text.length());
                            return lineCount;
                        }
                    }

                    lineCount += findEnter(buff, text.length(), index - text.length());

                    if (lastBuff != null)
                        newBuff = lastBuff;
                    else
                        newBuff = new char[text.length() + 4 * 1024];

                    lastBuff = buff;
                }
                else
                    return -1;
            }
        }
        finally
        {
            reader.close();
        }
    }
}
