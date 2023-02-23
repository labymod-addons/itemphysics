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

package net.labymod.addons.itemphysics.v1_12_2.mixins;

import java.util.Random;
import net.labymod.addons.itemphysics.ItemPhysics;
import net.labymod.addons.itemphysics.ItemPhysicsConfiguration;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEntityItem.class)
public abstract class MixinItemEntityRenderer extends Render<EntityItem> {

  private ItemPhysicsConfiguration itemPhysics$configuration;

  @Shadow
  @Final
  private Random random;

  @Final
  @Shadow
  private RenderItem itemRenderer;

  protected MixinItemEntityRenderer(RenderManager renderManager) {
    super(renderManager);
  }

  @Shadow
  protected abstract int getModelCount(ItemStack itemStack);

  @Inject(
      method = "doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V",
      at = @At("HEAD"),
      cancellable = true
  )
  public void itemPhysics$modifyDroppedItemRendering(
      @NotNull EntityItem itemEntity,
      double x,
      double y,
      double z,
      float yaw,
      float partialTicks,
      CallbackInfo ci
  ) {
    if (this.itemPhysics$configuration == null) {
      this.itemPhysics$configuration = ItemPhysics.get().configuration();
      this.shadowSize = 0;
    }

    ItemStack itemStack = itemEntity.getItem();
    if (!this.itemPhysics$configuration.enabled().get() || itemStack.getItem() == null) {
      return;
    }

    if (this.itemPhysics$render(itemEntity, x, y, z)) {
      super.doRender(itemEntity, x, y, z, yaw, partialTicks);
      ci.cancel();
    }
  }

  private boolean itemPhysics$render(
      EntityItem entity,
      double x,
      double y,
      double z
  ) {
    if (entity.getAge() == 0) {
      return false;
    }

    ItemStack itemStack = entity.getItem();
    if (itemStack == null) {
      return false;
    }

    this.random.setSeed(
        itemStack.isEmpty() ? 187
            : Item.getIdFromItem(itemStack.getItem()) + itemStack.getMetadata()
    );

    boolean flag = false;
    if (this.bindEntityTexture(entity)) {
      this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity))
          .setBlurMipmap(false, false);
      flag = true;
    }

    GlStateManager.enableRescaleNormal();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableBlend();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.tryBlendFuncSeparate(
        GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
        GlStateManager.SourceFactor.ONE,
        GlStateManager.DestFactor.ZERO
    );

    GlStateManager.pushMatrix();
    IBakedModel bakedModel = this.itemRenderer.getItemModelWithOverrides(
        itemStack,
        entity.world,
        null
    );

    boolean isThreeDimensional = bakedModel.isGui3d();
    float rotateBy =
        ItemPhysics.getRotation() * 40 * this.itemPhysics$configuration.rotationSpeed().get();
    if (Minecraft.getMinecraft().isGamePaused()) {
      rotateBy = 0;
    }

    if (entity.rotationPitch > 360.0F) {
      entity.rotationPitch = 0.0F;
    }

    if (!Double.isNaN(entity.posX) && !Double.isNaN(entity.posY) && !Double.isNaN(entity.posZ)) {
      if (entity.onGround) {
        float rotationPitch = entity.rotationPitch;
        if (rotationPitch != 0.0F && rotationPitch != 90.0F && rotationPitch != 180.0F
            && rotationPitch != 270.0F) {
          double var1 = Math.abs(rotationPitch);
          double var2 = Math.abs(rotationPitch - 90.0F);
          double var3 = Math.abs(rotationPitch - 180.0F);
          double var4 = Math.abs(rotationPitch - 270.0F);

          // On ground rotation
          if ((var1 <= var2) && (var1 <= var3) && (var1 <= var4)) {
            if (entity.rotationPitch < 0.0F) {
              entity.rotationPitch += rotateBy;
            } else {
              entity.rotationPitch -= rotateBy;
            }
          }

          if ((var2 < var1) && (var2 <= var3) && (var2 <= var4)) {
            if (entity.rotationPitch - 90.0F < 0.0F) {
              entity.rotationPitch += rotateBy;
            } else {
              entity.rotationPitch -= rotateBy;
            }
          }

          if ((var3 < var2) && (var3 < var1) && (var3 <= var4)) {
            if (entity.rotationPitch - 180.0F < 0.0F) {
              entity.rotationPitch += rotateBy;
            } else {
              entity.rotationPitch -= rotateBy;
            }
          }

          if ((var4 < var2) && (var4 < var3) && (var4 < var1)) {
            if (entity.rotationPitch - 270.0F < 0.0F) {
              entity.rotationPitch += rotateBy;
            } else {
              entity.rotationPitch -= rotateBy;
            }
          }
        }
      } else {
        if (this.itemPhysics$isNearWater(entity)) {
          rotateBy /= 4.0D;
        }

        entity.rotationPitch += rotateBy;
      }
    }

    GlStateManager.translate((float) x, (float) y + 0.15F, (float) z);
    GL11.glRotatef(entity.rotationYaw, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(entity.rotationPitch + 90.0F, 1.0F, 0.0F, 0.0F);

    int modelCount = this.getModelCount(itemStack);
    ItemCameraTransforms camera = bakedModel.getItemCameraTransforms();
    float f = camera.ground.scale.x;
    float f1 = camera.ground.scale.y;
    float f2 = camera.ground.scale.z;

    if (!isThreeDimensional) {
      float f3 = -0.0F * (modelCount - 1) * 0.5F * f;
      float f4 = -0.0F * (modelCount - 1) * 0.5F * f1;
      float f5 = -0.09375F * (modelCount - 1) * 0.5F * f2;
      GlStateManager.translate(f3, f4, f5);
    }

    if (this.renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(this.getTeamColor(entity));
    }

    for (int k = 0; k < modelCount; ++k) {
      if (isThreeDimensional) {
        GlStateManager.pushMatrix();

        if (k > 0) {
          float f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
          float f9 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
          float f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
          GlStateManager.translate(f7, f9, f6);
        }

        camera.applyTransform(TransformType.GROUND);
        this.itemRenderer.renderItem(itemStack, bakedModel);
        GlStateManager.popMatrix();
      } else {
        GlStateManager.pushMatrix();
        if (k > 0) {
          float f8 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
          float f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
          GlStateManager.translate(f8, f10, 0.0F);
        }

        camera.applyTransform(TransformType.GROUND);
        this.itemRenderer.renderItem(itemStack, bakedModel);
        GlStateManager.popMatrix();
        GlStateManager.translate(0.0F * f, 0.0F * f1, 0.09375F * f2);
      }
    }

    if (this.renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    }

    GlStateManager.popMatrix();
    GlStateManager.disableRescaleNormal();
    GlStateManager.disableBlend();
    this.bindEntityTexture(entity);

    if (flag) {
      this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity))
          .restoreLastBlurMipmap();
    }

    return true;
  }

  private boolean itemPhysics$isNearWater(EntityItem entity) {
    if (entity.isInWater()) {
      return true;
    }

    World world = entity.getEntityWorld();
    BlockPos position = entity.getPosition();
    return Block.getIdFromBlock(world.getBlockState(position).getBlock()) == 9
        || Block.getIdFromBlock(world.getBlockState(position.up()).getBlock()) == 9;
  }
}
