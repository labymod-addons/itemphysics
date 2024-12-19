package net.labymod.addons.itemphysics.bridge;

import net.labymod.addons.itemphysics.util.FloatOptional;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface VersionBridge {

  int getItemId(ItemStack itemStack);

  boolean isGamePaused();

  void renderItem(
      ItemStack itemStack,
      Stack stack,
      Object bufferSource,
      int packedLightCoords,
      BakedModel bakedModel
  );

  boolean isSpecialBlock(ItemEntity entity);

  boolean hasOptions();

  FloatOptional getFluidViscosity(ItemEntity entity, boolean withBelow);
}
