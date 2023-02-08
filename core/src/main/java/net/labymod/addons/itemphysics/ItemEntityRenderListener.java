package net.labymod.addons.itemphysics;

import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.entity.EntityRenderEvent;

public class ItemEntityRenderListener {

  private final ItemPhysics addon = ItemPhysics.get();
  private final ItemPhysicsConfiguration configuration = addon.configuration();

  @Subscribe
  public void onRenderEntity(EntityRenderEvent event) {

  }

}
