package com.test.data;

/**
 * Created by fj on 2018/7/27.
 */


public interface IBambooServer {
    /**
     * 保存数据，会覆盖前面的数据
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    boolean write(String key, String data) throws Exception;

    /**
     * 保存数据，会覆盖前面的数据
     *
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    boolean write(String key, byte[] data) throws Exception;

    /**
     * 读取数据，读取失败或没有值返回空字符“”
     *
     * @param key
     * @return
     * @throws Exception
     */
    String read(String key) throws Exception;

    /**
     * 读取数据，读取失败或没有值返回空字符“”
     *
     * @param key
     * @return
     * @throws Exception
     */
    byte[] readBytes(String key) throws Exception;

    /**
     * 删除数据，标记删除
     *
     * @param key
     * @return
     * @throws Exception
     */
    boolean cut(String key) throws Exception;

    /**
     * 移除数据实体，该函数很消耗io和计算资源，若无必要请不要使用
     *
     * @param key
     * @return
     * @throws Exception
     */
    boolean remove(String key) throws Exception;

    /**
     * 清除垃圾，该函数会删除被标记为删除的数据实体，相当耗费资源，请使用线程运行
     *
     * @return
     * @throws Exception
     */
    boolean clearRef() throws Exception;

    /**
     * 返回因各种意外导致的关闭状态
     *
     * @return
     */
    boolean isClose();
}
