package cn.com.hwtc.simpleipc;

/**
 * Created by yuanc on 2018/11/19.
 */

public interface OnConnectListener {

  boolean onConnectFailure();

  void onConnected();

  void onDisconnected();
}
