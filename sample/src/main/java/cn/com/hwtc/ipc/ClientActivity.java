package cn.com.hwtc.ipc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import cn.com.hwtc.simpleipc.BaseClient;
import cn.com.hwtc.simpleipc.IPCManager;
import cn.com.hwtc.simpleipc.OnConnectListener;
import cn.com.hwtc.simpleipc.OnMessageLisenter;

/**
 * Created by yuanc on 2018/11/16.
 */

public class ClientActivity extends Activity implements View.OnClickListener, OnConnectListener {

  private static final String TAG = ClientActivity.class.getSimpleName();
  private BaseClient mClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_client);
    setListener();
  }

  private void setListener() {
    findViewById(R.id.bt_connect).setOnClickListener(this);
    findViewById(R.id.bt_sendMessage).setOnClickListener(this);
    findViewById(R.id.bt_sendMessageWithCallback).setOnClickListener(this);
    findViewById(R.id.bt_sendMessageBlocked).setOnClickListener(this);
  }

  @Override protected void onResume() {
    super.onResume();
    SystemBarUitl.notifyNavigationBar(this, SystemBarUitl.HW_STATUSBAR_KEY_BACK);
  }

  @Override protected void onPause() {
    super.onPause();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.bt_connect:
        mClient = IPCManager.get().connect(this, ServerActivity.ACTION_IPC);
        mClient.setConnectListener(this);
        startActivity(new Intent("cn.com.hwtc.Action_Accept"));
        break;
      case R.id.bt_sendMessage:
        if (mClient == null) {
          Toaster.make(ClientActivity.this)
              .colorHead("#aa0000", "please connect first !!!")
              .show(Toast.LENGTH_SHORT);
        } else {
          mClient.sendMessage("Hello Server!");
        }
        break;
      case R.id.bt_sendMessageWithCallback:
        if (mClient == null) {
          Toaster.make(ClientActivity.this)
              .colorHead("#aa0000", "please connect first !!!")
              .show(Toast.LENGTH_SHORT);
        } else {
          Message msg = Message.obtain();
          Bundle data = new Bundle();
          data.putString("callback", "Hello Server,please say hello back for me !");
          msg.setData(data);
          mClient.sendMessageWithCallBack(msg, new OnMessageLisenter() {

            @Override public void onMessageReceive(Message msg) {
              Log.d(TAG, "onMessageReceive callback from server:" + msg);

              Toaster.make(ClientActivity.this)
                  .colorHead("#EE0000", "Client:")
                  .content("onMessageReceive callback from server :" + msg)
                  .show(Toast.LENGTH_SHORT);
            }
          });
        }

        break;
      case R.id.bt_sendMessageBlocked:
        if (mClient == null) {
          Toaster.make(ClientActivity.this)
              .colorHead("#aa0000", "please connect first !!!")
              .show(Toast.LENGTH_SHORT);
        } else {
          mClient.sendMessageBlocked("Hello Server and will Blocked!");
        }
        break;
      default:
        break;
    }
  }

  @Override public boolean onConnectFailure() {
    runOnUiThread(new Runnable() {
      @Override public void run() {
        Toaster.make(ClientActivity.this)
            .colorHead("#EE0000", "Client:")
            .content("onDisconnected !!! 连接失败" )
            .show(Toast.LENGTH_SHORT);
      }
    });
    return false;
  }

  @Override public void onConnected() {
    runOnUiThread(new Runnable() {
      @Override public void run() {
        Toaster.make(ClientActivity.this)
            .colorHead("#EE0000", "Client:")
            .content("连接成功" )
            .show(Toast.LENGTH_SHORT);
      }
    });
  }

  @Override public void onDisconnected() {
    runOnUiThread(new Runnable() {
      @Override public void run() {
        Toaster.make(ClientActivity.this)
            .colorHead("#EE0000", "Client:")
            .content("onDisconnected !!! 连接断开" )
            .show(Toast.LENGTH_SHORT);
      }
    });
  }
}
