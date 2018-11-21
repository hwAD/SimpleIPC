package cn.com.hwtc.simpleipc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import cn.com.hwtc.simpleipc.permission.Permissions;

/**
 * Created by yuanc on 2018/11/20.
 */

class RemoteBinderBroadcast extends BroadcastReceiver {

  private String mInnerIPCAction;

  private OnBinderListener mOnBinderListener;

  void setOnBinderListener(OnBinderListener onBinderListener) {
    mOnBinderListener = onBinderListener;
  }

  void registerBroadcast(Context context, String action) {
    IntentFilter filter = new IntentFilter();
    filter.addAction(action);
    filter.addCategory(Permissions.CATEGORY_IPC);
    filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
    mInnerIPCAction = action;
    context.registerReceiver(this, filter, Permissions.PERMISSION_IPC, null);
  }

  private void unRegisterBroadcast(Context context) {
    context.unregisterReceiver(this);
  }

  @Override public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (mInnerIPCAction.equals(action)) {
      unRegisterBroadcast(context);
      //context.removeStickyBroadcast(intent);
      Bundle extras = intent.getExtras();
      if (extras != null) {
        IBinder binder = extras.getBinder(Config.ID_IPC_BINDER);
        if (binder != null) {
          if (mOnBinderListener != null) {
            mOnBinderListener.onGetBinder(binder);
          }
          linkToDeath(binder);
        }
      }
    }
  }

  private void linkToDeath(final IBinder binder) {
    try {
      binder.linkToDeath(new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
          binder.unlinkToDeath(this, 0);
          if (mOnBinderListener != null) {
            mOnBinderListener.onBinderDied();
          }
        }
      }, 0);
    } catch (RemoteException e) {
      e.printStackTrace();
    }
  }
}

