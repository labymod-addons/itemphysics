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

package net.labymod.addons.itemphysics.v1_21_4.mixins;

import net.labymod.addons.itemphysics.v1_21_4.client.ItemEntityRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntityRenderState.class)
public abstract class MixinItemEntityRenderState implements ItemEntityRenderStateAccessor {

  private ItemEntity itemPhysics$itemEntity;

  @Override
  public ItemEntity getItemEntity() {
    return this.itemPhysics$itemEntity;
  }

  @Override
  public void setItemEntity(ItemEntity itemEntity) {
    this.itemPhysics$itemEntity = itemEntity;
  }
}
