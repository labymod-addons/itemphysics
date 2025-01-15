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

package net.labymod.addons.itemphysics.v1_16_5.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.labymod.addons.itemphysics.ItemPhysics;
import net.labymod.addons.itemphysics.ItemPhysicsRenderer;
import net.labymod.addons.itemphysics.bridge.BakedModel;
import net.labymod.addons.itemphysics.bridge.ItemEntity;
import net.labymod.addons.itemphysics.core.generated.DefaultReferenceStorage;
import net.labymod.addons.itemphysics.util.JavaRandom;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.api.client.world.item.ItemStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ItemEntityRenderer.class)
public abstract class MixinItemEntityRenderer
    extends EntityRenderer<net.minecraft.world.entity.item.ItemEntity> {

  @Shadow
  @Final
  private Random random;

  @Shadow
  @Final
  private ItemRenderer itemRenderer;

  protected MixinItemEntityRenderer(EntityRenderDispatcher lvt_1_1_) {
    super(lvt_1_1_);
  }

  @Inject(
      method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
      at = @At("HEAD"),
      cancellable = true
  )
  private void itemPhysics$render(
      net.minecraft.world.entity.item.ItemEntity itemEntity,
      float yRot,
      float partialTicks,
      PoseStack stack,
      MultiBufferSource bufferSource,
      int packedLightCoords,
      CallbackInfo ci
  ) {
    DefaultReferenceStorage storage = ItemPhysics.get().referenceStorage();
    ItemPhysicsRenderer renderer = storage.itemPhysicsRenderer();
    var bakedModel = this.itemRenderer.getModel(
        itemEntity.getItem(),
        itemEntity.level,
        null
    );
    boolean rendered = renderer.render(
        ((VanillaStackAccessor) stack).stack(),
        bufferSource,
        (ItemEntity) itemEntity,
        itemEntity.getAge(),
        (ItemStack) (Object) itemEntity.getItem(),
        (BakedModel) bakedModel,
        JavaRandom.of(this.random),
        packedLightCoords
    );

    if (rendered) {
      this.shadowRadius = 0;
      super.render(itemEntity, yRot, partialTicks, stack, bufferSource, packedLightCoords);
      ci.cancel();
    }
  }
}
