package net.labymod.addons.itemphysics.util;

import net.labymod.addons.itemphysics.bridge.RandomSource;
import java.util.Random;

public class JavaRandom implements RandomSource {

  private final Random random;

  private JavaRandom(Random random) {
    this.random = random;
  }

  public static JavaRandom of(Random random) {
    return new JavaRandom(random);
  }

  @Override
  public void itemPhysics$setSeed(long seed) {
    this.random.setSeed(seed);
  }

  @Override
  public float itemPhysics$nextFloat() {
    return this.random.nextFloat();
  }
}
