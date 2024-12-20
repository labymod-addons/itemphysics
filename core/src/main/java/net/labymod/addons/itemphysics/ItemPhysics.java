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

import net.labymod.addons.itemphysics.core.generated.DefaultReferenceStorage;
import net.labymod.addons.itemphysics.listener.GameRenderListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.loader.MinecraftVersions;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.util.time.TimeUtil;

@AddonMain
public class ItemPhysics extends LabyAddon<ItemPhysicsConfiguration> {

  private static final boolean USE_LEGACY_SPEED = MinecraftVersions.V1_12_2.orOlder();
  private static final float LEGACY_SPEED = 200_000_000.0F;
  private static final float MODERN_SPEED = 100_000_000.0F;
  private static ItemPhysics instance;
  private long lastRenderTime;

  public ItemPhysics() {
    ItemPhysics.instance = this;
  }

  public static ItemPhysics get() {
    return instance;
  }

  public static float getRotation() {
    return (TimeUtil.getNanoTime() - ItemPhysics.get().lastRenderTime) / (USE_LEGACY_SPEED ? LEGACY_SPEED : MODERN_SPEED);
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

  public DefaultReferenceStorage referenceStorage() {
    return this.referenceStorageAccessor();
  }

  public void updateLastRenderTime() {
    this.lastRenderTime = TimeUtil.getNanoTime();
  }
}
