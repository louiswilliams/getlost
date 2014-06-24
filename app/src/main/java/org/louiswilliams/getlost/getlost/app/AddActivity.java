package org.louiswilliams.getlost.getlost.app;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class AddActivity extends ActionBarActivity {

    private GoogleMap map;
    private MapFragment mapFragment;
    private Marker marker;
    private LocationManager locationManager;
    private Location currentLocation;
    private String provider;
    private LatLng markerLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), true);
        currentLocation = locationManager.getLastKnownLocation(provider);

        setUpMapIfNeeded();

        Button doneButton = (Button) findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker != null) {
                    Intent result = new Intent();
                    result.putExtra("lat",marker.getPosition().latitude);
                    result.putExtra("long",marker.getPosition().longitude);
                    setResult(RESULT_OK, result);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMap() {
        map.setMyLocationEnabled(true);
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                drawMarker(latLng);
//                Toast.makeText(getApplicationContext(), latLng.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpMapIfNeeded() {

        if (map == null) {
            map = mapFragment.getMap();
        } else {
            setUpMap();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void drawMarker(LatLng latLng) {
        map.clear();
        marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("New Location")
                        .draggable(true)
        );
    }
}
