package log;

/**
 * Created by fj on 2018/8/16.
 */

public interface LogWritter {
    void Writter(int level, String msg, String formatterMsg);
}
