package cn.com.hwtc.simpleipc;

import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import java.lang.ref.WeakReference;

/**
 * Created by yuanc on 2018/11/16.
 */

class AidlClient extends BaseClient {
  private static final String TAG = AidlClient.class.getSimpleName();
  private static OnConnectListener sOnConnectListener;
  private static IBinder mServerBinder;

  AidlClient() {
    super(sOnBinderListener);
  }

  @Override
  public void setConnectListener(OnConnectListener onConnectListener) {
    sOnConnectListener = onConnectListener;
  }

  private static OnBinderListener sOnBinderListener = new OnBinderListener() {
    @Override
    public void onGetBinder(IBinder binder) {
      if (binder != null) {
        mServerBinder = binder;
        if (sOnConnectListener != null) {
          sOnConnectListener.onConnected();
        }
      }
    }

    @Override
    public void onBinderDied() {
      mServerBinder = null;
      if (sOnConnectListener != null) {
        sOnConnectListener.onDisconnected();
      }
    }
  };

  /**
   * hanlde the message from server
   */
  private static final class ClientHandler extends Handler {

    private final WeakReference<AidlClient> mWeakReference;
    private OnMessageLisenter mOnMessageLisenter;

    private ClientHandler(AidlClient client) {
      mWeakReference = new WeakReference<AidlClient>(client);
    }

    @Override
    public void handleMessage(Message msg) {
      if (msg == null) return;
      if (mWeakReference.get() != null) {
        AidlClient client = mWeakReference.get();
        switch (msg.what) {
          case Config.TRANSACTION_sendMsg:
            if (mOnMessageLisenter != null) {
              mOnMessageLisenter.onMessageReceive(
                  msg); //callback only support onMessageReceive(Message msg)
            }
            break;
          case Config.MSG_WHAT_INNER:
            if (client != null) {
              client.sendMessage(msg);
            }
            break;
          default:
            break;
        }
      }
    }
  }

  /**
   * Sends a Message
   *
   * @param message message o send.
   * @param flags {@link IBinder#FLAG_ONEWAY,0}:
   * if it is FLAG_ONEWAY, then the return value indicates whether the client send request is successful. At this point, the thread calling transact will not be blocked.
   * if it is 0 (normal), then the return value is the return value of the ontranct of the service side. At this point, the thread calling transact is blocked.
   * @return Returns the result from {@link Binder#onTransact}.  A successful call
   * generally returns true; false generally means the transaction code was not
   * understood.
   * @throws RemoteException Throws DeadObjectException if the binder no longer exists.
   */
  private boolean send(Message message, int flags) throws RemoteException {
    android.os.Parcel data = android.os.Parcel.obtain();
    android.os.Parcel reply = android.os.Parcel.obtain();
    int code =
        Config.TRANSACTION_sendMsg;//{@link Config#TRANSACTION_sendMsg,Config#TRANSACTION_sendMsgWithCallBack}.
    try {
      //Log.d(TAG, "message :" + message);
      data.writeInterfaceToken(Config.DESCRIPTOR);
      if ((message != null)) {
        data.writeInt(1);
        code = message.what;
        message.writeToParcel(data, 0);
      } else {
        data.writeInt(0);
      }
      boolean transact = mServerBinder.transact(code, data, reply, flags);
      reply.readException();
      return transact;
    } finally {
      reply.recycle();
      data.recycle();
    }
  }

  /**
   * build a Message from specifical strings
   *
   * @param msg who Will be built into
   * @return a new Message
   */
  private Message buildMessage(String msg) {
    Message message = Message.obtain();
    Bundle bundle = new Bundle();
    bundle.putString(Config.INNER_MESSAGE_ID, msg);
    message.setData(bundle);
    return message;
  }

  /**
   * @param msg a String Content to send
   * @return whether the message sent successfully
   */
  @Override
  public boolean sendMessage(String msg) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    Message message = buildMessage(msg);
    message.what = Config.TRANSACTION_sendMsg;
    try {
      return send(message, IBinder.FLAG_ONEWAY);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean sendMessageAtTime(String msg, long uptimeMillis) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    Message message = buildMessage(msg);
    message.what = Config.MSG_WHAT_INNER;
    ClientHandler clientHandler = new ClientHandler(this);
    if (clientHandler.hasMessages(Config.MSG_WHAT_INNER)) {
      clientHandler.removeMessages(Config.MSG_WHAT_INNER);
    }
    return clientHandler.sendMessageAtTime(message, uptimeMillis);
  }

