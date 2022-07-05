package net.labymod.addons.itemphysics;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonListener;

@AddonListener
public class ItemPhysics extends LabyAddon<ItemPhysicsConfiguration> {

  @Override
  protected void enable() {
    this.registerSettingCategory();
  }

  @Override
  protected Class<ItemPhysicsConfiguration> configurationClass() {
    return ItemPhysicsConfiguration.class;
  }
}
