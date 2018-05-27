package org.kamilkurek.mapchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.kamilkurek.mapchat.model.MarkerModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MarkersHandler implements Serializable, GoogleMap.OnMarkerClickListener {

  private final GoogleMap map;
  private final Activity activity;
  private final Map<Marker, MarkerModel> markers;
  private boolean run = true;
  private boolean buttonPressed = false;

  public MarkersHandler(GoogleMap map, Activity activity) {
    this.map = map;
    this.activity = activity;
    this.markers = new HashMap<>();
    map.setOnMarkerClickListener(this);
  }

  void update(List<MarkerModel> markerModels) {
    if(!run) return;
    run = false;
    activity.runOnUiThread(() -> {
      for(Marker marker : markers.keySet()) {
        marker.remove();
      }
      for(MarkerModel markerModel : markerModels) {
          Marker marker = map.addMarker(
            new MarkerOptions()
              .position(new LatLng(markerModel.getLat(), markerModel.getLng()))
              .title(markerModel.getName())
          );
          markers.put(marker, markerModel);
      }
    });
  }

  @Override
  public boolean onMarkerClick(Marker marker) {
    System.out.println("Marker CLICK");
    if(buttonPressed) {
      Intent intent = new Intent(activity, ChatActivity.class);
      intent.putExtra("marker", markers.get(marker));
      activity.startActivity(intent);
      buttonPressed = false;
    } else {
      buttonPressed = true;
      new Handler().postDelayed(() -> buttonPressed = false, 2000);
    }
    return false;
  }
}
