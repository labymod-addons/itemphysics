package net.labymod.addons.itemphysics.bridge;

public interface ItemEntity {

  double itemPhysics$getPosX();

  double itemPhysics$getPosY();

  double itemPhysics$getPosZ();

  int itemPhysics$getAge();

  float itemPhysics$getYRot();

  boolean itemPhysics$isOnGround();

  float itemPhysics$getXRot();

  void itemPhysics$setXRot(float xRot);
}
