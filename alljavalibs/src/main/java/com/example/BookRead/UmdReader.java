package com.example.BookRead;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;

/**
 * Created by anye6488 on 2017/6/14.
 * 注释暂时没有,BUG应该会有
 * 请耐心细心用心体会 ╮(╯▽╰)╭
 */

public class UmdReader
{
    public static void main(String[] arga) throws IOException
    {
        UmdReader umdReader = new UmdReader(new File("D:\\test\\魔法变.umd"));

        Book book = umdReader.book();

        System.out.print(book.meta(BookName));
    }

    public static final int BookName = 0x02;
    public static final int Author = 0x03;
    public static final int Year = 0x04;
    public static final int Month = 0x05;
    public static final int Day = 0x06;
    public static final int Type = 0x07;
    public static final int Bookman = 0x08;
    public static final int Retailer = 0x09;

    private BinaryReader _reader;

    public interface FileStruct
    {
        void read(BinaryReader reader) throws IOException;
    }

    public class FileHeader implements FileStruct
    {
        public int Magic;

        @Override
        public void read(BinaryReader reader) throws IOException
        {
            Magic = reader.readInt();
        }
    }


    public class ChunkHeader implements FileStruct
    {
        public byte Flag;
        public int Type;

        @Override
        public void read(BinaryReader reader) throws IOException
        {
            Flag = reader.readByte();
            if (Flag == '#')
                Type = reader.readShort();
            else
                Type = reader.readInt();
        }
    }

    public abstract class ContentChunk implements FileStruct
    {
        public ChunkHeader Header;

        private byte _size;

        public int size()
        {
            return _size - 5;
        }

        @Override
        public void read(BinaryReader reader) throws IOException
        {
            reader.readByte();
            _size = reader.readByte();

            readContent(reader);
        }

        public abstract void readContent(BinaryReader reader) throws IOException;
    }

    public abstract class DataChunk implements FileStruct
    {
        public ChunkHeader Header;

        private int _size;

        public int size()
        {
            return _size - 9;
        }

        @Override
        public void read(BinaryReader reader) throws IOException
        {
            if (Header == null)
            {
                Header = new ChunkHeader();
                Header.read(reader);
            }

            _size = reader.readInt();

            readData(reader);
        }

        public abstract void readData(BinaryReader reader) throws IOException;
    }

    public class ChapterDataChunk extends DataChunk
    {
        public String Content;

        @Override
        public void readData(BinaryReader reader) throws IOException
        {
            byte[] buff = new byte[size()];
            reader.read(buff, 0, buff.length);
            InflaterInputStream inputStream = new InflaterInputStream(new ByteArrayInputStream(buff));

            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, "utf-16le"));

            StringBuilder builder = new StringBuilder();

            String line = null;
            while ((line = r.readLine()) != null)
            {
                builder.append(line + "\n");
            }

