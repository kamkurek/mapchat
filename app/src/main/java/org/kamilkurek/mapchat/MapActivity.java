package org.kamilkurek.mapchat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

  private GoogleMap map;
  private FusedLocationProviderClient fusedLocationClient;
  private Location location;
  private LocationService locationService;
  private Marker myLocationMarker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map);
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
  }

  @SuppressLint("MissingPermission")
  private void updateLocation() {
    fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> updateLocation(location));
  }

  private void updateLocation(Location location) {
    this.location = location;
    if(map != null) {
      if(myLocationMarker != null) myLocationMarker.remove();
      myLocationMarker = map.addMarker(new MarkerOptions().position(getLatLng()).title("You").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
      map.moveCamera(CameraUpdateFactory.newLatLng(getLatLng()));
      float zoomLevel = (float) 14.0;
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(getLatLng(), zoomLevel));
    }
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    map = googleMap;
    map.getUiSettings().setAllGesturesEnabled(false);
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
    } else {
      checkLocationPermission();
    }
    updateLocation();
    Intent intent = new Intent(this, LocationService.class);
    bindService(intent, locationServiceConnection, Context.BIND_AUTO_CREATE);
  }

  private ServiceConnection locationServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      LocationService.MyBinder locationBinder = (LocationService.MyBinder) iBinder;
      locationService = locationBinder.getService();
      locationService.registerHandler(new MarkersHandler(map, MapActivity.this));
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {}
  };

  private LatLng getLatLng() {
    if(location != null) {
      return new LatLng(location.getLatitude(), location.getLongitude());
    }
    return new LatLng(0, 0);
  }

  private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

  private void checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION );
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
    String permissions[], int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_LOCATION: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
          Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
        }
      }
    }
  }


}
