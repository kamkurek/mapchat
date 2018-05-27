package org.kamilkurek.mapchat;

import org.kamilkurek.mapchat.model.MarkerModel;

import java.util.Random;

public class RandomMarkerGenerator {

  private static final Random random = new Random();

  public static MarkerModel generate(MarkerModel markerModel) {
    markerModel = new MarkerModel(markerModel.getLat() + random.nextDouble() / 100, markerModel.getLng() + random.nextDouble() / 100);
    return markerModel;
  }
}