            Content = builder.toString();
        }
    }

    public class FileChunk extends ContentChunk
    {
        public byte Type;

        @Override
        public void readContent(BinaryReader reader) throws IOException
        {
            Type = reader.readByte();
            reader.readShort();
        }
    }

    public class MetaChunk extends ContentChunk
    {
        public String Content;

        @Override
        public void readContent(BinaryReader reader) throws IOException
        {
            Content = reader.readString(size(), "utf-16le");
        }
    }

    public class CoverChunk extends ContentChunk
    {
        public byte Flag;
        public int Index;
        public byte[] Data;
        private DataChunk _data = new DataChunk()
        {
            @Override
            public void readData(BinaryReader reader) throws IOException
            {
                Data = new byte[(int) this.size()];
                reader.read(Data, 0, Data.length);
            }
        };

        @Override
        public void readContent(BinaryReader reader) throws IOException
        {
            Flag = reader.readByte();
            Index = reader.readInt();

            _data.read(reader);
        }
    }

    public class LicenceChunk extends ContentChunk
    {
        @Override
        public void readContent(BinaryReader reader) throws IOException
        {
            reader.skip(size());
        }
    }

    public class ContentEndChunk extends ContentChunk
    {
        public byte Flag;
        public int Index;
        public byte[] Data;
        private DataChunk _data = new DataChunk()
        {
            @Override
            public void readData(BinaryReader reader) throws IOException
            {
                Data = new byte[this.size()];
                reader.read(Data, 0, Data.length);
            }
        };

        @Override
        public void readContent(BinaryReader reader) throws IOException
        {
            Index = reader.readInt();

            _data.read(reader);
        }
    }

    public class Title implements FileStruct
    {
        private byte _size;
        private String Content;

        @Override
        public void read(BinaryReader reader) throws IOException
        {
            _size = reader.readByte();
            Content = reader.readString(_size, "utf-16le");
        }
    }

    public class CIDChunk extends ContentChunk
    {
        public String CID;

        @Override
        public void readContent(BinaryReader reader) throws IOException
        {
            CID = _reader.readString(size(), "utf-16le");
        }
    }

    public class ChapterTitleChunk extends ContentChunk
    {
        public int Index;
        public List<String> Titles = new ArrayList<String>();
        private DataChunk _data = new DataChunk()
        {
            @Override
            public void readData(BinaryReader reader) throws IOException
            {
                int size = 0;

                while (true)
                {
                    int len = reader.readByte();
                    Titles.add(reader.readString(len, "utf-16le"));
                    size += (len + 1);

                    if (size >= size())
                        break;
                }
            }
        };

        @Override
        public void readContent(BinaryReader reader) throws IOException
        {
            Index = reader.readInt();

            _data.read(reader);
        }
    }

    public class ChapterChunk extends ContentChunk
    {
        public int Index;
        public List<Integer> Indexs = new ArrayList<>();
        private DataChunk _data = new DataChunk()
        {
            @Override
            public void readData(BinaryReader reader) throws IOException
            {
                int count = size() / 4;
                for (int i = 0; i < count; i++)
                {
                    Indexs.add(reader.readInt());
                }
            }
        };

        @Override
        public void readContent(BinaryReader reader) throws IOException
        {
            Index = reader.readInt();

            _data.read(reader);
        }
    }

    public class Book
    {
        private List<Chapter> _chapters = new ArrayList<Chapter>();

        public Book()
        {
            ChapterChunk chapterChunk = find(ChapterChunk.class);
            ChapterTitleChunk chapterTitleChunk = find(ChapterTitleChunk.class);

            for (int i = 0; i < chapterChunk.size(); i++)
            {
                if (i < chapterChunk.size() - 1)
                {
                    Chapter chapter = new Chapter(chapterTitleChunk.Titles.get(i),
                            _data.substring(chapterChunk.Indexs.get(i) / 2, chapterChunk.Indexs.get(i + 1) / 2));
                    _chapters.add(chapter);
                }
                else
                {
                    Chapter chapter = new Chapter(chapterTitleChunk.Titles.get(i),
                            _data.substring(chapterChunk.Indexs.get(i) / 2));
                    _chapters.add(chapter);
                }

            }
        }

        public List<Chapter> chapters()
        {
            return _chapters;
        }

        public String meta(int type)
        {
            for (FileStruct struct : _structList)
            {
                if (struct instanceof MetaChunk)
                {
                    if (((MetaChunk) struct).Header.Type == type)
                        return ((MetaChunk) struct).Content;
                }
            }

            return "";
        }
    }

    public class Chapter
    {
        public String _title;
        public String _content;

        public Chapter(String title, String content)
        {
            _title = title;
            _content = content;
        }
    }

    private FileHeader _header = new FileHeader();
    private List<FileStruct> _structList = new ArrayList<FileStruct>();
    private StringBuilder _data = new StringBuilder();

    public UmdReader(File file) throws IOException
    {
        readFile(new FileInputStream(file));
    }

    public Book book()
    {
        return new Book();
    }

    private <T extends FileStruct> T find(Class<T> cls)
    {
        for (FileStruct struct : _structList)
        {
            if (cls.isAssignableFrom(struct.getClass()))
                return (T) struct;
        }

        return null;
    }

    private void readFile(InputStream inputStream) throws IOException
    {
        _reader = new BinaryReader(inputStream, ByteConvert.Big);


        _header.read(_reader);

        while (_reader.available())
        {
            ChunkHeader header = new ChunkHeader();

            header.read(_reader);
            System.out.println(header.Flag + " " + header.Type);
            ContentChunk struct = null;
            if (header.Flag == '$')
            {
                ChapterDataChunk chunk = new ChapterDataChunk();
                chunk.Header = header;
                chunk.read(_reader);
                _data.append(chunk.Content);
            }
            else
                switch (header.Type)
                {
                    case 0x01:
                        struct = new FileChunk();
                        break;
                    case 0x02:
                    case 0x03:
                    case 0x04:
                    case 0x05:
                    case 0x06:
                    case 0x07:
                    case 0x08:
                    case 0x09:
                    case 0x0b:
                        struct = new MetaChunk();
                        break;
                    case 0x0c:
                        struct = new LicenceChunk();
                        break;
                    case 0x0a:
                        struct = new CIDChunk();
                        break;
                    case 0x81:
                        struct = new ContentEndChunk();
                        break;
                    case 0x82:
                        struct = new CoverChunk();
                        break;
                    case 0x83:
                        struct = new ChapterChunk();
                        break;
                    case 0x84:
                        struct = new ChapterTitleChunk();
                        break;
                    case 0xf1:
                        struct = new LicenceChunk();
                        break;
                }

            if (struct != null)
            {
                struct.Header = header;
                _structList.add(struct);
                struct.read(_reader);
            }

        }
    }
}
