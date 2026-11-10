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

package net.labymod.addons.itemphysics.v26_2.client;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.inject.Singleton;
import net.labymod.addons.itemphysics.bridge.BakedModel;
import net.labymod.addons.itemphysics.bridge.ItemEntity;
import net.labymod.addons.itemphysics.bridge.VersionBridge;
import net.labymod.addons.itemphysics.util.FloatOptional;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.item.Item;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

@Singleton
@Implements(VersionBridge.class)
public class VersionedVersionBridge implements VersionBridge {

  @Override
  public int getItemId(ItemStack itemStack) {
    Item item = itemStack.getAsItem();
    return net.minecraft.world.item.Item.getId((net.minecraft.world.item.Item) item);
  }

  @Override
  public boolean isGamePaused() {
    return Minecraft.getInstance().isPaused();
  }

  @Override
  public void renderItem(
      ItemStack itemStack,
      Stack stack,
      Object submitContext,
      int packedLightCoords,
      BakedModel bakedModel
  ) {
    // Since 1.21.10 there is no buffer source to draw into here. The item is submitted from the
    // render state the game already extracted, which is also why the stack is not read any more.
    SubmitContext context = (SubmitContext) submitContext;
    context.item().submit(
        (PoseStack) stack.getProvider().getPoseStack(),
        context.collector(),
        packedLightCoords,
        OverlayTexture.NO_OVERLAY,
        context.overlayColor()
    );
  }

  @Override
  public boolean isSpecialBlock(ItemEntity entity) {
    net.minecraft.world.entity.item.ItemEntity itemEntity = (net.minecraft.world.entity.item.ItemEntity) entity;
    Level level = itemEntity.level();

    BlockPos blockPos = itemEntity.blockPosition();
    return level.getBlockState(blockPos).getBlock() == Blocks.SNOW ||
        level.getBlockState(blockPos.below()).getBlock() == Blocks.SOUL_SAND;
  }

  @Override
  public boolean hasOptions() {
    return Minecraft.getInstance().options != null;
  }

  @Override
  public FloatOptional getFluidViscosity(ItemEntity entity, boolean withBelow) {
    net.minecraft.world.entity.item.ItemEntity itemEntity = (net.minecraft.world.entity.item.ItemEntity) entity;
    Level level = itemEntity.level();

    Fluid fluid = this.getFluid(itemEntity);
    if (withBelow) {
      fluid = this.getFluid(itemEntity, true);
    }

    return fluid == null ? FloatOptional.empty() : FloatOptional.of(fluid.getTickDelay(level));
  }

  private Fluid getFluid(net.minecraft.world.entity.item.ItemEntity entity) {
    return this.getFluid(entity, false);
  }

  private Fluid getFluid(net.minecraft.world.entity.item.ItemEntity entity, boolean below) {
    double y = entity.position().y;
    BlockPos pos = entity.blockPosition();
    if (below) {
      pos = pos.below();
    }

    Level level = entity.level();
    FluidState state = level.getFluidState(pos);
    Fluid fluidType = state.getType();
    if (fluidType.getTickDelay(level) == 0) {
      return null;
    }

    if (below) {
      return fluidType;
    }

    float filled = state.getHeight(level, pos);
    if (y - pos.getY() - 0.2 <= filled) {
      return fluidType;
    }

    return null;
  }

}
