package net.labymod.addons.itemphysics.v1_21_3.mixins.api;

import net.labymod.addons.itemphysics.bridge.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.world.entity.item.ItemEntity.class)
public abstract class MixinItemEntity extends Entity implements ItemEntity {

  @Shadow
  private int age;

  public MixinItemEntity(EntityType<?> $$0, Level $$1) {
    super($$0, $$1);
  }

  @Override
  public double itemPhysics$getPosX() {
    return this.position().x();
  }

  @Override
  public double itemPhysics$getPosY() {
    return this.position().y();
  }

  @Override
  public double itemPhysics$getPosZ() {
    return this.position().z();
  }

  @Override
  public int itemPhysics$getAge() {
    return this.age;
  }

  @Override
  public float itemPhysics$getYRot() {
    return this.getYRot();
  }

  @Override
  public boolean itemPhysics$isOnGround() {
    return this.onGround();
  }

  @Override
  public float itemPhysics$getXRot() {
    return this.getXRot();
  }

  @Override
  public void itemPhysics$setXRot(float xRot) {
    this.setXRot(xRot);
  }
}
