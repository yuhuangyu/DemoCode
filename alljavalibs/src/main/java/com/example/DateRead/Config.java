package com.example.DateRead;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by anye6488 on 2017/6/7.
 * 注释暂时没有,BUG应该会有
 * 请耐心细心用心体会 ╮(╯▽╰)╭
 */

/**
 *  Mod - yfj
 * 修改添加无组名时group()的无参调用，支持group("")
 * 修改错误日志：总字符数->当前行字符数(行数与字符数从0开始未变)
 */


/*
*
* test.txt 解析
*
nogroupname=nnn
nogroupage=20

#ren
[person]
name=aaa
sex=1
age=17

[student]
stuname=bbb
stusex=2
stuage=18#stu
* */
public abstract class Config
{
    public static void main(String[] args)
    {
        try
        {
            //创建对象实现类，遍历文件内容，创建_cfg的map集合，即_cfg((组名.键名),值)
            Config cfg = config("D:\\test\\test.txt");
            //value(键，默认值)方法调用map集合通过键查找值，没找到或错误返回默认值
            System.out.println(cfg.value("student.stuname", ""));
            // group(组名)方法会在调用value(键，默认值)时将键拼接为：组名.键，查找值
            System.out.println(cfg.group("person").value("age", ""));

            System.out.print(cfg.group().value("nogroupage", ""));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Config config(String path)
    {
        try
        {
            // ConfigImpl为Config的实现类
            // 返回保存了数据后的对象
            return new ConfigImpl(new FileInputStream("D:\\test\\test.txt"));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        // 创建Config对象需实现其抽象方法
        // 返回对象,对象调用value方法时返回默认值
        return new Config()
        {
            @Override
            public String value(String name, String def)
            {
                return def;
            }
        };
    }
    //  继承者需实现这个抽象方法，由键取值的方法，必须实现，name为键，def为默认值(未找到或报错是返回)
    public abstract String value(String name, String def);

    public Config group(String name)
    {
        return new ConfigWrapper(name, this);
    }
    // 修改添加 无参调用
    public Config group()
    {
        return new ConfigWrapper("", this);
    }

    public class ConfigWrapper extends Config
    {
        private Config _config;
        private String _base;

        private ConfigWrapper(String base, Config config)
        {
            _config = config;
            _base = base;
        }

        @Override
        public String value(String name, String def)
        {
            //当无组名时不用拼接字符串直接查找 键名
            if (!"".equals(_base)) {
                return _config.value(_base + "." + name, def);
            } else {
                return _config.value(name, def);
            }

        }
    }

    static class StringReader
    {
        private InputStreamReader _reader;
        private char[] _charBuffer = new char[1024];
        private int _index = -1;
        private int len = -1;

        private final int None = -1;
        private final int Group = 1;
        private final int Key = 2;
        private final int Value = 3;
        private final int Cmt = 4;

        private int state = None;
        private int _loc = -1;
        private int _lineCount = 0;
        private StringBuilder _builder = new StringBuilder();
        private boolean nextFlag = false;

        public StringReader(InputStreamReader reader)
        {
            _reader = reader;
        }

        public Character read() throws IOException
        {
            // 一次读取len长度的_charBuffer数组
            // _index加一，不断返回 _charBuffer数组中的单个字节
            // 当_charBuffer数组数据取完时，_index = -1，获取新的_charBuffer数组
            if (_index >= len)
                _index = -1;

            if (_index == -1)
            {
                // 将文件中数据取出放到_charBuffer数组中
                len = _reader.read(_charBuffer, 0, _charBuffer.length);
                // 如果文件中数据被取完时，返回null
                if (len <= 0)
                    return null;

                _index = 0;
            }
            // _charBuffer数组，从下标为0开始返回，下标不断加一
            return _charBuffer[_index++];
        }

        public void loop() throws IOException
        {
            while (true)
            {
                //不断调用 read()方法获取单个字符，_index不断加一，得到下一个字符
                Character c = read();
                if (c == null)
                    return;
                //_loc为当前总字符数，修改为当前行的字符数
                _loc++;
                if (nextFlag) {
                    _loc = 0;
                    nextFlag = false;
                }
                //_lineCount为当前行数，从0开始
                if (c == '\n'){
                    _lineCount++;
                    nextFlag = true;
                }
                //当前字符有：'['，']'，'#'，'\n'，'\r'，其他字符
                //根据当前状态 选择 不同方法处理当前字符，并转换当前状态
                //None-空状态，Group-组状态，Key-键状态，Cmt-注释状态，Value-值状态
                //通过回调保存数据，创建StringReader对象时重写回调的方法，取出保存数据
                if (state == None)
                    onNone(c);
                else if (state == Group)
                    onGroup(c);
                else if (state == Key)
                    onKey(c);
                else if (state == Cmt)
                    onCmt(c);
                else if (state == Value)
                    onValue(c);
            }
        }

        private void onNone(char c)
        {
            if (c == '[')
                state = Group;
            else if (c == '#')
                state = Cmt;
            else if (c == '\n' || c == '\r')
                state = None;
            else
            {
                state = Key;
                onKey(c);
            }
        }

        private void onGroup(char c)
        {
            if (c == '\n' || c == '\r' || c == '[')
            {
                onError("非法字符 " + c + "line " + _lineCount + " at " + _loc);
            }
            else if (c != ']')
                _builder.append(c);
            else
            {
                onGroupReady(_builder.toString());
                state = None;
                _builder = new StringBuilder();
            }
        }

        private void onKey(char c)
        {
            if (c == '\n' || c == '\r' || c == '#')
            {
                onError("非法字符 " + ((int) c) + " line " + _lineCount + " at " + _loc);
            }
            else if (c != '=')
                _builder.append(c);
            else
            {
                onKeyReady(_builder.toString());
                state = Value;
                _builder = new StringBuilder();
            }
        }

        private void onValue(char c)
        {
            if (c == '#')
            {
                onValueReady(_builder.toString());
                state = Cmt;
                _builder = new StringBuilder();
            }
            else if (c == '\n' || c == '\r')
            {
                onValueReady(_builder.toString());
                state = None;
                _builder = new StringBuilder();
            }
            else
                _builder.append(c);
        }

        private void onCmt(char c)
        {
            if (c == '\n')
                state = None;
        }

        void onError(String msg)
        {

        }

        void onValueReady(String s)
        {

        }

        void onKeyReady(String s)
        {

        }

        void onGroupReady(String name)
        {

        }
    }

    public static class ConfigImpl extends Config
    {
        private Map<String, String> _cfg = new HashMap<>();

        public ConfigImpl(InputStream input)
        {

            try
            {
                InputStreamReader reader = new InputStreamReader(input, "gbk");

                new StringReader(reader)
                {
                    private String _group = null;
                    private String _key;
                    //将数据取出后分类保存到_cfg的Map集合中
                    // 组名不为空：_cfg(_group + "." + _key, s) 或 组名为空：_cfg(_key, s)
                    @Override
                    void onGroupReady(String name)
                    {
                        if (_key != null)
                        {
                            onError("error group " + name);
                        }
                        else
                            _group = name;
                    }

                    @Override
                    void onKeyReady(String s)
                    {
                        if (_key == null)
                            _key = s;
                        else
                        {
                            onError("error key " + s + " duplicate key");
                        }
                    }

                    @Override
                    void onValueReady(String s)
                    {
                        if (_key != null)
                        {
                            if (_group != null)
                                _cfg.put(_group + "." + _key, s);
                            else
                                _cfg.put(_key, s);
                            _key = null;
                        }
                        else
                        {
                            onError("error value " + s + " not key");
                        }
                    }

                    @Override
                    void onError(String msg)
                    {
                        System.out.println(msg);
                    }
                }.loop();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public String value(String name, String def)
        {
            if (_cfg.containsKey(name))
                return _cfg.get(name);

            return def;
        }
    }
}
