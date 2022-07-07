package com.example.baskett1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.widget.Toast;

import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Timer;

public class calculateacce extends AppCompatActivity implements SensorEventListener{

    private float[] gravity = new float[3];
    float avgsp=0;
    float prevp=0;
    float Fmil=0;
    int rmv =0;
    Intent inten;
    boolean dumb =false;
    int num=0;
    float prevnum=0;
    ArrayList<Float> b5nt = new ArrayList<Float>();
    ArrayList<Float> b5n = new ArrayList<Float>();
    private float[] linear_acceleration = new float[3];
    float t=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       CountDownTimer t1 = new CountDownTimer(2000000000,1 ) {
           @Override
           public void onTick(long l) {
                Fmil++;
           }

           @Override
           public void onFinish() {
               Fmil=0;
           }
       };
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculateacce);
        t1.start();
         inten = new Intent(this,MainActivity.class);
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_GAME);
        //Fmil = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       /* if(t==0){
         t = ((event.timestamp/11)*3)/6;}
         float alpha = (float) (t/(t+0.02));

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];*/
        /*if(Math.max(Math.abs(event.values[2]),Math.abs(prevp))==prevp&&dumb){
            Intent inty = new Intent();
            inty.putExtra("avg",avgsp);
            inty.putExtra("num",num);
            setResult(RESULT_OK,inty);
            finish();
        }
        if(Math.max(Math.abs(event.values[2]),Math.abs(prevp))==prevp){
            dumb= true;
            avgsp = Math.max(Math.abs(event.values[2]),Math.abs(prevp));
        }
        avgsp = Math.max(Math.abs(event.values[2]),Math.abs(avgsp));
        num++;
        prevp = event.values[2];*/

        //Fmil+=event.timestamp;
        if(avgsp!=0){
        if(b5n.get(b5n.size()-1)>=event.values[2]){
            num=0;
            b5n.add(event.values[2]);
            b5nt.add((Fmil)/1000);
        }
        if(b5n.get(b5n.size()-1)-event.values[2]<=-0.1&&num>=1){

            for(int i=0;i<b5n.size()-num;i++){
                inten.putExtra("X: "+i,b5n.get(i));
                inten.putExtra("Y: "+i,b5nt.get(i));
            }
            inten.putExtra("leng",b5n.size()-num);
            setResult(RESULT_OK,inten);
            finish();
        }
        if(b5n.get(b5n.size()-1)-event.values[2]<=-0.1){
            num++;
            b5n.add(event.values[2]);
            b5nt.add((Fmil)/1000);
        }}else {

        if(event.values[2]<=-5){
            avgsp =1;
            b5n.add(event.values[2]);
            b5nt.add((Fmil)/1000);
        }
        else if(prevnum-event.values[2]>=-0.07){
            b5n.add(event.values[2]);
            b5nt.add((Fmil)/1000);
        }else if(prevnum-event.values[2]<=-0.07&&avgsp==0){
            b5n.clear();
            b5nt.clear();
        }}



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void end(int taked){
        Intent inten = new Intent();
        for(int i=0;i<b5n.size()-taked;i++){
            inten.putExtra("X: "+i,b5n.get(i));
            inten.putExtra("Y: "+i,b5nt.get(i));
        }
        inten.putExtra("leng",b5n.size()-taked);
        setResult(RESULT_OK,inten);
    }
}
