package com.example.baskett1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {

    TextView t1;
    float angle;
    float gravity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        gravity = (float) 9.806;
        t1= findViewById(R.id.gouy);
        Intent inj = getIntent();
        angle = inj.getExtras().getFloat("angle",0);
        Toast.makeText(this,""+angle,Toast.LENGTH_LONG).show();
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_GAME);
    }
//
    @Override
    public void onSensorChanged(SensorEvent event) {
        float angleD = (float) (Math.asin(event.values[1]/gravity)*(180/Math.PI));
        if(angleD-angle<=0.5&&angleD-angle>=-0.5){
            t1.setBackgroundColor(Color.GREEN);
            t1.setText("");
        }else if(angle<angleD){
            t1.setBackgroundColor(Color.RED);
            t1.setText("-"+"\n"+angleD);
        }else if(angle>angleD) {
            t1.setBackgroundColor(Color.RED);
            t1.setText("+"+"\n"+angleD);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}