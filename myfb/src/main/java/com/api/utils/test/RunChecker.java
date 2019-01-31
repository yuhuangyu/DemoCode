package com.api.utils.test;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 判断条件：
 * 1.模拟器
 * 2.手机debug系统
 * 3.没有蓝牙
 * 4.没有光传感器
 * 5.开启开发者模式
 * 6.打开root
 * 7.连接usb
 * 8.打开usb调试
 * 9.没有锁屏密码
 * 10.开启VPN
 * 11.没有摄像头
 * 12.设备存在debug版本apk
 * 13.没有Facebook和Google play账号
 * 14.电量大于97
 * <p>
 * <p>
 * 释放条件：
 * 1.网络状态变化 2次
 * 2.屏幕解锁 2次
 * 3.开关蓝牙 1次
 * 4.调节音量 2次
 * 5.电量低于80
 * 6.电量变化绝对值大于20
 */
public class RunChecker {
    private volatile static RunChecker mRuncheck = null;
    private final long FourHours = 4 * 60 *60 * 1000L;
    private final long TwoDays = 48 * 60 * 60 *1000L;
    private GetParamsUtil mParamsUtil = null;
    ChangeBroadcastReceiver mReceiver = null;
    private RunCheckInterface util = null;
    private String conditionStr = "";
    private Map<Integer, ArrayList<ArrayList<Integer>>> conditionCfg;
    private Map<Integer, Integer> releaseCfg;
    private Callback mCallback = null;
    private boolean showLog = true;
    private boolean canRun = false;
    private boolean firstNetChanage = true;
    private String defaultCondition = "{\"condition_1\": [[1],[2],[3],[4],[11],[5,6,7,8,9,10]],\"condition_2\": [[5,6,7,8,9,10],[12],[13],[14]],\"release\": [[1,2],[2,2],[3,1],[4,2],[5,80],[6,20]]}";
    private String[] mAllActions = {
            "android.net.conn.CONNECTIVITY_CHANGE",
            Intent.ACTION_USER_PRESENT,
            BluetoothAdapter.ACTION_STATE_CHANGED,
            "android.media.VOLUME_CHANGED_ACTION",
            "android.intent.action.BATTERY_CHANGED"};

    private Map<Integer, Long> mCacheMap = new HashMap();


    private Context mContext;
    private boolean isWatching = false;

    private RunChecker(Context context, RunCheckInterface util, Map<Integer, ArrayList<ArrayList<Integer>>> conditionCfg, Map<Integer, Integer> releaseCfg) {
        this.mContext = context;
        this.util = util;
//        if (conditionCfg == null) {
//            setDefCondition();
//        } else {
//            this.conditionCfg = conditionCfg;
//        }
//        if (releaseCfg == null) {
//            setDefReleaseCfg();
//        } else {
//            this.releaseCfg = releaseCfg;
//        }
        if (TextUtils.isEmpty(conditionStr)) {
            conditionStr = util.getConfig("checkCondition", defaultCondition);
            printLog(" 配置信息为 " + conditionStr);
            getConditionFromJson();
        }
        showLog = "true".equals(util.getConfig("checkLog", "true"));
        mParamsUtil = new GetParamsUtil(context);
    }

    public static RunChecker getInstance(Context context, RunCheckInterface util, Map<Integer, ArrayList<ArrayList<Integer>>> conditionCfg, Map<Integer, Integer> releaseCfg) {
        if (mRuncheck == null) {
            synchronized (RunChecker.class) {
                if (mRuncheck == null) {
                    mRuncheck = new RunChecker(context, util, conditionCfg, releaseCfg);
                }
            }
        }
        return mRuncheck;
    }

