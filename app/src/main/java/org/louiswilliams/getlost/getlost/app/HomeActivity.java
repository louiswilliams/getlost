package org.louiswilliams.getlost.getlost.app;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;


public class HomeActivity extends ActionBarActivity implements SensorEventListener, LocationListener{


    private CharSequence mTitle;
    private TextView mLabel;
    private LocationManager mLocationManager;
    private Location currentLocation;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetic;
    private float[] mAccelVector;
    private float[] mMagVector;
    private float[] mOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mLabel = (TextView) findViewById(R.id.label);
        mLabel.setText("Hello world");

        // Initialize sensor manager and sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if ( mMagnetic == null || mAccelerometer == null) {
            mLabel.setText("This device is cannot be used.");
        } else {
            mOrientation = new float[3];
        }

        // Initialize location mananger and location
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Register sensor listeners
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);

        currentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // Register Location listeners. Once for GPS, the other for Network
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onActivityResult(int request, int result, Intent data) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_place) {
            startActivityForResult(new Intent(getApplicationContext(), AddActivity.class), 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            mAccelVector = event.values;
        }
        if (event.sensor == mMagnetic) {
            mMagVector = event.values;
        }
        if (mMagVector != null && mAccelVector !=null) {
            float[]rMatrix = new float[9];
            if (SensorManager.getRotationMatrix(rMatrix, null, mAccelVector, mMagVector)) {
                SensorManager.getOrientation(rMatrix, mOrientation);
        //        mLabel.setText("degrees from N: " + (180*mOrientation[0]/(Math.PI)) );
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        mLabel.setText(Float.toString(currentLocation.getBearing()));
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}
}
