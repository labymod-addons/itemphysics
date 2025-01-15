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

package net.labymod.addons.itemphysics.v1_20_2.mixins.bridge;

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
