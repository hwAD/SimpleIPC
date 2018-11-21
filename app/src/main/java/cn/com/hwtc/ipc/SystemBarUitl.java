package cn.com.hwtc.ipc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import java.lang.reflect.Method;

/**
 * Created by yuanc on 2017/8/4.
 */

public class SystemBarUitl {

  public static final String HW_STATUSBAR_MODE_EXTRA = "hw_statusbar_mode_extra";
      //   3:HOME键 , 4：BACK键 ,5：隐藏
  public static final String HW_STATUSBAR_SHOW_OR_HIDE_ACTION = "android.hw.statusbar.SHOW_OR_HIDE";
  public static final int HW_STATUSBAR_KEY_HOME = 3;
  public static final int HW_STATUSBAR_KEY_BACK = 4;
  public static final int HW_STATUSBAR_KEY_HIDE_NAVI = 5;
  public static final int HW_STATUSBAR_KEY_HIDE_STATUS = 6;
  public static final int HW_STATUSBAR_KEY_SHOW_STATUS = 7;

  @SuppressLint("PrivateApi")
  public static void collapsingNotification(Context context) {
    @SuppressLint("WrongConstant") Object service = context.getSystemService("statusbar");
    if (null == service) {
      return;
    }
    try {
      Class<?> clazz = Class.forName("android.app.StatusBarManager");
      int sdkVersion = android.os.Build.VERSION.SDK_INT;
      Method collapse;
      if (sdkVersion <= 16) {
        collapse = clazz.getMethod("collapse");
      } else {
        collapse = clazz.getMethod("collapsePanels");
      }
      collapse.setAccessible(true);
      collapse.invoke(service);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void hideNavigationBar(Activity activity) {
    View decorView = activity.getWindow().getDecorView();
    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    decorView.setSystemUiVisibility(uiOptions);
  }

  public static void showNavigationBar(Activity activity) {
    View decorView = activity.getWindow().getDecorView();
    int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
    decorView.setSystemUiVisibility(uiOptions);
  }

  public static void notifyNavigationBar(Context context, int state) {
    Intent intent = new Intent(HW_STATUSBAR_SHOW_OR_HIDE_ACTION);
    intent.putExtra(HW_STATUSBAR_MODE_EXTRA, state);
    context.sendBroadcast(intent);
  }
}
