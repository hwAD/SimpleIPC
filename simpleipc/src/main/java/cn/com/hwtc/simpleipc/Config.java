package cn.com.hwtc.simpleipc;

/**
 * Created by yuanc on 2018/11/19.
 */

final class Config {
  static final int TRANSACTION_sendMsg =
      (android.os.IBinder.FIRST_CALL_TRANSACTION);
  static final int TRANSACTION_sendMsgWithCallBack =
      (android.os.IBinder.FIRST_CALL_TRANSACTION)+2;
  static final int MSG_WHAT_INNER =
      (android.os.IBinder.FIRST_CALL_TRANSACTION)+4;
  static final String DESCRIPTOR = "cn.com.hwtc.simpleipc.TOKEN";
  static final String ID_IPC_BINDER = "id_ipc_binder";
  static final String INNER_MESSAGE_ID = "inner_message_id";
}
