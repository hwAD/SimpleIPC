package cn.com.hwtc.simpleipc.utils;

import cn.com.hwtc.simpleipc.BuildConfig;

/**
 * Created by yuanc on 2017/3/28.
 */

public class L {

  private static boolean isPrint = BuildConfig.DEBUG;
  private static String TAG = BuildConfig.APPLICATION_ID;

  private L() {
  }

  public static void v(String TAG, String msg) {
    if (isPrint) {
      android.util.Log.v(TAG, buildMessage(msg));
    }
  }

  public static void v(String TAG, String msg, Throwable thr) {
    if (isPrint) {
      android.util.Log.v(TAG, buildMessage(msg), thr);
    }
  }

  public static void v(String msg) {
    if (isPrint) {
      android.util.Log.v(TAG, buildMessage(msg));
    }
  }

  public static void d(String TAG, String msg) {
    if (isPrint) {
      android.util.Log.d(TAG, buildMessage(msg));
    }
  }

  public static void d(String msg) {
    if (isPrint) {
      android.util.Log.d(TAG, buildMessage(msg));
    }
  }

  public static void d(String TAG, String msg, Throwable thr) {
    if (isPrint) {
      android.util.Log.d(TAG, buildMessage(msg), thr);
    }
  }

  public static void i(String TAG, String msg) {
    if (isPrint) {
      android.util.Log.i(TAG, buildMessage(msg));
    }
  }

  public static void i(String msg) {
    if (isPrint) {
      android.util.Log.i(TAG, buildMessage(msg));
    }
  }

  public static void i(String TAG, String msg, Throwable thr) {
    if (isPrint) {
      android.util.Log.i(TAG, buildMessage(msg), thr);
    }
  }

  public static void e(String TAG, String msg) {
    if (isPrint) {
      android.util.Log.e(TAG, buildMessage(msg));
    }
  }

  public static void e(String msg) {
    if (isPrint) {
      android.util.Log.e(TAG, buildMessage(msg));
    }
  }

  public static void w(String TAG, String msg) {
    if (isPrint) {
      android.util.Log.w(TAG, buildMessage(msg));
    }
  }

  public static void w(String msg) {
    if (isPrint) {
      android.util.Log.w(TAG, buildMessage(msg));
    }
  }

  public static void w(String TAG, String msg, Throwable thr) {
    if (isPrint) {
      android.util.Log.w(TAG, buildMessage(msg), thr);
    }
  }

  public static void w(String TAG, Throwable thr) {
    if (isPrint) {
      android.util.Log.w(TAG, buildMessage(""), thr);
    }
  }

  public static void e(String TAG, String msg, Throwable thr) {
    if (isPrint) {
      android.util.Log.e(TAG, buildMessage(msg), thr);
    }
  }

  private static String buildMessage(String msg) {
    StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
    return new StringBuilder().append(caller.getClassName())
        .append(".")
        .append(caller.getMethodName())
        .append("(): ")
        .append(msg)
        .toString();
  }
}
