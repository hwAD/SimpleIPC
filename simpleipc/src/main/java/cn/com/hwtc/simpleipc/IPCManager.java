package cn.com.hwtc.simpleipc;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;

/**
 * Created by yuanc on 2018/11/16.
 */

public class IPCManager {

  private static final String TAG = "IPCManager";

  private volatile static IPCManager sActivationManager;

  public static synchronized IPCManager get() {
    if (null == sActivationManager) {
      synchronized (IPCManager.class) {
        if (null == sActivationManager) {
          sActivationManager = new IPCManager();
        }
      }
    }
    return sActivationManager;
  }

  private IPCManager() {
  }

  public void destroy() {
    sActivationManager = null;
  }

  /**
   * called by client
   * @param context context
   * @param action registeraction
   * @return BaseClient
   */
  public BaseClient connect(Context context, String action) {
    BaseClient client = getBaseClient();
    client.registerBroadcast(context, action);
    return client;
  }

  /**
   * called by server
   * @param context context
   * @param action receive action
   * @return BaseServer
   */
  public BaseServer accept(Context context, String action) {
    BaseServer server = getBaseServer();
    server.sendBroadcast(context, action);
    return server;
  }

  private boolean checkPermission(Context context, String permission) {
    if (Binder.getCallingPid() == android.os.Process.myPid()) {
      return true;
    }
    if (context.checkCallingPermission(permission) == PackageManager.PERMISSION_GRANTED) {
      return true;
    }
    return false;
  }

  private BaseServer getBaseServer() {
    return new AidlServer();
  }

  private BaseClient getBaseClient() {
    return new AidlClient();
  }
}
