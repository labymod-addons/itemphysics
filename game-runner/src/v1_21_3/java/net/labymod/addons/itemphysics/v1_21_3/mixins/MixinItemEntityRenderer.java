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

package net.labymod.addons.itemphysics.v1_21_3.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.labymod.addons.itemphysics.ItemPhysics;
import net.labymod.addons.itemphysics.ItemPhysicsRenderer;
import net.labymod.addons.itemphysics.bridge.BakedModel;
import net.labymod.addons.itemphysics.bridge.ItemEntity;
import net.labymod.addons.itemphysics.core.generated.DefaultReferenceStorage;
import net.labymod.addons.itemphysics.v1_21_3.client.ItemEntityRenderStateAccessor;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.api.client.world.item.ItemStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class MixinItemEntityRenderer
    extends EntityRenderer<net.minecraft.world.entity.item.ItemEntity, ItemEntityRenderState> {

  @Shadow
  @Final
  private RandomSource random;

  protected MixinItemEntityRenderer(Context $$0) {
    super($$0);
  }

  @Inject(
      method = "render(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
      at = @At("HEAD"),
      cancellable = true
  )
  private void itemPhysics$render(
      ItemEntityRenderState renderState,
      PoseStack stack,
      MultiBufferSource bufferSource,
      int packedLightCoords,
      CallbackInfo ci
  ) {
    DefaultReferenceStorage storage = ItemPhysics.get().referenceStorage();
    ItemPhysicsRenderer renderer = storage.itemPhysicsRenderer();
    boolean rendered = renderer.render(
        ((VanillaStackAccessor) stack).stack(),
        bufferSource,
        (ItemEntity) ((ItemEntityRenderStateAccessor) renderState).getItemEntity(),
        renderState.ageInTicks,
        (ItemStack) (Object) renderState.item,
        (BakedModel) renderState.itemModel,
        (net.labymod.addons.itemphysics.bridge.RandomSource) this.random,
        packedLightCoords
    );

    if (rendered) {
      this.shadowRadius = 0;
      super.render(renderState, stack, bufferSource, packedLightCoords);
      ci.cancel();
    }
  }


  @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At("TAIL")
  )
  private void itemPhysics$extractRenderState(
      net.minecraft.world.entity.item.ItemEntity entity,
      ItemEntityRenderState renderState,
      float tickDelta,
      CallbackInfo ci
  ) {
    ((ItemEntityRenderStateAccessor) renderState).setItemEntity(entity);
  }


}
