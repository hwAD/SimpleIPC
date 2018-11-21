# SimpleIPC
一个使用简便，高效的Android进程间通信IPC框架。


##特色

1. 不需要创建Service,不需要编写AIDL，使得进程间通信像本地使用Handler一样方便简单。

2. 支持阻塞式和非阻塞式的调用。

3. 支持进程间函数回调，调用其他进程函数的时候可以传入回调函数，让其他进程回调本进程的方法。

   目前初步实现了基本IPC通信功能，开发中。。。


##Gradle

```
dependencies {
    compile 'com.github.hwtc:SimpleIPC:v1.0.1'
}
```



##使用方法

##客户端获取BaseClient
```
BaseClient client = IPCManager.get().connect(this, ServerActivity.ACTION_IPC);

##BaseClient提供12中通信方法：（基本上看方法名就能知道如何使用）

  boolean sendMessage(String msg);

  boolean sendMessageDelayed(String msg,long uptimeMillis);

  boolean sendMessageAtTime(String msg,long uptimeMillis);

  boolean sendMessageBlocked(String msg);

  boolean sendMessage(Message msg);

  boolean sendMessageDelayed(Message msg,long uptimeMillis);

  boolean sendMessageAtTime(Message msg,long uptimeMillis);

  boolean sendMessageBlocked(Message msg);

  boolean sendMessageWithCallBack(String msg, OnMessageLisenter onMessageLisenter);

  boolean sendMessageWithCallBackBlocked(String msg,
      OnMessageLisenter onMessageLisenter);

  boolean sendMessageWithCallBack(Message msg, OnMessageLisenter onMessageLisenter);

  boolean sendMessageWithCallBackBlocked(Message msg,
      OnMessageLisenter onMessageLisenter);
```

##服务端获取BaseServer
```
BaseServer server = IPCManager.get().accept(this, ServerActivity.ACTION_IPC);
server.setOnMessageLisenter(new OnMessageLisenter() {
        //根据客户端传递的消息类型提供两种监听方法
        @Override public void onMessageReceive( String msg) {
        }

        @Override public void onMessageReceive( Message msg) {
      });
      
void handleCallBack(Message msg) throws IllegalAccessException 当需要项客户端回调消息时调用

```


##注意事项

1. 原则上，需要先调用客服端的connect（）。



