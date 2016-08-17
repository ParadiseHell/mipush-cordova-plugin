package com.ct.cordova.mipush;

import android.content.Context;
import android.util.Log;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * Created by ChengTao on 2016-08-16.
 */
@SuppressWarnings("ALL")
public class MiPushReceiver extends PushMessageReceiver {
    private String mRegId;
    private String mMessage;
    private String mTitle;
    private String mDescription;

    /**
     * onReceivePassThroughMessage用来接收服务器发送的透传消息
     */
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceivePassThroughMessage(context, miPushMessage);
    }

    /**
     * onNotificationMessageClicked用来接收服务器发来的通知栏消息（用户点击通知栏时触发）
     *
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        Log.e("TAG", "onNotificationMessageClicked");
        mMessage = miPushMessage.getContent();
        mTitle = miPushMessage.getTitle();
        mDescription = miPushMessage.getDescription();
        Log.e("TAG", "onNotificationMessageClicked");
        MiPushPlugin.onNotificationMessageClickedCallBack(mTitle,mDescription,mMessage);
    }

    /**
     * onNotificationMessageArrived用来接收服务器发来的通知栏消息（消息到达客户端时触发，并且可以接收应用在前台时不弹出通知的通知消息）
     *
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        Log.e("TAG", "onNotificationMessageArrived");
        mMessage = miPushMessage.getContent();
        mTitle = miPushMessage.getTitle();
        mDescription = miPushMessage.getDescription();
        Log.e("TAG", "onNotificationMessageArrived");
        MiPushPlugin.onNotificationMessageArrivedCallBack(mTitle,mDescription,mMessage);
    }

    /**
     * onReceiveRegisterResult用来接受客户端向服务器发送注册命令消息后返回的响应
     *
     * @param context
     * @param miPushCommandMessage
     */
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        Log.e("TAG", "onReceiveRegisterResult");
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                Log.e("TAG", "onReceiveRegisterResult");
                MiPushPlugin.onReceiveRegisterResultCallBack(mRegId);
            }
        }
    }

    /**
     * onCommandResult用来接收客户端向服务器发送命令消息后返回的响应
     *
     * @param context
     * @param miPushCommandMessage
     */
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
    }
}
