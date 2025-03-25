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

package net.labymod.addons.itemphysics.v1_21_5.mixins.bridge;

import net.labymod.addons.itemphysics.v1_21_5.client.ItemClusterRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemClusterRenderState.class)
public class MixinItemClusterRenderState implements ItemClusterRenderStateAccessor {

  private ItemStack itemPhysics$itemStack;

  @Inject(method = "extractItemGroupRenderState", at = @At("TAIL"))
  private void itemPhysics$storeItemStack(
      Entity entity,
      ItemStack itemStack,
      ItemModelResolver resolver,
      CallbackInfo ci
  ) {
    this.itemPhysics$itemStack = itemStack;
  }

  @Override
  public ItemStack itemPhysics$getItemStack() {
    return this.itemPhysics$itemStack;
  }
}
