package cn.com.hwtc.simpleipc;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import cn.com.hwtc.simpleipc.utils.L;

/**
 * Created by yuanc on 2018/11/16.
 */

class AidlServer extends BaseServer {

  private static final String TAG = AidlServer.class.getSimpleName();
  private OnMessageLisenter mOnMessageLisenter;
  private static Messenger sClientMessenger;

  AidlServer() {
  }

  @Override public void handleCallBack(Message msg) throws IllegalAccessException {
    if (msg != null) {
      if (msg.what != 0) {
        L.w("No need to set up msg.what , it will be ignored.");
      }
    }
    if (sClientMessenger == null) {
      throw new IllegalAccessException(
          "should not send messages first from the server side. !");
    }
    Message message = Message.obtain(msg);
    message.what = Config.TRANSACTION_sendMsg;

    try {
      sClientMessenger.send(message);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }

  @Override public void setOnMessageLisenter(OnMessageLisenter onMessageLisenter) {
    mOnMessageLisenter = onMessageLisenter;
  }

  private class MyBinder extends Binder {
    @Override //server
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
        throws RemoteException {
      switch (code) {
        case INTERFACE_TRANSACTION:
          reply.writeString(Config.DESCRIPTOR);
          return true;
        case Config.TRANSACTION_sendMsg:
          data.enforceInterface(Config.DESCRIPTOR);
          Message message = null;
          if ((0 != data.readInt())) {
            message = Message.CREATOR.createFromParcel(data);
          }
          if (message != null) {
            sClientMessenger = message.replyTo;
            if (mOnMessageLisenter != null) {
              Bundle bundle = message.getData();
              if (bundle != null) {
                String msg = bundle.getString(Config.INNER_MESSAGE_ID);
                //Log.d(TAG, "onTransact TRANSACTION_sendMsg response String: " + (msg != null));
                if (!TextUtils.isEmpty(msg)) {
                  mOnMessageLisenter.onMessageReceive(msg);
                } else {
                  mOnMessageLisenter.onMessageReceive(message);
                }
              }
            }
          }
          //Log.d(TAG, "onTransact TRANSACTION_sendMsg sClientMessenger: " + sClientMessenger+",message:"+message);
          reply.writeNoException();
          return true;

        case Config.TRANSACTION_sendMsgWithCallBack:
          data.enforceInterface(Config.DESCRIPTOR);
          IBinder iBinder = data.readStrongBinder();
          Log.d(TAG, "onTransact TRANSACTION_sendMsgWithCallBack iBinder: " + iBinder);

          reply.writeNoException();
          return true;
      }
      return super.onTransact(code, data, reply, flags);
    }
  }

  @Override
  IBinder getBinder() {
    return new MyBinder();
  }

  @Override void destroy() {
    sClientMessenger = null;
  }
}
