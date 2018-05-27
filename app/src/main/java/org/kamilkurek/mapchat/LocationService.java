package org.kamilkurek.mapchat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import org.kamilkurek.mapchat.model.MarkerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LocationService extends Service {

  private MarkersHandler markersHandler;
  private final IBinder mBinder = new MyBinder();

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
      System.out.println("LOOP.");
      List<MarkerModel> list = new ArrayList<>();
      MarkerModel markerModel = RandomMarkerGenerator.generate(new MarkerModel(50.052530, 19.913654));
      list.add(markerModel);
      if(markersHandler != null) {
        markersHandler.update(list);
      }
    },1, 1, TimeUnit.SECONDS);
    return mBinder;
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  public void registerHandler(MarkersHandler markersHandler) {
    this.markersHandler = markersHandler;
  }

  public class MyBinder extends Binder {
    LocationService getService() {
      return LocationService.this;
    }
  }



}
