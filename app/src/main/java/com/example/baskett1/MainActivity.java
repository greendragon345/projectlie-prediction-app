package com.example.baskett1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.linear.MatrixUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button moveact;
    Boolean istouch;
    double[] xVector;

    Boolean isok;
    float startmil;
    float retmil;
    EditText you;
    EditText pog;
    EditText forL;
    EditText pmass;
    EditText omass;
    float m;
    EditText FY;
    double[] d5;
    float v0;
    Button SM;
    //Button confirmB;
   // Dialog confirmD;
   /* private SensorManager mSensorManager;
    private Sensor mLight;

    private SensorEventListener mLightSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d("MY_APP", event.toString());


            double oo = (event.sensor.getPower()/m);
            int treuev = (int)oo;
            if(treuev>-1&&istouch){
                v0 += oo  *((double)((event.timestamp)/1000000000.0));
                isok = true;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            Log.d("MY_APP", sensor.toString() + " - " + accuracy);

        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isok=false;
        pmass = findViewById(R.id.pmass);
        omass = findViewById(R.id.omass);
        startmil = System.currentTimeMillis();

        // Get sensor manager
       // mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Get the default sensor of specified type
     //   mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        moveact = findViewById(R.id.measureangle);
        SM = findViewById(R.id.startmeasuaring);
        pog = findViewById(R.id.ymeasure);
        you = findViewById(R.id.xmeaure);
        FY = findViewById(R.id.y0);
        forL = findViewById(R.id.foratmlength);
    //    SM.setOnTouchListener(this);
        SM.setOnClickListener(this);
        moveact.setOnClickListener(this);











    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode==RESULT_OK){
              /*  Toast.makeText(this,"txt:"+(data.getExtras().getFloat("avg")/data.getExtras().getInt("num",1)),Toast.LENGTH_LONG).show();
                retmil = System.currentTimeMillis();
                m= Float.parseFloat(pmass.getText()+"");
                float F = (data.getExtras().getFloat("avg",0)*((retmil-startmil)/1000))/2;
                float v1 = F*m;
                v0 = v1/Float.parseFloat(omass.getText()+"");*/
                double[] d1 = intenTarr(data,"Y: ");
                double[] d2 = intenTarr(data,"X: ");

                setValues(d2,d1);
                double x1 = d1[0];
                double x2 = d1[d1.length-1];
                double ax = d5[2];
                double bx = d5[1];
                double cx = d5[0];
                double s1 = ax/3*x1*x1*x1+bx/2*x1*x1+cx*x1;
                double s2 = ax/3*x2*x2*x2+bx/2*x2*x2+cx*x2;
                double sh = s1-s2;
                double spd = sh*(Double.parseDouble(pmass.getText().toString()));
                v0 = (float) ((float) spd/(Double.parseDouble(omass.getText().toString())));
                Toast.makeText(this,v0+"",Toast.LENGTH_LONG).show();

            }
        }
    }
   /* public void Confirm(){
        confirmD = new Dialog(this);
        confirmD.setContentView(R.layout.instruct);
        confirmD.setTitle("Confirm");
        confirmD.setCancelable(true);
        confirmB = confirmD.findViewById(R.id.switch_act);
        confirmB.setOnClickListener(this);
        confirmD.show();

    }*/
    public float calculate_angle(){
        double g = 9.807;
        double y = Double.parseDouble(pog.getText().toString());
        double x = Double.parseDouble(you.getText().toString());
        double y0 = Double.parseDouble(FY.getText().toString());
        double forarm = Double.parseDouble(forL.getText().toString());


         //   Toast.makeText(this,"angmin "+ma2,Toast.LENGTH_LONG).show();

        double minang =  0;
        double wantedamg = 0;
        for(double i = minang; i < 90; i += 0.00001)
        {
            double goodg = (x-(forarm*Math.cos((i * Math.PI) / 180))) / (v0 * Math.cos((i*Math.PI)/180));
            double wanted = (forarm*Math.sin((i * Math.PI) / 180))+y0+(v0 * Math.sin((i * Math.PI) / 180) * goodg) - ((g / 2) * (goodg * goodg));
            if (Math.abs(wanted - y) <= 0.001)
            {
                wantedamg = i;
                i = 90;
            }


        }
        if(wantedamg==0){
           if((Math.sqrt(2 * g * y))/v0>=1){
               Toast.makeText(this,"velocity to high",Toast.LENGTH_LONG).show();
           }else {
               Toast.makeText(this,"velocity too low",Toast.LENGTH_LONG).show();
           }
        }
        return (float) wantedamg;



    }






    @Override
    public void onClick(View view) {

         if(view==SM){
             Intent i1 = new Intent(this,calculateacce.class);
            
             startActivityForResult(i1,0);

         }else if(view==moveact){
             float ang = calculate_angle();
         //    Toast.makeText(this,""+ang,Toast.LENGTH_LONG).show();
       //     Confirm();


             Intent sog =  new Intent(this,MainActivity2.class);
                if(ang!=0){
             sog.putExtra("angle",ang);
             startActivity(sog);}
         }

    }
    public double[] intenTarr(Intent ing,String txt){
        double[] d1 = new double[ing.getExtras().getInt("leng")];
        for(int i=0;i<ing.getExtras().getInt("leng");i++){
            d1[i] = ing.getExtras().getFloat(txt+i);
        }
        return  d1;
    }
    public void setValues(double[] y, double[] x) {
        if (x.length != y.length) {
            throw new IllegalArgumentException(String.format("The numbers of y and x values must be equal (%d != %d)",y.length,x.length));
        }
        double[][] xData = new double[x.length][];
        for (int i = 0; i < x.length; i++) {
            // the implementation determines how to produce a vector of predictors from a single x
            xData[i] = xVector(x[i]);
        }

        OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();
        ols.setNoIntercept(true); // let the implementation include a constant in xVector if desired
        ols.newSampleData(y, xData); // provide the data to the model
         d5 = ols.estimateRegressionParameters(); // get our coefs

    }
    protected double[] xVector(double x) { // {1, x, x*x, x*x*x, ...}
        double[] poly = new double[3];
        double xi=1;
        for(int i=0; i<=2; i++) {
            poly[i]=xi;
            xi*=x;
        }
        return poly;
    }
}
