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

package net.labymod.addons.itemphysics.v1_8_9.mixins;

import net.labymod.addons.itemphysics.ItemPhysics;
import net.labymod.addons.itemphysics.ItemPhysicsRenderer;
import net.labymod.addons.itemphysics.bridge.BakedModel;
import net.labymod.addons.itemphysics.bridge.ItemEntity;
import net.labymod.addons.itemphysics.core.generated.DefaultReferenceStorage;
import net.labymod.addons.itemphysics.util.JavaRandom;
import net.labymod.api.client.gfx.pipeline.RenderEnvironmentContext;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.v1_8_9.client.render.matrix.VersionedStackProvider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Random;

@Mixin(RenderEntityItem.class)
public abstract class MixinItemEntityRenderer extends Render<EntityItem> {

  @Shadow
  @Final
  private RenderItem itemRenderer;

  @Shadow
  private Random field_177079_e;

  protected MixinItemEntityRenderer(RenderManager lvt_1_1_) {
    super(lvt_1_1_);
  }

  @Inject(
      method = "doRender(Lnet/minecraft/entity/item/EntityItem;DDDFF)V",
      at = @At("HEAD"),
      cancellable = true
  )
  private void itemPhysics$render(
      EntityItem itemEntity,
      double x, double y, double z,
      float yRot,
      float partialTicks,
      CallbackInfo ci
  ) {
    DefaultReferenceStorage storage = ItemPhysics.get().referenceStorage();
    ItemPhysicsRenderer renderer = storage.itemPhysicsRenderer();
    var bakedModel = this.itemRenderer.getItemModelMesher()
        .getItemModel(itemEntity.getEntityItem());
    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);

    boolean rendered = renderer.render(
        VersionedStackProvider.DEFAULT_STACK,
        null,
        (ItemEntity) itemEntity,
        itemEntity.getAge(),
        (ItemStack) (Object) itemEntity.getEntityItem(),
        (BakedModel) bakedModel,
        JavaRandom.of(this.field_177079_e),
        RenderEnvironmentContext.FULL_BRIGHT
    );
    GlStateManager.popMatrix();

    if (rendered) {
      this.shadowSize = 0;
      super.doRender(itemEntity, x, y, z, yRot, partialTicks);
      ci.cancel();
    }
  }
}
