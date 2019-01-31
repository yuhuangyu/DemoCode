package log;

/**
 * Created by fj on 2018/8/16.
 */

public interface LogFilter {
    boolean Filter(int level, String msg);
}
