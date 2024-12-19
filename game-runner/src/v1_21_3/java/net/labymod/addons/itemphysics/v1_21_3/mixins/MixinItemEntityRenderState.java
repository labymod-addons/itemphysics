package net.labymod.addons.itemphysics.v1_21_3.mixins;

import net.labymod.addons.itemphysics.v1_21_3.client.ItemEntityRenderStateAccessor;
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
