package net.labymod.addons.itemphysics.v1_21_3.mixins.api;

import net.labymod.addons.itemphysics.bridge.BakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.client.resources.model.BakedModel.class)
public interface MixinBakedModel extends BakedModel {

  @Shadow
  boolean isGui3d();

  @Override
  default boolean itemPhysics$isGui3D() {
    return this.isGui3d();
  }
}
