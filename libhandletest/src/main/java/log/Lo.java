package log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import log.Format.FConfig;
import log.Format.Format;

/**
 * Created by fj on 2018/8/16.
 */

public class Lo {
    public final static int Error = 0x08;
    public final static int Warn = 0x04;
    public final static int Info = 0x02;
    public final static int Debug = 0x01;
    private static boolean isOpen = true;
    private static Map<String, Lo> loggers = new HashMap<>();
    private List<LogWritter> logWritterList = new ArrayList<>();
    private Map<Integer, String> levelNames = new HashMap<Integer, String>(){
        {
            put(Debug, "Debug");
            put(Info, "Info");
            put(Warn, "Warn");
            put(Error, "Error");
        }
    };
    private FileLogWritter fileLogWritter;
    private LogFilter logFilter = new LogFilter() {
        @Override
        public boolean Filter(int level, String msg) {
            return false;
        }
    };
    private LogFormatter logFormatter = new LogFormatter() {
        @Override
        public String Formatter(String level, String msg) {
            return level+"  msg: "+msg;
        }
    };

    private Lo(){
        initConfig();
    }

    private void initConfig() {
        Config config = Config.get();

        String filterLevel = config.value("Log.FilterLevel", "");
        if (!"".equals(filterLevel)) {
            try {
                int cLevel = Integer.parseInt(filterLevel);
                logFilter = new LogFilter() {
                    @Override
                    public boolean Filter(int level, String msg) {
                        if (level < cLevel) {
                            return true;
                        }
                        return false;
                    }
                };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String writers = config.value("Log.Writer", "");
        String filePath = config.value("Log.FilePath", "");
        if (writers.contains("File") && !"".equals(filePath)) {
            setFileWritter(filePath);
        }

        String formats = config.value("Log.Format", "");
        if (!"".equals(formats)) {
            try {
                Format format = Format.get(formats);
                logFormatter = new LogFormatter() {
                    @Override
                    public String Formatter(String level, String msg) {
                        Map<Object, Object> map = new HashMap<>();
                        map.put("level",level);
                        map.put("msg",msg);
                        try {
                            return format.getformat(map, new FConfig());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return level+" :  "+msg;
                    }
                };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getLevelName(int level) {
        return levelNames.get(level);
    }

    public static Lo get(){
        return get("log_def");
    }

    public static Lo get(String config){
        if (loggers.size() > 0 && loggers.get(config) != null) {
            return loggers.get(config);
        }else {
            Lo lo = new Lo();
            loggers.put(config, lo);
            return lo;
        }
    }

    public void out(int level, String msg){
        if (!isOpen) {
            return;
        }

        boolean filter = logFilter.Filter(level, msg);
        if (filter) {
            return;
        }

        String formatterMsg = logFormatter.Formatter(getLevelName(level), msg);

        if (fileLogWritter != null) {
            fileLogWritter.Writter(level,msg,formatterMsg);
        }

        if (logWritterList.size() > 0) {
            for (LogWritter writter : logWritterList) {
                writter.Writter(level,msg,formatterMsg);
            }
        }else {
            System.out.println(formatterMsg);
        }
    }

    public Lo filter(LogFilter logFilter){
        this.logFilter = logFilter;
        return this;
    }

    public Lo filter(int lev){
        logFilter = new LogFilter() {
            @Override
            public boolean Filter(int level, String msg) {
                if ((level & lev) > 0) {
                    return true;
                }
                return false;
            }
        };
        return this;
    }

    public Lo formatter(LogFormatter logFormatter){
        this.logFormatter = logFormatter;
        return this;
    }

    public Lo addWritter(LogWritter logWritter) {
        logWritterList.add(logWritter);
        return this;
    }

    public Lo setFileWritter(String path) {
        fileLogWritter = new FileLogWritter(path);
        return this;
    }

    public static void open() {
        isOpen = true;
    }

    public static void close() {
        isOpen = false;
    }

}
