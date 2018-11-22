package cn.com.hwtc.ipc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import cn.com.hwtc.simpleipc.BaseServer;
import cn.com.hwtc.simpleipc.IPCManager;
import cn.com.hwtc.simpleipc.OnMessageLisenter;

public class ServerActivity extends AppCompatActivity implements View.OnClickListener {
  public static final String TAG = ServerActivity.class.getSimpleName();
  public static final String ACTION_IPC = "cn.com.hwtc.Action_IPC";

  private BaseServer mServer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_server);
    findViewById(R.id.accept).setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == R.id.accept) {
      mServer = IPCManager.get().accept(this, ServerActivity.ACTION_IPC);
      mServer.setOnMessageLisenter(new OnMessageLisenter() {

        @Override public void onMessageReceive(final String msg) {
          Log.d(TAG, "onMessageReceive from client:" + msg + ",ThreadName:" + Thread.currentThread()
              .getName());

          //try {
          //  Thread.sleep(2000L);
          //} catch (InterruptedException e) {
          //  e.printStackTrace();
          //}

          runOnUiThread(new Runnable() {
            @Override public void run() {
              Toaster.make(ServerActivity.this).colorHead("#EE0000","Server:").content("onMessageReceive from client :" + msg).show(Toast.LENGTH_SHORT);
            }
          });
        }

        @Override public void onMessageReceive(final Message msg) {
          runOnUiThread(new Runnable() {
            @Override public void run() {

              Toaster.make(ServerActivity.this).colorHead("#EE0000","Server:").content("onMessageReceive from client :" + msg).show(Toast.LENGTH_SHORT);

              //try {
              //  Thread.sleep(2000L);
              //} catch (InterruptedException e) {
              //  e.printStackTrace();
              //}

              Message message = Message.obtain(msg);
              message.what = 8;
              Bundle bundle = new Bundle();
              bundle.putString("callback", "hello client !!");
              message.setData(bundle);
              try {
                mServer.handleCallBack(message);
              } catch (IllegalAccessException e) {
                Log.d(TAG, "onClick: IllegalAccessException :" + e.getMessage());
                e.printStackTrace();
              }
            }
          });
        }
      });

      startActivity(new Intent("cn.com.hwtc.Action_Connect"));
    }
  }
}
