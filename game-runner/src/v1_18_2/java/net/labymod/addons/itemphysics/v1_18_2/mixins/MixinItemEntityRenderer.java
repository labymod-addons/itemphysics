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

package net.labymod.addons.itemphysics.v1_18_2.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import net.labymod.addons.itemphysics.ItemPhysics;
import net.labymod.addons.itemphysics.ItemPhysicsConfiguration;
import net.labymod.api.client.render.matrix.Stack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class MixinItemEntityRenderer extends EntityRenderer<ItemEntity> {

  private ItemPhysicsConfiguration configuration;

  @Shadow
  @Final
  private Random random;

  @Shadow
  @Final
  private ItemRenderer itemRenderer;

  private MixinItemEntityRenderer(Context param0) {
    super(param0);
  }

  @Shadow
  public abstract int getRenderAmount(ItemStack param0);

  @Inject(at = @At("HEAD"), method = "render", cancellable = true)
  public void render(@NotNull ItemEntity itemEntity, float param1, float partialTicks,
      PoseStack poseStack, MultiBufferSource source, int i,
      CallbackInfo callbackInfo) {

    if (this.configuration == null) {
      this.configuration = ItemPhysics.get().configuration();
      this.shadowRadius = 0;
    }

    ItemStack itemStack = itemEntity.getItem();

    if (!this.configuration.enabled().get() || itemStack.isEmpty()) {
      return;
    }

    int renderCount = this.getRenderAmount(itemStack);

    Item item = itemStack.getItem();
    int seed = Item.getId(item) + itemStack.getDamageValue();
    this.random.setSeed(seed);
    BakedModel bakedModel = this.itemRenderer.getModel(itemStack, itemEntity.level, null, seed);

    Stack stack = Stack.create(poseStack);
    stack.push();

    float rotation =
        (((itemEntity.getAge() + partialTicks) / 20.0F + itemEntity.getBbHeight()) / 10)
            * this.configuration.rotationSpeed().get();

    stack.rotateRadians((float) Math.PI / 2, 1, 0, 0);
    stack.rotateRadians(itemEntity.getYRot(), 0, 0, 1);

    this.rotateX(itemEntity, rotation);
    if (bakedModel.isGui3d()) {
      stack.translate(0, -0.2f, -0.08f);
    } else if (itemEntity.level.getBlockState(itemEntity.blockPosition()).getBlock() == Blocks.SNOW
        || itemEntity.level.getBlockState(itemEntity.blockPosition().below()).getBlock()
        == Blocks.SOUL_SAND) {
      stack.translate(0, 0.0f, -0.14f);
    } else {
      stack.translate(0, 0, -0.04f);
    }

    float height = 0.2f;
    if (bakedModel.isGui3d()) {
      stack.translate(0, height, 0);
    }
    stack.rotateRadians(itemEntity.getXRot(), 0, 1, 0);
    if (bakedModel.isGui3d()) {
      poseStack.translate(0, -height, 0);
    }

    if (!bakedModel.isGui3d()) {
      float x = -0.0F * (renderCount - 1) * 0.5F;
      float y = -0.0F * (renderCount - 1) * 0.5F;
      float z = -0.09375F * (renderCount - 1) * 0.5F;
      stack.translate(x, y, z);
    }
    for (int k = 0; k < renderCount; ++k) {
      stack.push();
      if (k > 0) {
        if (bakedModel.isGui3d()) {
          float f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
          float f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
          float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
          stack.translate(f11, f13, f10);
        }
      }

      this.itemRenderer.render(itemStack, ItemTransforms.TransformType.GROUND, false, poseStack,
          source, i, OverlayTexture.NO_OVERLAY, bakedModel);
      stack.pop();
      if (!bakedModel.isGui3d()) {
        stack.translate(0.0f, 0.0f, 0.09375F);
      }
    }

    stack.pop();

    callbackInfo.cancel();
  }

  private void rotateX(ItemEntity itemEntity, float rotation) {
    if (itemEntity.isOnGround()) {
      return;
    }
    Fluid fluid = this.getFluid(itemEntity);
    if (fluid == null) {
      fluid = this.getFluid(itemEntity, true);
    }
    if (fluid != null) {
      rotation *= 0.25;
    }

    itemEntity.setXRot(itemEntity.getXRot() + rotation * 2);
  }

  private Fluid getFluid(ItemEntity item) {
    return this.getFluid(item, false);
  }

  private Fluid getFluid(ItemEntity item, boolean below) {
    double y = item.position().y;
    BlockPos pos = item.blockPosition();
    if (below) {
      pos = pos.below();
    }

    FluidState state = item.level.getFluidState(pos);
    Fluid fluid = state.getType();

    if (below) {
      return fluid;
    }

    double filled = state.getHeight(item.level, pos);

    if (y - pos.getY() - 0.2 <= filled) {
      return fluid;
    }
    return null;
  }
}
