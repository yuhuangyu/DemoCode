package log;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by fj on 2018/8/21.
 */

public class Config {
    private static Config mConfig;
    private Properties _properties;

    private Config(){
        _properties = new Properties();
        File file = new File("libhandletest/src/data");

        if (file.exists()) {
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                _properties.load(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Config get(){
        if (mConfig == null) {
            mConfig = new Config();
        }
        return mConfig;
    }

    public String value(String name, String def) {
        return _properties.getProperty(name, def);
    }
}
