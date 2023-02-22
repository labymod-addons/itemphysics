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

package net.labymod.addons.itemphysics.listener;

import net.labymod.addons.itemphysics.ItemPhysics;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.GameRenderEvent;

public class GameRenderListener {

  private final ItemPhysics itemPhysics;

  public GameRenderListener(ItemPhysics itemPhysics) {
    this.itemPhysics = itemPhysics;
  }

  @Subscribe
  public void onGameRender(GameRenderEvent event) {
    if (event.phase() != Phase.PRE) {
      return;
    }

    this.itemPhysics.updateLastRenderTime();
  }
}
