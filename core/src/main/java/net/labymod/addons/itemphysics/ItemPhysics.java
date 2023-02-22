/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.labymod.addons.itemphysics;

import net.labymod.addons.itemphysics.listener.GameRenderListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class ItemPhysics extends LabyAddon<ItemPhysicsConfiguration> {

  private static ItemPhysics instance;
  private long lastRenderTime;

  public ItemPhysics() {
    ItemPhysics.instance = this;
  }

  public static ItemPhysics get() {
    return instance;
  }

  public static float getRotation() {
    return (System.nanoTime() - ItemPhysics.get().lastRenderTime) / 100000000F;
  }

  @Override
  protected void enable() {
    this.registerSettingCategory();
    this.registerListener(new GameRenderListener(this));
  }

  @Override
  protected Class<ItemPhysicsConfiguration> configurationClass() {
    return ItemPhysicsConfiguration.class;
  }

  public void updateLastRenderTime() {
    this.lastRenderTime = System.nanoTime();
  }
}
