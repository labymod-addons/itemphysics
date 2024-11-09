package net.labymod.addons.itemphysics.v1_21_3.client;

import net.minecraft.world.entity.item.ItemEntity;

public interface VersionedItemEntityRenderStateAccessor {

  ItemEntity getItemEntity();

  void setItemEntity(ItemEntity itemEntity);
}
