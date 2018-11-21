package cn.com.hwtc.simpleipc;

import android.os.Message;

/**
 * Created by yuanc on 2018/11/19.
 */

public abstract class OnMessageLisenter {

  public void onMessageReceive(Message msg) {}

  public void onMessageReceive(String msg) {}

}
