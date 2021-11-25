package org.example.core;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.jetbrains.annotations.NotNull;

@AddonMain
public class ExampleAddon {

  public ExampleAddon() {
  }

  @Subscribe
  public void onTick(@NotNull GameTickEvent event) {
    System.out.println("Hello, Minecraft Tick (" + event.getPhase().name() + ")");
  }

}
