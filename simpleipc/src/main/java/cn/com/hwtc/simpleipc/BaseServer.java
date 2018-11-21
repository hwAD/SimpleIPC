package cn.com.hwtc.simpleipc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import cn.com.hwtc.simpleipc.permission.Permissions;

/**
 * Created by yuanc on 2018/11/19.
 */

public abstract class BaseServer {

  BaseServer() {
  }

  final void sendBroadcast(Context context, String action) {
    IBinder binder = getBinder();
    Bundle bundle = new Bundle();
    bundle.putBinder(Config.ID_IPC_BINDER, binder);
    Intent intent = new Intent();
    intent.putExtras(bundle);
    intent.setAction(action);
    //intent.setComponent(new ComponentName(context, "cn.com.hwtc.testorderedbroadcast.ipc2.RemoteBinderBroadcast"));
    intent.addCategory(Permissions.CATEGORY_IPC);
    //context.sendStickyBroadcast(intent, Permissions.PERMISSION_IPC);
    context.sendBroadcast(intent, Permissions.PERMISSION_IPC);
  }

  public abstract void setOnMessageLisenter(OnMessageLisenter onMessageLisenter);

  /**
   * Providing interfaces to respond to clients
   * @param msg Reply to client message
   * @throws IllegalAccessException Throws IllegalAccessException
   * No calls from the client have been invoked since the server is called.
   */
  public void handleCallBack(Message msg) throws IllegalAccessException {
  }

  abstract IBinder getBinder();

  void destroy() {
  }
}