  @Override
  public boolean sendMessageDelayed(String msg, long uptimeMillis) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    Message message = buildMessage(msg);
    message.what = Config.MSG_WHAT_INNER;
    ClientHandler clientHandler = new ClientHandler(this);
    if (clientHandler.hasMessages(Config.MSG_WHAT_INNER)) {
      clientHandler.removeMessages(Config.MSG_WHAT_INNER);
    }
    return clientHandler.sendMessageDelayed(message, uptimeMillis);
  }

  /**
   * {@link #sendMessage(String)},The difference is that the thread of the caller will be blocked
   * until the message is processed.
   *
   * @param msg a String Content to send
   * @return whether the message sent successfully
   */
  @Override
  public boolean sendMessageBlocked(String msg) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    try {
      Message message = buildMessage(msg);
      message.what = Config.TRANSACTION_sendMsg;
      return send(message, 0);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean sendMessage(Message msg) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    Message message = Message.obtain(msg);
    message.what = Config.TRANSACTION_sendMsg;
    try {
      return send(message, IBinder.FLAG_ONEWAY);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean sendMessageDelayed(Message msg, long uptimeMillis) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    Message message = Message.obtain(msg);
    message.what = Config.MSG_WHAT_INNER;
    ClientHandler clientHandler = new ClientHandler(this);
    if (clientHandler.hasMessages(Config.MSG_WHAT_INNER)) {
      clientHandler.removeMessages(Config.MSG_WHAT_INNER);
    }
    return clientHandler.sendMessageDelayed(message, uptimeMillis);
  }

  @Override
  public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    Message message = Message.obtain(msg);
    message.what = Config.MSG_WHAT_INNER;
    ClientHandler clientHandler = new ClientHandler(this);
    if (clientHandler.hasMessages(Config.MSG_WHAT_INNER)) {
      clientHandler.removeMessages(Config.MSG_WHAT_INNER);
    }
    return clientHandler.sendMessageAtTime(message, uptimeMillis);
  }

  @Override
  public boolean sendMessageBlocked(Message msg) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    try {
      Message message = Message.obtain(msg);
      message.what = Config.TRANSACTION_sendMsg;
      return send(message, 0);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   *
   * @param msg
   * @return
   */
  @Override
  public boolean sendMessageWithCallBack(String msg, OnMessageLisenter onMessageLisenter) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    Message message = buildMessage(msg);
    message.what = Config.TRANSACTION_sendMsg;
    ClientHandler clientHandler = new ClientHandler(this);
    clientHandler.mOnMessageLisenter = onMessageLisenter;
    message.replyTo = new Messenger(clientHandler);     //指定回信人是客户端定义的,未来得及处理回调消息将被忽略
    try {
      return send(message, IBinder.FLAG_ONEWAY);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean sendMessageWithCallBackBlocked(String msg, OnMessageLisenter onMessageLisenter) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    Message message = buildMessage(msg);
    message.what = Config.TRANSACTION_sendMsg;
    ClientHandler clientHandler = new ClientHandler(this);
    clientHandler.mOnMessageLisenter = onMessageLisenter;
    message.replyTo = new Messenger(clientHandler);     //指定回信人是客户端定义的,未来得及处理回调消息将被忽略
    try {
      return send(message, 0);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean sendMessageWithCallBack(Message msg, OnMessageLisenter onMessageLisenter) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    Message message = Message.obtain(msg);
    message.what = Config.TRANSACTION_sendMsg;
    ClientHandler clientHandler = new ClientHandler(this);
    clientHandler.mOnMessageLisenter = onMessageLisenter;
    message.replyTo = new Messenger(clientHandler);     //指定回信人是客户端定义的,未来得及处理回调消息将被忽略
    try {
      return send(message, IBinder.FLAG_ONEWAY);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean sendMessageWithCallBackBlocked(Message msg, OnMessageLisenter onMessageLisenter) {
    if (checkBinderNotNull()) return sOnConnectListener.onConnectFailure();
    Message message = Message.obtain(msg);
    message.what = Config.TRANSACTION_sendMsg;
    ClientHandler clientHandler = new ClientHandler(this);
    clientHandler.mOnMessageLisenter = onMessageLisenter;
    message.replyTo = new Messenger(clientHandler);     //指定回信人是客户端定义的,未来得及处理回调消息将被忽略
    try {
      return send(message, 0);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean checkBinderNotNull() {
    if (mServerBinder == null) {
      if (sOnConnectListener != null) {
        Log.w(TAG, "Remote Object maybe has died or not ready!");
        return true;
      }
    }
    return false;
  }

  @Override
  public void destroy() {
    sOnConnectListener = null;
    sOnBinderListener = null;
    mServerBinder = null;
  }
}
