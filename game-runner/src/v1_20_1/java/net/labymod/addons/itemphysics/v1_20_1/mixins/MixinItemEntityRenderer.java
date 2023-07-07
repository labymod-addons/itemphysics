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

package net.labymod.addons.itemphysics.v1_20_1.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.labymod.addons.itemphysics.ItemPhysics;
import net.labymod.addons.itemphysics.ItemPhysicsConfiguration;
import net.labymod.addons.itemphysics.v1_20_1.client.VersionedEntityAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class MixinItemEntityRenderer extends EntityRenderer<ItemEntity> {

  private ItemPhysicsConfiguration itemPhysics$configuration;

  @Shadow
  @Final
  private ItemRenderer itemRenderer;

  @Shadow
  @Final
  private RandomSource random;

  protected MixinItemEntityRenderer(Context context) {
    super(context);
  }

  @Shadow
  protected abstract int getRenderAmount(ItemStack itemStack);

  @Inject(
      method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
      at = @At("HEAD"),
      cancellable = true
  )
  private void itemPhysics$modifyDroppedItemRendering(
      ItemEntity itemEntity,
      float f,
      float g,
      PoseStack poseStack,
      MultiBufferSource multiBufferSource,
      int i,
      CallbackInfo ci
  ) {
    if (this.itemPhysics$configuration == null) {
      this.itemPhysics$configuration = ItemPhysics.get().configuration();
      this.shadowRadius = 0;
    }

    ItemStack itemStack = itemEntity.getItem();
    if (!this.itemPhysics$configuration.enabled().get() || itemStack.isEmpty()) {
      return;
    }

    if (this.itemPhysics$render(itemEntity, poseStack, multiBufferSource, i)) {
      super.render(itemEntity, f, g, poseStack, multiBufferSource, i);
      ci.cancel();
    }
  }

  private boolean itemPhysics$render(
      ItemEntity entity,
      PoseStack pose,
      MultiBufferSource buffer,
      int packedLight
  ) {
    if (entity.getAge() == 0) {
      return false;
    }

    pose.pushPose();
    ItemStack itemStack = entity.getItem();
    this.random.setSeed(
        itemStack.isEmpty() ? 187 : Item.getId(itemStack.getItem()) + itemStack.getDamageValue()
    );

    BakedModel bakedModel = this.itemRenderer.getModel(itemStack, entity.level(), null,
        entity.getId());
    boolean isThreeDimensional = bakedModel.isGui3d();
    pose.mulPose(Axis.XP.rotation((float) Math.PI / 2));
    pose.mulPose(Axis.ZP.rotation(entity.getYRot()));

    Minecraft minecraft = Minecraft.getInstance();
    boolean applyEffects =
        entity.getAge() != 0 && (isThreeDimensional || minecraft.options != null);

    //Handle Rotations
    if (applyEffects) {
      float rotateBy =
          ItemPhysics.getRotation() * this.itemPhysics$configuration.rotationSpeed().get();
      if (minecraft.isPaused()) {
        rotateBy = 0;
      }

      Vec3 motionMultiplier = this.itemPhysics$getStuckSpeedMultiplier(entity);
      if (motionMultiplier != null && motionMultiplier.lengthSqr() > 0) {
        rotateBy *= motionMultiplier.x * 0.2;
      }

      if (isThreeDimensional) {
        if (!entity.onGround()) {
          rotateBy *= 2;
          Fluid fluid = this.itemPhysics$getFluid(entity);
          if (fluid == null) {
            fluid = this.itemPhysics$getFluid(entity, true);
          }

          if (fluid != null) {
            rotateBy /= (1 + this.itemPhysics$getViscosity(fluid, entity.level()));
          }

          entity.setXRot(entity.getXRot() + rotateBy);
        }
      } else if (!Double.isNaN(entity.getX()) && !Double.isNaN(entity.getY()) && !Double.isNaN(
          entity.getZ())) {
        if (entity.onGround()) {
          entity.setXRot(0);
        } else {
          rotateBy *= 2;
          Fluid fluid = this.itemPhysics$getFluid(entity);
          if (fluid != null) {
            rotateBy /= (1 + this.itemPhysics$getViscosity(fluid, entity.level()));
          }

          entity.setXRot(entity.getXRot() + rotateBy);
        }
      }

      if (isThreeDimensional) {
        pose.translate(0, -0.2, -0.08);
      } else if (
          entity.level().getBlockState(entity.blockPosition()).getBlock() == Blocks.SNOW
              || entity.level().getBlockState(entity.blockPosition().below()).getBlock()
              == Blocks.SOUL_SAND) {
        pose.translate(0, 0.0, -0.14);
      } else {
        pose.translate(0, 0, -0.04);
      }

      double height = 0.2;
      if (isThreeDimensional) {
        pose.translate(0, height, 0);
      }

      pose.mulPose(Axis.YP.rotation(entity.getXRot()));
      if (isThreeDimensional) {
        pose.translate(0, -height, 0);
      }
    }

    int modelAmount = this.getRenderAmount(itemStack);
    if (!isThreeDimensional) {
      float f7 = -0.0F * (modelAmount - 1) * 0.5F;
      float f8 = -0.0F * (modelAmount - 1) * 0.5F;
      float f9 = -0.09375F * (modelAmount - 1) * 0.5F;
      pose.translate(f7, f8, f9);
    }

    for (int k = 0; k < modelAmount; ++k) {
      pose.pushPose();
      if (k > 0 && isThreeDimensional) {
        float f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
        float f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
        float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
        pose.translate(f11, f13, f10);
      }

      this.itemRenderer.render(itemStack, ItemDisplayContext.GROUND, false, pose, buffer,
          packedLight, OverlayTexture.NO_OVERLAY, bakedModel);
      pose.popPose();
      if (!isThreeDimensional) {
        pose.translate(0.0, 0.0, 0.09375F);
      }
    }

    pose.popPose();
    return true;
  }

  private Fluid itemPhysics$getFluid(ItemEntity item) {
    return this.itemPhysics$getFluid(item, false);
  }

  private Fluid itemPhysics$getFluid(ItemEntity item, boolean below) {
    double d0 = item.position().y;
    BlockPos pos = item.blockPosition();
    if (below) {
      pos = pos.below();
    }

    FluidState state = item.level().getFluidState(pos);
    Fluid fluid = state.getType();
    if (fluid.getTickDelay(item.level()) == 0) {
      return null;
    }

    if (below) {
      return fluid;
    }

    double filled = state.getHeight(item.level(), pos);
    if (d0 - pos.getY() - 0.2 <= filled) {
      return fluid;
    }

    return null;
  }

  private Vec3 itemPhysics$getStuckSpeedMultiplier(Entity entity) {
    return ((VersionedEntityAccessor) entity).getStuckSpeedMultiplier();
  }

  private float itemPhysics$getViscosity(Fluid fluid, Level level) {
    if (fluid == null) {
      return 0;
    }

    return fluid.getTickDelay(level);
  }
}