    /**
     * 根据配置 确认当前设备是否可以开始
     *
     * @return true 可以跑
     */
    public boolean freedomCheck(Callback callback, String string) {
//        if (callback != null) {
//            mCallback = callback;
//        }
        printLog(string + " 调用检测");
        ArrayList<ArrayList<Integer>> conditionGroup_1 = conditionCfg.get(1);
        Iterator<ArrayList<Integer>> itGroup_1 = conditionGroup_1.iterator();
        printLog("run check 屏蔽条件检测开始 ------------------");
        while (itGroup_1.hasNext()) {
            Iterator<Integer> it = itGroup_1.next().iterator();
            boolean bad = false;
            boolean flag = true;
            while (it.hasNext()) {
                int idx = it.next();
                ConditionImpl con = new ConditionImpl(idx);
                boolean result = con.result();
                if (flag) {
                    flag = false;
                    bad = result;
                } else {
                    bad = bad && result;
                }
                printLog("n" + idx + " --- " + result);
                util.saveValue("n" + idx, "" + result);
            }
            if (bad) {
                printLog("run check false 被屏蔽了");
                printLog("run check 屏蔽条件检测结束------------------");
                return false;
            }
        }
        printLog("run check 屏蔽条件检测正常");
        printLog("run check 屏蔽条件检测结束------------------");

        long time = Long.parseLong(util.getValue("t1", "0"));
        printLog("run check time "+time);
        if (time == 0L) {
            //第一次检测
            printLog("run check first check 第一次检测");
            return freedomCheck_2();
        } else {
            //之前记录过数据，直接使用之前的数据判断
            long curTime = System.currentTimeMillis();
            long interval = curTime - time;
            long timeInter = Long.parseLong(util.getConfig("TimeInter", String.valueOf(TwoDays)));

            if (interval <= timeInter) {
                //有效期内，判断是否正常用户
                printLog("run check 有检测记录，直接判断");
//                mCallback = null;
                return AnalysisParams(-1);
            } else {
                //超出24小时，数据失效,重新开始检测
                printLog("run check 数据超出有效期，重新检测");
                util.saveValue("c1", "0");
                canRun = false;
                return freedomCheck_2();
            }
        }

    }

    public void setCallBack(Callback callBack) {
        mCallback = callBack;
    }

    public boolean freedomCheckForKe() {
        ArrayList<ArrayList<Integer>> conditionGroup_1 = conditionCfg.get(1);
        Iterator<ArrayList<Integer>> itGroup_1 = conditionGroup_1.iterator();
        while (itGroup_1.hasNext()) {
            Iterator<Integer> it = itGroup_1.next().iterator();
            boolean run = false;
            boolean flag = true;
            while (it.hasNext()) {
                int idx = it.next();
                ConditionImpl con = new ConditionImpl(idx);
                boolean result = con.result();
                if (flag) {
                    flag = false;
                    run = con.result();
                } else {
                    run = run && con.result();
                }
                printLog("n" + idx + " -- " + result);
                util.saveValue("n" + idx, "" + result);
            }
            if (run) {
                printLog("run check 壳判断 被屏蔽了");
                return false;
            }
        }
        printLog("run check 壳判断，正常用户");
        return true;
    }

    /**
     * 可配置检测可疑条件
     *
     * @return true 可以跑
     */
    private boolean freedomCheck_2() {
        ArrayList<ArrayList<Integer>> conditionGroup_2 = conditionCfg.get(2);
        Iterator<ArrayList<Integer>> itGroup_2 = conditionGroup_2.iterator();
        printLog("run check 可疑条件检测开始 ------------------");
        boolean checkBad = false;
        while (itGroup_2.hasNext()) {
            Iterator<Integer> it = itGroup_2.next().iterator();
            boolean bad = false;
            boolean flag = true;
            while (it.hasNext()) {
                int idx = it.next();
                ConditionImpl con = new ConditionImpl(idx);
                boolean result = con.result();
                if (flag) {
                    flag = false;
                    bad = result;
                } else {
                    bad = bad || result;
                }
                printLog("n" + idx + " -- " + result);
                util.saveValue("n" + idx, "" + result);
            }
//            if (bad) {
//                startWatch();
//                printLog("被怀疑 开始检测");
//                printLog("run check 可疑条件检测结束 ------------------");
//                return false;
//            }
            checkBad = checkBad || bad;
        }
        if (checkBad) {
            startWatch();
            printLog("被怀疑 开始检测");
            printLog("run check 可疑条件检测结束 ------------------");
            return false;
        }
        printLog("run check 可疑条件通过，正常");
        printLog("run check 可疑条件检测结束 ------------------");
        mCallback = null;
        return true;
    }

