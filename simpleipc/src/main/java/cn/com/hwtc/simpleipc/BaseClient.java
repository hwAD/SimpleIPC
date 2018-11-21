package cn.com.hwtc.simpleipc;

import android.content.Context;
import android.os.Message;

/**
 * Created by yuanc on 2018/11/19.
 */

public abstract class BaseClient {

  private RemoteBinderBroadcast mBroadcastReceiver;

  BaseClient(OnBinderListener onBinderListener) {
    mBroadcastReceiver = new RemoteBinderBroadcast();
    mBroadcastReceiver.setOnBinderListener(onBinderListener);
  }

 final void registerBroadcast(Context context, String action) {
    mBroadcastReceiver.registerBroadcast(context, action);
  }

  public abstract void setConnectListener(OnConnectListener onConnectListener);

  public abstract boolean sendMessage(String msg);

  public abstract boolean sendMessageDelayed(String msg,long uptimeMillis);

  public abstract boolean sendMessageAtTime(String msg,long uptimeMillis);

  public abstract boolean sendMessageBlocked(String msg);

  public abstract boolean sendMessage(Message msg);

  public abstract boolean sendMessageDelayed(Message msg,long uptimeMillis);

  public abstract boolean sendMessageAtTime(Message msg,long uptimeMillis);

  public abstract boolean sendMessageBlocked(Message msg);

  public abstract boolean sendMessageWithCallBack(String msg, OnMessageLisenter onMessageLisenter);

  public abstract boolean sendMessageWithCallBackBlocked(String msg,
      OnMessageLisenter onMessageLisenter);

  public abstract boolean sendMessageWithCallBack(Message msg, OnMessageLisenter onMessageLisenter);

  public abstract boolean sendMessageWithCallBackBlocked(Message msg,
      OnMessageLisenter onMessageLisenter);

  public abstract void destroy();
}
