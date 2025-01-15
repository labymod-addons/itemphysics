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

package net.labymod.addons.itemphysics.v1_8_9.mixins.bridge;

import net.labymod.addons.itemphysics.bridge.ItemEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityItem.class)
public abstract class MixinItemEntity extends Entity implements ItemEntity {

  @Shadow
  private int age;

  public MixinItemEntity(World lvt_1_1_) {
    super(lvt_1_1_);
  }

  @Override
  public double itemPhysics$getPosX() {
    return this.posX;
  }

  @Override
  public double itemPhysics$getPosY() {
    return this.posY;
  }

  @Override
  public double itemPhysics$getPosZ() {
    return this.posZ;
  }

  @Override
  public int itemPhysics$getAge() {
    return this.age;
  }

  @Override
  public float itemPhysics$getYRot() {
    return this.rotationYaw;
  }

  @Override
  public boolean itemPhysics$isOnGround() {
    return this.onGround;
  }

  @Override
  public float itemPhysics$getXRot() {
    return this.rotationPitch;
  }

  @Override
  public void itemPhysics$setXRot(float xRot) {
    this.rotationPitch = xRot;
  }
}
