package net.labymod.addons.itemphysics.event;

import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.event.Event;

public class ItemEntityRenderEvent implements Event {

  private final Entity entity;
  private final double x;
  private final double y;
  private final double z;
  private final float yaw;
  private final float partialTicks;
  private final Stack matrixStack;

  public ItemEntityRenderEvent(Entity entity, Stack matrixStack, double x, double y, double z,
      float yaw, float partialTicks) {
    this.entity = entity;
    this.x = x;
    this.y = y;
    this.z = z;
    this.matrixStack = matrixStack;
    this.yaw = yaw;
    this.partialTicks = partialTicks;
  }

  public Stack matrixStack() {
    return matrixStack;
  }

  public Entity entity() {
    return entity;
  }

  public double x() {
    return x;
  }

  public double y() {
    return y;
  }

  public double z() {
    return z;
  }

  public float yaw() {
    return yaw;
  }

  public float partialTicks() {
    return partialTicks;
  }

}
