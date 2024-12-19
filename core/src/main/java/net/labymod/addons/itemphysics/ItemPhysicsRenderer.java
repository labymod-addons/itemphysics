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

package net.labymod.addons.itemphysics;

import javax.inject.Singleton;
import net.labymod.addons.itemphysics.bridge.BakedModel;
import net.labymod.addons.itemphysics.bridge.EntityMotion;
import net.labymod.addons.itemphysics.bridge.ItemEntity;
import net.labymod.addons.itemphysics.bridge.RandomSource;
import net.labymod.addons.itemphysics.bridge.VersionBridge;
import net.labymod.addons.itemphysics.util.FloatOptional;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
@Singleton
public class ItemPhysicsRenderer {

  private static final long AIR_SEED = 187;
  private static final float HALF_PI = (float) Math.PI / 2.0F;
  private final VersionBridge bridge;

  public ItemPhysicsRenderer(VersionBridge bridge) {
    this.bridge = bridge;
  }

  public boolean render(
      Stack stack,
      Object bufferSource,
      ItemEntity itemEntity,
      float age,
      ItemStack itemStack,
      BakedModel model,
      RandomSource random,
      int packedLightCoords
  ) {
    if (age == 0) {
      return false;
    }

    stack.push();
    random.itemPhysics$setSeed(
        itemStack.isAir() ?
            AIR_SEED :
            this.bridge.getItemId(itemStack) + itemStack.getCurrentDamageValue()
    );

    int modelCount = this.getModelCount(itemStack);
    boolean gui3D = model.itemPhysics$isGui3D();

    float rotatedBy = ItemPhysics.getRotation() * ItemPhysics.get()
        .configuration()
        .rotationSpeed()
        .get();
    if (this.bridge.isGamePaused()) {
      rotatedBy = 0;
    }

    EntityMotion entityMotion = (EntityMotion) itemEntity;
    if (entityMotion.itemPhysics$hasStuckSpeedMultiplier() &&
        entityMotion.itemPhysics$lengthSqr() > 0) {
      rotatedBy *= (float) (entityMotion.itemPhysics$getStuckSpeedMultiplierX() * 0.2F);
    }

    stack.rotateRadians(HALF_PI, 1, 0, 0);
    stack.rotateRadians(itemEntity.itemPhysics$getYRot(), 0, 0, 1);

    boolean applyEffects = age != 0 && (gui3D || this.bridge.hasOptions());
    if (applyEffects) {
      this.applyRotations(
          stack,
          itemEntity,
          gui3D,
          rotatedBy
      );
    }

    if (!gui3D) {
      float offsetX = -0.0F * (modelCount - 1) * 0.5F;
      float offsetY = -0.0F * (modelCount - 1) * 0.5F;
      float offsetZ = -0.09375F * (modelCount - 1) * 0.5F;
      stack.translate(offsetX, offsetY, offsetZ);
    }

    for (int index = 0; index < modelCount; index++) {
      stack.push();
      if (index > 0) {
        if (gui3D) {
          float offsetX = (random.itemPhysics$nextFloat() * 2.0F - 1.0F) * 0.15F;
          float offsetY = (random.itemPhysics$nextFloat() * 2.0F - 1.0F) * 0.15F;
          float offsetZ = (random.itemPhysics$nextFloat() * 2.0F - 1.0F) * 0.15F;
          stack.translate(offsetX, offsetY, offsetZ);
        }
      }

      this.bridge.renderItem(itemStack, stack, bufferSource, packedLightCoords, model);

      stack.pop();

      if (!gui3D) {
        stack.translate(0.0F, 0.0F, 0.09375F);
      }
    }

    stack.pop();
    return true;
  }

  private void applyRotations(
      Stack stack,
      ItemEntity itemEntity,
      boolean gui3D,
      float rotatedBy
  ) {
    if (gui3D) {
      if (!itemEntity.itemPhysics$isOnGround()) {
        rotatedBy *= 2;

        FloatOptional fluidViscosity = this.bridge.getFluidViscosity(itemEntity, true);
        if (fluidViscosity.isSet()) {
          rotatedBy /= (1 + fluidViscosity.get());
        }

        itemEntity.itemPhysics$setXRot(itemEntity.itemPhysics$getXRot() + rotatedBy);
      }
    } else if (!Double.isNaN(itemEntity.itemPhysics$getPosX()) &&
        !Double.isNaN(itemEntity.itemPhysics$getPosY()) &&
        !Double.isNaN(itemEntity.itemPhysics$getPosZ())) {
      if (itemEntity.itemPhysics$isOnGround()) {
        itemEntity.itemPhysics$setXRot(0);
      } else {
        rotatedBy *= 2;

        FloatOptional fluidViscosity = this.bridge.getFluidViscosity(itemEntity, false);
        if (fluidViscosity.isSet()) {
          rotatedBy /= (1 + fluidViscosity.get());
        }

        itemEntity.itemPhysics$setXRot(itemEntity.itemPhysics$getXRot() + rotatedBy);
      }
    }

    if (gui3D) {
      stack.translate(0.0F, -0.2F, -0.08F);
    } else if (this.bridge.isSpecialBlock(itemEntity)) {
      stack.translate(0.0F, 0.0F, -0.14F);
    } else {
      stack.translate(0.0F, 0.0F, -0.04F);
    }

    float height = 0.2F;
    if (gui3D) {
      stack.translate(0.0F, height, 0.0F);
    }

    stack.rotateRadians(itemEntity.itemPhysics$getXRot(), 1, 0, 0);

    if (gui3D) {
      stack.translate(0.0F, -height, 0.0F);
    }
  }

  private int getModelCount(ItemStack itemStack) {
    int size = itemStack.getSize();
    if (size > 48) {
      return 5;
    } else if (size > 32) {
      return 4;
    } else if (size > 16) {
      return 3;
    } else if (size > 1) {
      return 2;
    } else {
      return 1;
    }
  }
}
