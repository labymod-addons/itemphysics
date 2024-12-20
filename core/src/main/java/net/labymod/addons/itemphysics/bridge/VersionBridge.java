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

package net.labymod.addons.itemphysics.bridge;

import net.labymod.addons.itemphysics.util.FloatOptional;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.math.vector.FloatVector3;

@Referenceable
public interface VersionBridge {

  FloatVector3 DEFAULT_GROUND_SCALE = new FloatVector3(1.0F, 1.0F, 1.0F);

  int getItemId(ItemStack itemStack);

  boolean isGamePaused();

  default void preRenderItem(ItemEntity entity, boolean renderOutlines) {

  }

  void renderItem(
      ItemStack itemStack,
      Stack stack,
      Object bufferSource,
      int packedLightCoords,
      BakedModel bakedModel
  );

  default void postRenderItem(ItemEntity entity, boolean renderOutlines) {

  }

  boolean isSpecialBlock(ItemEntity entity);

  boolean hasOptions();

  FloatOptional getFluidViscosity(ItemEntity entity, boolean withBelow);

  default FloatVector3 getGroundScale(BakedModel model) {
    return DEFAULT_GROUND_SCALE;
  }
}
