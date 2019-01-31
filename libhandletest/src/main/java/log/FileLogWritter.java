package log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by fj on 2018/8/16.
 */

public class FileLogWritter implements LogWritter{
    private PrintStream _writer;
    private String path;

    public FileLogWritter(String path){
        this.path = path;
    }

    @Override
    public void Writter(int level, String msg, String formatterMsg) {
        if (_writer == null)
            initWriter();


//        if (level == null) {
//
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                _writer.println(formatterMsg);
            }
        }).start();
    }

    private synchronized void initWriter()
    {
        if (path == null)
        {
            _writer = System.out;
            return;
        }

        if (_writer == null)
        {
            File file = new File(path);
            try
            {
                if (!file.exists())
                {
                    create(file);
                }

                _writer = new PrintStream(new FileOutputStream(file, true));
                return;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        _writer = System.out;
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
}
