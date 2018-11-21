package cn.com.hwtc.ipc;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hwtc.simpleipc.IPCManager;
import java.lang.reflect.Field;

/**
 * Created by yuanc on 2018/11/21.
 */

public class Toaster {

  private Toast mToast;

  private volatile static Toaster sToastUtil;
  private final TextView mTextView;

  public static synchronized Toaster make(Context context) {
    if (null == sToastUtil) {
      synchronized (IPCManager.class) {
        if (null == sToastUtil) {
          sToastUtil = new Toaster(context);
        }
      }
    }
    return sToastUtil;
  }

  private Toaster(Context context) {
    mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    mTextView = getTextView();
    if(mTextView != null) {
      mTextView.setTextSize(38);
    }
  }

  public void destroy() {
    sToastUtil = null;
  }

  /**
   * @param duration {@link Toast#LENGTH_LONG },{@link Toast#LENGTH_SHORT}
   */
  public void show(int duration) {
    mToast.setDuration(duration);
    mToast.show();
  }

  public Toaster setTextSize(float size) {
    if(mTextView != null) {
      mTextView.setTextSize(size);
    }
    return this;
  }

  public Toaster colorHead(String color, String text) {
    String toastStr = "<font color='" + color + "'>" + text + "</font>";
    mToast.setText(Html.fromHtml(toastStr));
    return this;
  }

  public Toaster content(String text) {
    if(mTextView != null) {
      mTextView.append(text);
    }
    return this;
  }

  private TextView getTextView() {
    View view = mToast.getView();
    int message_id = getReflactField("com.android.internal.R$id", "message");
    return view.findViewById(message_id);
  }

  public void cancel() {
    mToast.cancel();
  }

  private int getReflactField(String className, String fieldName) {
    int result = -1;
    try {
      Class<?> clz = Class.forName(className);
      Field field = clz.getField(fieldName);
      field.setAccessible(true);
      result = field.getInt(null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
}
