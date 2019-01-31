package Opt;

/**
 * Created by ASUS on 2017/9/13.
 */

public interface workdemo {

    @GET(value1 = "akd/{cityId}.html",value2 = "")
    public String getWether(@PATH(value = "cityId") String cityid);


}