    /**
     * 确认当前设备是否可以开始
     *
     * @return true 可以跑，false 不能跑
     */
    public boolean check() {
        boolean check_1 = check_1();
        printLog("check_1 = " + check_1);
        if (check_1) {
            return false;
        }
        return check_2();
    }

    /**
     * 壳部分请求，确认当前设备是否可以开始
     *
     * @return true 可以跑
     */
    public boolean checkForKe() {
        boolean check_1 = check_1();
        printLog("check_1 = " + check_1);
        if (check_1) {
            return false;
        }
        return true;
    }

    /**
     * 任意一项命中就不跑
     *
     * @return true 有至少一项命中
     */
    private boolean check_1() {
        boolean isSimulator = mParamsUtil.isSimulator();
        boolean isDebugSys = mParamsUtil.isDebugSystem();  //debug系统
        boolean notbluetoothExist = !mParamsUtil.isBluetoothExist();
        boolean notLightSensor = !mParamsUtil.isLightSensorExist();
        boolean notCamera = !mParamsUtil.isCameraExist();
        boolean multiCondition = mParamsUtil.isDevelopmentEnabled() && mParamsUtil.isRootEnabled()
                && mParamsUtil.isLinkUSB() && mParamsUtil.isUsbDebugEnabled() && (!mParamsUtil.hasLockPwd())
                && mParamsUtil.isOpenVPN();

        return isSimulator || isDebugSys || notbluetoothExist
                || notLightSensor || notCamera || multiCondition;
    }

    /**
     * 怀疑需要每4小时连续统计数据，判断为正常用户后停止统计
     *
     * @return true正常用户可以跑， false可疑用户不能跑
     */
    private boolean check_2() {
        long time = Long.parseLong(util.getValue("t1", "0"));
        if (time == 0L) {
            //第一次检测
            printLog("first check");
            return startCheck();
        } else {
            //之前记录过数据，直接使用之前的数据判断
            long curTime = System.currentTimeMillis();
            long interval = curTime - time;
            long timeInter = Long.parseLong(util.getConfig("TimeInter", String.valueOf(TwoDays)));

            if (interval <= timeInter) {
                //有效期内，判断是否正常用户
                return AnalysisParams(-1);
            } else {
                //超出24小时，数据失效,重新开始检测
                return startCheck();
            }
        }
    }

