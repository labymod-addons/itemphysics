package net.labymod.addons.itemphysics.util;

public class FloatOptional {

  private final float value;
  private final boolean set;

  private FloatOptional(float value, boolean set) {
    this.value = value;
    this.set = set;
  }

  public static FloatOptional of(float value) {
    return new FloatOptional(value, true);
  }

  public static FloatOptional empty() {
    return new FloatOptional(0.0F, false);
  }

  public boolean isSet() {
    return this.set;
  }

  public float get() {
    return this.value;
  }

}
