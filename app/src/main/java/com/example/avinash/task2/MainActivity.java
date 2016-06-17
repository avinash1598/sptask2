package com.example.avinash.task2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
//******************************%%%%%%%%%%%% PLEase REad %%%%%%%%%%%*************************************************************
// by default track1 is played when slideshow is pressed
//geturecontrol.java uses proximity sensor, enable and disable buton to trigger these functionality
//servicetimer.java is used to change the imageview using worker thread
//********IMP!!!!!!!!!!!!!!!!!! GESTURE WILL WORK EVEN WHEN THE SLIDE SHOW IS RUNNING if it is enabled*************
//********* when the player reaches end it will resumed from the begining in case of gesture control only *********
//*****************************************************************************************************************************

public class MainActivity extends AppCompatActivity  {
    public static ImageButton pl, ps;
    public static Button en, ds, sls;
    public static Spinner track;
    public static ImageView imv;
    public static TextView tv;

    public static int index=0;
    long t = 3000;
    static  Boolean flag=true;
    public static Object lock = new Object();
    public static Object lock2 = new Object();

    public static int sec=0,min=0,pos=0,select=0,i=0,k=0;
    static ServiceTimer t2;
    public static Handler settext;

    public String image[]={"a1.jpg","a2.jpg","a3.jpg","a4.jpg","a5.jpg","a6.jpg","a7.jpg","a8.jpg","a9.jpg"};
    public int id[] = {R.raw.download, R.raw.download1, R.raw.download2, R.raw.images3, R.raw.download4, R.raw.images5,
            R.raw.images6,R.raw.images7,R.raw.images8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pl = (ImageButton) findViewById(R.id.imageButton);
        ps = (ImageButton) findViewById(R.id.imageButton2);
        en = (Button) findViewById(R.id.button);
        ds = (Button) findViewById(R.id.button2);ds.setEnabled(false);
        sls = (Button) findViewById(R.id.button3);
        imv = (ImageView) findViewById(R.id.imageView);
        tv = (TextView) findViewById(R.id.textView2);

        track=(Spinner)findViewById(R.id.spinner);
        String array[]={"Select_Track","Track1/rawFolder","Track2/assetsFolder"};
        ArrayAdapter<String> aa=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner,array);

        track.setAdapter(aa);
        track.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;

                if(pos==1||pos==2)
                {select=1;
                    index=0;flag=true;i=0;sec=0;min=0;
                    t2.pause();
                Toast.makeText(getApplicationContext(),"Track"+""+pos,Toast.LENGTH_SHORT).show();}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                select=0;
            }
        });

        t2=new ServiceTimer();// servicetimer object

        settext=new Handler(){
            public void handleMessage(android.os.Message m){
                if(flag==true){
                    if(index==9){sec--;}
                    tv.setText(""+min+":"+""+sec);
                    if(sec%3==0&&index<9){
                        if(pos==0||pos==1) {
                            imv.setImageResource(id[index]);
                        }
                        else if(pos==2){
                            try
                            {
                                InputStream ims = getAssets().open(image[index]);
                                Drawable d = Drawable.createFromStream(ims, null);
                                imv.setImageDrawable(d);
                            } catch(IOException ex)
                            {
                                return;
                            }
                        }
                    }
                }
            }
        };
    }

    public void slideshow(View v) {

        if(index<=9&&index>=0){
            index=0;flag=true;i=0;sec=0;min=0;
            Thread.State s=t2.getState();
            Toast.makeText(getApplicationContext(),"ThreadState"+":"+s.toString(),Toast.LENGTH_SHORT).show();
            t2.play(); ;
        }
        if(k==0){
            t2.start();
            k=1;
        }
    }

    public void pause(View v) {
        Toast.makeText(this, "PAUSED", Toast.LENGTH_SHORT).show();
        t2.pause();
    }

    public void play(View v) {
        Toast.makeText(this, "PLAY", Toast.LENGTH_SHORT).show();
        if(select==1){
            index=0;flag=true;i=0;sec=0;min=0;
            select=0;
        }
        if(k==0){
            t2.start();
            k=1;
        }
        else
            t2.play();
    }

    public void enable(View v){
        if(k!=0)t2.pause();
        en.setEnabled(false);
        ds.setEnabled(true);
        startService(new Intent(this,GestureControl.class));
    }

    public void disable(View v){
        en.setEnabled(true);
        ds.setEnabled(false);
        stopService(new Intent(this,GestureControl.class));

    }

}


