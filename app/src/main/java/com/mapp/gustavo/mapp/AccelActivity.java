package com.mapp.gustavo.mapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class AccelActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener{

    private Sensor sensor;
    private SensorManager sensorManager;
    private TextView xText, yText, zText;
    private Button toggleButton;
    private double gx, gy, gz;
    private boolean isSensorOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_accel);

        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);
        toggleButton = (Button) findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(this);
        isSensorOn = false;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        gx = 0;
        gy = 0;
        gz = 0;

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        isSensorOn = !isSensorOn;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (isSensorOn) {
            double alpha = 0.8;

            gx = alpha * gx + (1 - alpha) * sensorEvent.values[0];
            gy = alpha * gy + (1 - alpha) * sensorEvent.values[1];
            gz = alpha * gz + (1 - alpha) * sensorEvent.values[2];

            String line = Double.toString(gx) + "," + Double.toString(gy) + "," + Double.toString(gz) + "\n";
            String FILENAME = "mov_data.csv";
            File path = Environment.getExternalStorageDirectory();
            System.out.println(path.getAbsolutePath());
            File file = new File(path, FILENAME);
            try {
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write(line.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            xText.setText("X: " + (sensorEvent.values[0] - gx));
            yText.setText("Y: " + (sensorEvent.values[1] - gy));
            zText.setText("Z: " + (sensorEvent.values[2] - gz));

        } else {
            xText.setText("X");
            yText.setText("Y");
            zText.setText("Z");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
