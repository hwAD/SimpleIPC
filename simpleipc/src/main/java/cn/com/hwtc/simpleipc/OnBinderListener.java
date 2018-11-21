package cn.com.hwtc.simpleipc;

import android.os.IBinder;

/**
 * Created by yuanc on 2018/11/20.
 */

interface OnBinderListener {

  void onGetBinder(IBinder binder);

  void onBinderDied();
}
