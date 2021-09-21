package com.sufian.handler_thread;

import static com.sufian.handler_thread.ExampleHandlerThread.EXAMPLE_TASK;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final ExampleHandlerThread handlerThread = new ExampleHandlerThread();
    private final ExampleRunnable1 exampleRunnable1 = new ExampleRunnable1();

    private final Object token = new Object();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThread.start();

        findViewById(R.id.btnDOWork).setOnClickListener(v->doWork());

        findViewById(R.id.btnRemoveMsg).setOnClickListener(v->removeMessages());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }

    private void doWork(){
        Message msg = Message.obtain(handlerThread.getHandler());
        msg.what = EXAMPLE_TASK;
        msg.arg1 = 23;
        msg.obj = "Obj String";
        // msg.setData();

        msg.sendToTarget();



        handlerThread.getHandler().postAtTime(exampleRunnable1, token, SystemClock.uptimeMillis());
        handlerThread.getHandler().post(exampleRunnable1);
//        handlerThread.getHandler().postAtFrontOfQueue(new ExampleRunnable2());
    }

    private void removeMessages(){
        handlerThread.getHandler().removeCallbacks(exampleRunnable1, token);
    }

    private static  class ExampleRunnable1 implements Runnable{

        @Override
        public void run() {
            for (int i = 0; i < 4; i++) {
                Log.d(TAG, "Runnable1: " + i);
                SystemClock.sleep(1000);
            }
        }
    }

    private static  class ExampleRunnable2 implements Runnable{

        @Override
        public void run() {
            for (int i = 0; i < 4; i++) {
                Log.d(TAG, "Runnable2: " + i);
                SystemClock.sleep(1000);
            }
        }
    }
}