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

package net.labymod.addons.itemphysics.v1_20_1.mixins.bridge;

import net.labymod.addons.itemphysics.bridge.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.util.RandomSource.class)
public interface MixinRandomSource extends RandomSource {

  @Shadow
  void setSeed(long var1);

  @Shadow
  float nextFloat();

  @Override
  default void itemPhysics$setSeed(long seed) {
    this.setSeed(seed);
  }

  @Override
  default float itemPhysics$nextFloat() {
    return this.nextFloat();
  }
}
