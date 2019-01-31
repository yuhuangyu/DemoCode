package annotation;

/**
 * Created by fj on 2018/8/7.
 */

public class SimpleDomain extends BaseInfo{

    @EncryptFiled
    @DecryptFiled
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
