package com.example.avinash.task2;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by AVINASH on 6/17/2016.
 */
public class ServiceTimer extends Thread {
    MainActivity obj=new MainActivity();

    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()) {
            if (obj.sec == 60) {
                obj.sec = 0;
                obj.min = 1;
            }
           if(obj.index<9){
            Bundle b = new Bundle();
            b.putInt("index", obj.index);
            Message m = (obj.settext).obtainMessage();
            m.setData(b);
            try {
                java.lang.Thread.sleep(500);
            } catch (Exception e) {
                Log.d("Error", e.toString());
            }
            check();
            try {
                java.lang.Thread.sleep(500);
            } catch (Exception e) {
                Log.d("Error", e.toString());
            }
               obj.settext.sendMessage(m);

               if(obj.sec%3==0){
                if(obj.i==0){
                    obj.i=1;
                    }
                else {
                    obj.index++;
                }
               }
            obj.sec++;
           }
        }
    }

    public void check() {
        synchronized (obj.lock2) {
            while (!obj.flag) {
                try {obj.lock2.wait();
                } catch (Exception e) {}
            }
        }
    }

    public void pause() {
        obj.flag = false;
    }

    public void play() {
        obj.flag=true;
        synchronized(obj.lock2) {
            obj.lock2.notify();
        }
    }
}
