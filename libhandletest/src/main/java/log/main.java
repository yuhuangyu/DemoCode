package log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Filter;

/**
 * Created by fj on 2018/8/16.
 */

public class main {
    public static void main(String[] args) {
        Lo.close();
        Lo log = Lo.get("sss")
                .filter(new LogFilter() {
                    @Override
                    public boolean Filter(int level, String msg) {
                        if (level < Lo.Warn) {
                            return true;
                        }
                        return false;
                    }
                })
                .filter(Lo.Debug|Lo.Info|Lo.Warn)
                .formatter(new LogFormatter() {
                    @Override
                    public String Formatter(String level, String msg) {
                        return level+" :  "+msg;
                    }
                })
                .setFileWritter("D:\\encry\\fileLog")
                .addWritter(new LogWritter() {
                    @Override
                    public void Writter(int level, String msg, String formatterMsg) {
                        System.out.println("sdk:    "+formatterMsg);
                    }
                });

        log.out(Lo.Error,"aaaaaa");
        log.out(Lo.Warn,"bbbb");
        log.out(Lo.Info,"cccc");
        log.out(Lo.Debug,"dddd");

        Lo.get().out(Lo.Error,"ss");
        Lo.get().out(Lo.Warn,"ss");
        Lo.get().out(Lo.Info,"ss");
        Lo.get().out(Lo.Debug,"ss");

        Lo.get("sss").out(Lo.Error,"lllll");


        ArrayList<String> objects = new ArrayList<>();
        objects.stream()
                .filter(o -> o.length()>1)
                .map( s -> s.length());
//        objects.stream().filter();
//        objects.stream().filter().

        String sss = null;
        Integer integer = Optional.ofNullable(sss).map(s -> s.length())
                .orElse(0);
//                .orElseGet(null);
//        Optional.


        Optional.ofNullable(sss).map(new Function<String,Integer>(){
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        }).filter(integer1 -> integer1 >3);
    }
}
