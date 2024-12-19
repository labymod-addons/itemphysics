package net.labymod.addons.itemphysics.v1_21_3.mixins.api;

import net.labymod.addons.itemphysics.bridge.EntityMotion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class MixinEntity implements EntityMotion {

  @Shadow
  protected Vec3 stuckSpeedMultiplier;

  @Override
  public boolean itemPhysics$hasStuckSpeedMultiplier() {
    return this.stuckSpeedMultiplier != null;
  }

  @Override
  public double itemPhysics$lengthSqr() {
    return this.stuckSpeedMultiplier.lengthSqr();
  }

  @Override
  public double itemPhysics$getStuckSpeedMultiplierX() {
    return this.stuckSpeedMultiplier.x;
  }
}
