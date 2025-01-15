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

package net.labymod.addons.itemphysics.util;

public class FloatOptional {

  private final float value;
  private final boolean set;

  private FloatOptional(float value, boolean set) {
    this.value = value;
    this.set = set;
  }

  public static FloatOptional of(float value) {
    return new FloatOptional(value, true);
  }

  public static FloatOptional empty() {
    return new FloatOptional(0.0F, false);
  }

  public boolean isSet() {
    return this.set;
  }

  public float get() {
    return this.value;
  }

}
