package com.example.avinash.task2;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by AVINASH on 6/17/2016.
 */
public class GestureControl extends Service implements SensorEventListener {
    MainActivity obj=new MainActivity();
   public Sensor proximity;
    public SensorManager sm;
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate(){
        Toast.makeText(getApplicationContext(),"serviceStarted",Toast.LENGTH_SHORT).show();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){
    }
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0] == 0){
            obj.t2.pause();
          if(obj.pos==0||obj.pos==1){
              if(obj.index<9){
                  obj.imv.setImageResource(obj.id[obj.index]);
                  obj.index++;}
              if(obj.index==9){obj.index=0;obj.i=0;obj.sec=0;obj.min=0;}

          }
            else if(obj.pos==2){
              if(obj.index<9){

                  try
                  {
                      InputStream ims = getAssets().open(obj.image[obj.index]);
                      Drawable d = Drawable.createFromStream(ims, null);
                      obj.imv.setImageDrawable(d);
                  } catch(IOException ex)
                  {
                      return;
                  }
                  obj.index++;}
              if(obj.index==9){obj.index=0;obj.i=0;obj.sec=0;obj.min=0;}

          }
        }
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        sm.unregisterListener( this);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this,"CommandStarted",Toast.LENGTH_SHORT).show();
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        proximity=sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sm.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);

        return Service.START_STICKY;
    }

}
