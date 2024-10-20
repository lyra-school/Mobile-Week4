package com.example.week4;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView tvx, tvy, tvz;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean isFlat = false;

    private MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().setTitle("Accelerometer");
        tvx = findViewById(R.id.tvxval); // this assumes
        //there are three textviews
        tvy = findViewById(R.id.tvyval); // in your xml file
        // called tvxval, tvyval
        tvz = findViewById(R.id.tvzval); // and tvzval

        mp = MediaPlayer.create(this, R.raw.airhorn);

        // choose the sensor you want
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    /*
    * When the app is brought to the foreground - using app on screen
    */
    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * App running but not on screen - in the background
     */
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this); // turn off listener to save power
    }

    /*
    * Called by the system every x milllisecs when sensor changes
    */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // called byt the system every x ms
        float x, y, z;
        x = Math.abs(event.values[0]); // get x value from sensor
        y = Math.abs(event.values[1]);
        z = Math.abs(event.values[2]);

        tvx.setText(String.valueOf(x));
        tvy.setText(String.valueOf(y));
        tvz.setText(String.valueOf(z));

        if(x < 1 && y < 1 && z > 9) { // phone is flat
            if(!isFlat) {
                isFlat = true;
                Toast.makeText(this, "Phone is flat", Toast.LENGTH_SHORT).show();
                mp.start();
            }
        }
    }

    public void reset(View view) {
        isFlat = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not using
    }
}