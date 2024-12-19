package net.labymod.addons.itemphysics.v1_21_3.mixins.api;

import net.labymod.addons.itemphysics.bridge.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.util.RandomSource.class)
public interface MixinRandomSource extends RandomSource {

  @Shadow
  void setSeed(long var1);

  @Shadow
  float nextFloat();

  @Override
  default void itemPhysics$setSeed(long seed) {
    this.setSeed(seed);
  }

  @Override
  default float itemPhysics$nextFloat() {
    return this.nextFloat();
  }
}
