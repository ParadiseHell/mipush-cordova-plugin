package com.ct.cordova.mipush;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.xiaomi.mipush.sdk.MiPushClient;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MiPushPlugin extends CordovaPlugin {
    private static String TAG = "MiPushPlugin";
    private static String MI_PUSH = "mipush";
    private static Activity activity;
    private static MiPushPlugin instance;
    public static String openNotificationTitle;
    public static String openNotificationDescription;
    public static String openNotificationExtras;
    private final List<String> methodList =
            Arrays.asList(
                    "init",
                    "onNotificationMessageArrivedCallBack",
                    "onNotificationMessageClickedCallBack",
                    "onReceiveRegisterResultCallBack",
                    "showToast",
                    "setAlias",
                    "unSetAlias",
                    "setUserAccount",
                    "unSetUserAccount",
                    "setTopic",
                    "unSetTopic"
            );
    private ExecutorService threadPool = Executors.newFixedThreadPool(1);

    public MiPushPlugin() {
        instance = this;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        activity = cordova.getActivity();
        //如果是首次启动，并且点击的通知消息，则处理消息
        if (openNotificationTitle != null) {
            onNotificationMessageClickedCallBack(openNotificationTitle, openNotificationDescription,
                    openNotificationExtras);
        }
    }

    @Override
    public boolean execute(final String action, final JSONArray data,
                           final CallbackContext callbackContext) throws JSONException {
        Log.e(TAG, "-------------action------------------" + action);
        if (!methodList.contains(action)) {
            return false;
        }
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Method method = MiPushPlugin.class.getDeclaredMethod(action,
                            JSONArray.class, CallbackContext.class);
                    method.invoke(MiPushPlugin.this, data, callbackContext);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity = null;
        instance = null;
    }

    /**
     * 注册小米推送
     *
     * @param data
     * @param callbackContext
     */
    public void init(JSONArray data, CallbackContext callbackContext) {
        if (shouldInit(activity)) {
            try{
                ApplicationInfo appInfo = activity.getPackageManager()
                        .getApplicationInfo(activity.getPackageName(),
                                PackageManager.GET_META_DATA);
                String APP_KEY = appInfo.metaData.getString("MiPushAppKey");
                String APP_ID = appInfo.metaData.getString("MiPushAppId");
                APP_KEY = APP_KEY.split(MI_PUSH)[0];
                APP_ID = APP_ID.split(MI_PUSH)[0];
                Log.e(TAG,"-------APP_KEY-------"+APP_KEY+"------APP_ID----"+APP_ID);
                MiPushClient.registerPush(activity, APP_ID, APP_KEY);
                Log.e(TAG, "-------------init------------------");
                callbackContext.success();
            }catch (Exception e){
                e.printStackTrace();
                callbackContext.error("init:error---"+e.toString());
            }

        }
    }

    /**
     * @param activity
     * @return
     */
    private boolean shouldInit(Activity activity) {
        ActivityManager am = ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = activity.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 弹出土司
     *
     * @param data
     * @param callbackContext
     */
    public void showToast(final JSONArray data, final CallbackContext callbackContext) {
        Log.e(TAG,"------showToast-------");
        try{
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(activity, data.get(0).toString(), Toast.LENGTH_SHORT).show();
                        callbackContext.success();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            callbackContext.error("showToast:error---"+e.toString());
        }
    }


    /**
     * 设置别名
     *
     * @param data
     * @param callbackContext
     */
    public void setAlias(JSONArray data, CallbackContext callbackContext) {
        Log.e(TAG, "---------setAlias-----------");
        try {
            String alias = data.get(0).toString();
            Log.e(TAG,"-----------alias-------------"+alias);
            MiPushClient.setAlias(activity,alias,null);
            callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error("setAlias:error--"+e.toString());
        }
    }

    /**
     * 取消设置别名
     *
     * @param data
     * @param callbackContext
     */
    public void unSetAlias(JSONArray data, CallbackContext callbackContext) {
        Log.e(TAG, "---------unSetAlias-----------");
        try {
            String alias = data.get(0).toString();
            Log.e(TAG,"-----------alias-------------"+alias);
            MiPushClient.unsetAlias(activity,alias,null);
            callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error("unSetAlias:error---"+e.toString());
        }
    }

    /**
     * 设置userAccount
     *
     * @param data
     * @param callbackContext
     */
    public void setUserAccount(JSONArray data, CallbackContext callbackContext) {
        Log.e(TAG, "---------setUserAccount-----------");
        try {
            String userAccount = data.get(0).toString();
            Log.e(TAG,"-----------userAccount-------------"+userAccount);
            MiPushClient.setUserAccount(activity,userAccount,null);
            callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error("setUserAccount:error---"+e.toString());
        }
    }

    /**
     * 取消设置userAccount
     *
     * @param data
     * @param callbackContext
     */
    public void unSetUserAccount(JSONArray data, CallbackContext callbackContext) {
        Log.e(TAG, "---------unSetUserAccount-----------");
        try {
            String userAccount = data.get(0).toString();
            Log.e(TAG,"-----------userAccount-------------"+userAccount);
            MiPushClient.unsetUserAccount(activity,userAccount,null);
            callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error("unSetUserAccount:error---"+e.toString());
        }
    }

    /**
     * 订阅topic
     *
     * @param data
     * @param callbackContext
     */
    public void setTopic(JSONArray data, CallbackContext callbackContext) {
        Log.e(TAG, "---------setTopic-----------");
        try {
            String topic = data.get(0).toString();
            Log.e(TAG,"-----------topic-------------"+topic);
            MiPushClient.subscribe(activity, topic, null);
            callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error("setTopic:error---"+e.toString());
        }
    }

    /**
     * 取消订阅topic
     *
     * @param data
     * @param callbackContext
     */
    public void unSetTopic(JSONArray data, CallbackContext callbackContext) {
        Log.e(TAG, "---------unSetTopic-----------");
        try {
            String topic = data.get(0).toString();
            Log.e(TAG,"-----------topic-------------"+topic);
            MiPushClient.unsubscribe(activity, topic, null);
            callbackContext.success();
        } catch (Exception e) {
            e.printStackTrace();
            callbackContext.error("unSetTopic:error---"+e.toString());
        }
    }

    /**
     * 接受到消息
     * @param title
     * @param description
     * @param extras
     */
    public static void onNotificationMessageArrivedCallBack(String title,String description,String extras) {
        Log.e(TAG, "-------------onNotificationMessageArrivedCallBack------------------");
        if (instance == null) {
            return;
        }
        JSONObject object = getNotificationJsonObject(title,description,extras);
        Log.e(TAG, "-------------onNotificationMessageArrivedCallBack------------------"+object.toString());
        String format = "window.plugins.MiPushPlugin.onNotificationMessageArrivedCallBack(%s);";
        final String js = String.format(format, object.toString());
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
    }

    /**
     * 用户点击
     * @param title
     * @param description
     * @param extras
     */
    public static void onNotificationMessageClickedCallBack(String title,String description,String extras) {
        Log.e(TAG, "-------------onNotificationMessageClickedCallBack------------------");
        if (instance == null) {
            return;
        }
        JSONObject object = getNotificationJsonObject(title,description,extras);
        Log.e(TAG, "-------------onNotificationMessageClickedCallBack------------------"+object.toString());
        String format = "window.plugins.MiPushPlugin.onNotificationMessageClickedCallBack(%s);";
        final String js = String.format(format, object.toString());
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
        MiPushPlugin.openNotificationTitle=null;
        MiPushPlugin.openNotificationDescription=null;
        MiPushPlugin.openNotificationExtras=null;
    }

    /**
     * 小米推送注册成功
     *
     * @param regId
     */
    public static void onReceiveRegisterResultCallBack(String regId) {
        Log.e(TAG, "-------------onReceiveRegisterResultCallBack------------------" + regId);
        if (instance == null) {
            return;
        }
        try {
            JSONObject object = new JSONObject();
            object.put("regId",regId);
            String format = "window.plugins.MiPushPlugin.onReceiveRegisterResultCallBack(%s);";
            final String js = String.format(format, object.toString());
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    instance.webView.loadUrl("javascript:" + js);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取消息通知的Json对象
     * @param title
     * @param description
     * @param extras
     * @return
     */
    public static JSONObject getNotificationJsonObject(String title,String description,String extras){
        JSONObject data = new JSONObject();
        try {
            data.put("title",title);
            data.put("description",description);
            JSONObject extrasObject = new JSONObject(extras);
            Iterator it = extrasObject.keys();
            while (it.hasNext()){
                String key = (String) it.next();
                String value = extrasObject.getString(key);
                data.put(key,value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}