    private void releaseReceiver() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        isWatching = false;
    }

    /**
     * 第一次检测，判断是否可疑，要不要持续检测
     *
     * @return true 正常用户
     */
    private boolean startCheck() {
        boolean hasDebugApk = mParamsUtil.getDebugApkCount() > 0;
        boolean hasForeignAccount = mParamsUtil.getAccount();
        boolean batteryHigh = mParamsUtil.getCurBattery() >= 97;
        boolean multiCondition = mParamsUtil.isDevelopmentEnabled() || mParamsUtil.isRootEnabled()
                || mParamsUtil.isLinkUSB() || mParamsUtil.isUsbDebugEnabled() || (!mParamsUtil.hasLockPwd())
                || mParamsUtil.isOpenVPN();  //至少一项命中

        boolean flag = hasDebugApk || (!hasForeignAccount) || batteryHigh || multiCondition;  //true 可疑
        if (flag) {
            startWatch();
            return false;  //可疑不能跑
        } else {
            return true;  //正常用户，可以跑
        }
    }

    /**
     * 分析数据是否变成正常用户
     *
     * @return true 正常用户
     */
    private boolean AnalysisParams(int idx) {
//        int netChange = Integer.parseInt(util.getValue("p1", "0"));
//        int screenChange = Integer.parseInt(util.getValue("p2", "0"));
//        int bluetoothChange = Integer.parseInt(util.getValue("p3", "0"));
//        int volueChange = Integer.parseInt(util.getValue("p4", "0"));
//        int batteryLevel = Integer.parseInt(util.getValue("p5", "0"));
//        int batteryChange = Integer.parseInt(util.getValue("p6", "0"));
//        if (netChange >= 1 || screenChange >= 2 || bluetoothChange >= 1 || volueChange >= 2 || batteryLevel <= 80 || batteryChange >= 20) {
//            releaseReceiver();
//            printLog("AnalysisParams true");
//            return true;
//        }
//        printLog("AnalysisParams false");
        printLog("start AnalysisParams idx = " + idx);
        if (idx != -1) {
            boolean rst;
            if (idx == 5) {
                rst = ((Integer.parseInt(util.getValue("p" + idx, "100"))) <= releaseCfg.get(idx))
                        || (Integer.parseInt(util.getValue("p" + 6, "0")) >= releaseCfg.get(6));
            } else {
                rst = (Integer.parseInt(util.getValue("p" + idx, "0"))) >= releaseCfg.get(idx);
            }
            if (rst) {
                releaseReceiver();
                printLog("释放条件 " + idx + " 变化，变为正常用户");
                canRun = true;
                if (mCallback != null) {
                    mCallback.afterCheck();
                    mCallback = null;
                }
                return true;
            }
            printLog("AnalysisParams 仍然为可疑用户");
            return false;
        } else {
            Set<Integer> set = releaseCfg.keySet();
            Iterator<Integer> it = set.iterator();
            boolean run = false;
            boolean flag = true;
            while (it.hasNext()) {
                int key = it.next();
                boolean rst;
                if (key == 5) {
                    rst = Integer.parseInt(util.getValue("p" + key, "100")) <= releaseCfg.get(key);
                } else {
                    rst = (Integer.parseInt(util.getValue("p" + key, "0"))) >= releaseCfg.get(key);
                }
                if (flag) {
                    flag = false;
                    run = rst;
                } else {
                    run = run || rst;
                }
                printLog("释放条件 " + key + " -- " + rst);
            }
            if (run) {
                releaseReceiver();
                printLog("AnalysisParams true 可疑转变为正常用户");
                canRun = true;
                if (mCallback != null) {
                    mCallback.afterCheck();
                    mCallback = null;
                }
                return true;
            }
            printLog("AnalysisParams 仍然为可疑用户");
            startWatch();
            return false;
        }

    }

    private void setDefReleaseCfg() {
        releaseCfg = new HashMap();
        releaseCfg.put(1, 2);  //网络
        releaseCfg.put(2, 2);  // 解锁
        releaseCfg.put(3, 1);  //蓝牙
        releaseCfg.put(4, 2);  // 音量
        releaseCfg.put(5, 80); //电量低于80
        releaseCfg.put(6, 20); //电量变化大于20
    }

    private void getConditionFromJson() {
        if (!TextUtils.isEmpty(conditionStr)) {
            try {
                Map<Integer, ArrayList<ArrayList<Integer>>> conditionMap = new HashMap();
                JSONObject con = new JSONObject(conditionStr);
                for (int x = 1; x <= 2; x++) {
                    JSONArray condition = con.getJSONArray("condition_" + x);
                    ArrayList<ArrayList<Integer>> groupList = new ArrayList();
                    for (int i = 0; i < condition.length(); i++) {
                        JSONArray groupArray = condition.getJSONArray(i);
                        ArrayList<Integer> group = new ArrayList();
                        for (int j = 0; j < groupArray.length(); j++) {
                            group.add(groupArray.optInt(j, 0));
                        }
                        groupList.add(group);
                    }
                    conditionMap.put(x, groupList);
                }
                conditionCfg = conditionMap;
                JSONArray release = con.getJSONArray("release");
                Map<Integer, Integer> releaseMap = new HashMap<Integer, Integer>();
                for (int i = 0; i < release.length(); i++) {
                    JSONArray group = release.getJSONArray(i);
                    releaseMap.put(group.optInt(0, 0), group.optInt(1, 0));
                }
                releaseCfg = releaseMap;
                printLog("解析配置成功");
            } catch (JSONException e) {
//                Lg.e("getConditionFromJson ", e);
                printLog("配置信息异常，使用默认配置");
                setDefCondition();
                setDefReleaseCfg();
            }
        } else {
            printLog("没有代码本地配置");
        }

    }

    private void setDefCondition() {
        conditionCfg = new HashMap();
        int[][] first = {{1}, {2}, {3}, {4}, {11}, {5, 6, 7, 8, 9, 10}};
        ArrayList<ArrayList<Integer>> group_1 = new ArrayList();
        for (int i = 0; i < first.length; i++) {
            ArrayList<Integer> list = new ArrayList();
            for (int j = 0; j < first[i].length; j++) {
                list.add(first[i][j]);
            }
            group_1.add(list);
        }
        conditionCfg.put(1, group_1);
        int[][] second = {{5, 6, 7, 8, 9, 10}, {12}, {13}, {14}};
        ArrayList<ArrayList<Integer>> group_2 = new ArrayList();
        for (int i = 0; i < second.length; i++) {
            ArrayList<Integer> list = new ArrayList();
            for (int j = 0; j < second[i].length; j++) {
                list.add(second[i][j]);
            }
            group_2.add(list);
        }
        conditionCfg.put(2, group_2);


    }

    /**
     * 设置检测开始时初始电量
     */
    private void setOriginBattery() {
        Intent batteryIntent = mContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (batteryIntent != null) {
            int var2 = batteryIntent.getIntExtra("level", -1);
            int var3 = batteryIntent.getIntExtra("scale", -1);
            int var6 = 0;
            if (var3 > 0) {
                var6 = (var2 * 100) / var3;
            }
            util.saveValue("p99", "" + var6);
        }
    }

    private void startWatch() {
        if (canRun) {
            return;
        }
        if (isWatching) {
            return;
        }
        long lastTime = Long.parseLong(util.getValue("t1", "0"));
        int checkTimes = Integer.parseInt(util.getValue("c1", "0"));
        if ((lastTime != 0) && (System.currentTimeMillis() - lastTime >= TwoDays)) {
            util.saveValue("c1", "0");
            printLog("上次检测时间超过48小时，清空数据继续检测 ");
            printLog("检测次数为：" + (checkTimes + 1));
            util.saveValue("p1", "0");
            util.saveValue("p2", "0");
            util.saveValue("p3", "0");
            util.saveValue("p4", "0");
            util.saveValue("p5", "100");
            util.saveValue("p6", "0");
        } else if ((lastTime != 0) && (System.currentTimeMillis() - lastTime >= FourHours) && (checkTimes < 3) && (checkTimes >= 0)) {
            util.saveValue("c1", "" + (checkTimes + 1));
            printLog("上次检测时间超过4小时，清空数据继续检测 ");
            printLog("检测次数为：" + (checkTimes + 1));
            util.saveValue("p1", "0");
            util.saveValue("p2", "0");
            util.saveValue("p3", "0");
            util.saveValue("p4", "0");
            util.saveValue("p5", "100");
            util.saveValue("p6", "0");
        }
        util.saveValue("t1", "" + System.currentTimeMillis()); // 设置开始记录时间}
        setOriginBattery();
        IntentFilter filter = new IntentFilter();
        for (int i = 0; i < mAllActions.length; i++) {
            if (releaseCfg.containsKey(i + 1)) {
                filter.addAction(mAllActions[i]);
            }
        }
//        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        filter.addAction(Intent.ACTION_SCREEN_OFF);//监听屏幕暗
//        filter.addAction(Intent.ACTION_SCREEN_ON);//监听屏幕亮
//        filter.addAction(Intent.ACTION_USER_PRESENT);//监听解屏
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//监听蓝牙打开关闭
//        filter.addAction("android.media.VOLUME_CHANGED_ACTION");//监听音量变化
//        filter.addAction("android.intent.action.BATTERY_CHANGED");//监听电量变化
        if (Integer.parseInt(util.getValue("c1", "0")) < 3) {
            mReceiver = new ChangeBroadcastReceiver();
            mContext.registerReceiver(mReceiver, filter);
            isWatching = true;
        }
    }

    private void printLog(String msg) {
        if (showLog) {
            util.log(msg);
        }
    }

    private boolean isActionAvailable(int key) {
        if (mCacheMap.containsKey(key)) {
            long lastTime = mCacheMap.get(key);
            long curTime = System.currentTimeMillis();
            if (curTime - lastTime > 1500) {
                mCacheMap.put(key, curTime);
                return true;
            } else {
                return false;
            }
        } else {
            mCacheMap.put(key, System.currentTimeMillis());
            return true;
        }
    }

    public String[] getCheckData() {
        String[] data = new String[4];
        int col = 0;
        try {
            JSONObject object = new JSONObject();
            for (int i = 1; i < 14; i++) {
                if (i / 4 != col) {
                    data[col] = object.toString();
                    object = new JSONObject();
                    col = i / 4;
                    object.put("n" + i, util.getValue("n" + i, "0"));
                } else {
                    object.put("n" + i, util.getValue("n" + i, "0"));
                }
                if (i == 13) {
                    data[3] = object.toString();
                }
            }
        } catch (JSONException e) {
//            Lg.e("getCheckData", e);
        }
        return data;
    }


    private class ChangeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long curTime = System.currentTimeMillis();
            long lastTime = Long.parseLong(util.getValue("t1", "0"));
            if (lastTime == 0) {
                printLog(" error last time is 0L");
            }
            int releaseIdx = 0;
            if (curTime - lastTime >= TwoDays) {
                //时间变为48小时之后，人为修改时间
                printLog("时间被修改，异常");
                util.saveValue("p1", "0");
                util.saveValue("p2", "0");
                util.saveValue("p3", "0");
                util.saveValue("p4", "0");
                util.saveValue("p5", "100");
                util.saveValue("p6", "0");
                util.saveValue("t1", "" + curTime);
                util.saveValue("c1", "0");
                setOriginBattery();
                releaseReceiver();
                return;
            }
            if (curTime - lastTime >= FourHours) {
                if (AnalysisParams(-1)) {
                    releaseReceiver();
                    return;
                }
                //检测时间超过4小时，清空记录
                util.saveValue("p1", "0");
                util.saveValue("p2", "0");
                util.saveValue("p3", "0");
                util.saveValue("p4", "0");
                util.saveValue("p5", "100");
                util.saveValue("p6", "0");
                util.saveValue("t1", "" + curTime);
                setOriginBattery();
                util.saveValue("c1", "" + (Integer.parseInt(util.getValue("c1", "0")) + 1));
                int a = Integer.parseInt(util.getValue("c1", "0"));
                printLog("run check times = " + a + " --- " + "" + (a >= 3));

                if (Integer.parseInt(util.getValue("c1", "0")) >= 3) {
                    util.saveValue("c1", "0");
                    printLog("check times >= 3");
                    releaseReceiver();
//                    util.dataEvent();
                }
            }
            printLog("receiver get action = " + intent.getAction());
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                if (firstNetChanage) {
                    firstNetChanage = false;
                } else {
                    if (isActionAvailable(1)) {
                        util.saveValue("p1", "" + (Integer.parseInt(util.getValue("p1", "0")) + 1));
                        printLog("net CONNECTIVITY_CHANGE -- " + util.getValue("p1", "0"));
                        releaseIdx = 1;
                    }
                }
            } else if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                if (isActionAvailable(2)) {
                    util.saveValue("p2", "" + (Integer.parseInt(util.getValue("p2", "0")) + 1));
                    printLog("ACTION_USER_PRESENT -- " + util.getValue("p2", "0"));
                    releaseIdx = 2;
                }
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                if (isActionAvailable(3)) {
                    util.saveValue("p3", "" + (Integer.parseInt(util.getValue("p3", "0")) + 1));
                    printLog("ACTION_STATE_CHANGED -- " + util.getValue("p3", "0"));
                    releaseIdx = 3;
                }
            } else if ("android.media.VOLUME_CHANGED_ACTION".equals(intent.getAction())) {
                if (isActionAvailable(4)) {
                    util.saveValue("p4", "" + (Integer.parseInt(util.getValue("p4", "0")) + 1));
                    printLog("VOLUME_CHANGED_ACTION -- " + util.getValue("p4", "0"));
                    releaseIdx = 4;
                }
            } else if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                if (isActionAvailable(5)) {
                    int var2 = intent.getIntExtra("level", -1);
                    int var3 = intent.getIntExtra("scale", -1);
                    int var6 = 0;
                    if (var3 > 0) {
                        var6 = (var2 * 100) / var3;
                    }
                    util.saveValue("p5", "" + var6);
                    releaseIdx = 5;

                    int change = Math.abs(var6 - Integer.parseInt(util.getValue("p99", "0")));
                    util.saveValue("p6", "" + change);
                    printLog("BATTERY_CHANGED -- " + util.getValue("p5", "0") + " -- " + util.getValue("p6", "0"));
                }
            }
            if (releaseIdx != 0) {
                AnalysisParams(releaseIdx);
            }
        }
    }

    public interface RunCheckInterface {
        void saveValue(String key, String value);

        String getValue(String key, String def);

        void log(String msg);

        void dataEvent(String arg1, String arg2, String arg3, String arg4, String arg5);

        String getConfig(String key, String def);
    }

    public static class RunCheckImpl implements RunCheckInterface {
        private Context context;

        public RunCheckImpl(Context context) {
            this.context = context;
        }

        @Override
        public void saveValue(String key, String value) {
            try {
                SharedPreferences sharedPreferences = context.getSharedPreferences("aaaaa", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                editor.putString(key, value);
                editor.commit();//提交修改
            } catch (Exception e) {
//                Lg.e(e);
            }
        }

        @Override
        public String getValue(String key, String def) {
            String value = null;
            try {
                SharedPreferences sharedPreferences = context.getSharedPreferences("aaaaa", Context.MODE_PRIVATE);
                value = sharedPreferences.getString(key, def);
            } catch (Exception e) {
//                Lg.e(e);
            }
            if (value != null) {
                return (String) value;
            }
//            Lg.e("run check --  getValue " + def);
            return def;
        }

        @Override
        public void log(String msg) {
            Log.e("sdk","run check --  " + msg);
        }

        @Override
        public void dataEvent(String arg1, String arg2, String arg3, String arg4, String arg5) {
//            CPServer.get(context).sendPointEvent(arg1,arg2,arg3,arg4,arg5);
        }

        @Override
        public String getConfig(String key, String def) {
//            return Config.get(context).get(key, def);
            return def;
        }
    }

    private interface conditionInterface {
        boolean result();
    }

    private class ConditionImpl implements conditionInterface {
        private int conditionNum = 0;

        public ConditionImpl(int conditionNum) {
            this.conditionNum = conditionNum;
        }

        @Override
        public boolean result() {
            if (conditionNum != 0) {
                switch (conditionNum) {
                    case 1:
                        return mParamsUtil.isSimulator();
                    case 2:
                        return mParamsUtil.isDebugSystem();
                    case 3:
                        return mParamsUtil.notHasBlueTooth();
                    case 4:
                        return !mParamsUtil.isLightSensorExist();
                    case 5:
                        return mParamsUtil.isDevelopmentEnabled();
                    case 6:
                        return mParamsUtil.isRootEnabled();
                    case 7:
                        return mParamsUtil.isLinkUSB();
                    case 8:
                        return mParamsUtil.isUsbDebugEnabled();
                    case 9:
                        return !mParamsUtil.hasLockPwd();
                    case 10:
                        return mParamsUtil.isOpenVPN();
                    case 11:
                        return !mParamsUtil.isCameraExist();
                    case 12:
                        return mParamsUtil.getDebugApkCount() > 0;
                    case 13:
                        return !mParamsUtil.getAccount();
                    case 14:
                        return mParamsUtil.getCurBattery() > 97;
                    default:
                        return false;
                }
            }
            return false;
        }
    }

    public interface Callback {
        void afterCheck();
    }


}
