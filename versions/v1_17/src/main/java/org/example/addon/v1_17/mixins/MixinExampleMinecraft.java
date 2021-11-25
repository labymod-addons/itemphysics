package org.example.addon.v1_17.mixins;

import net.minecraft.client.Minecraft;
import org.example.addon.v1_17.ExampleMinecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinExampleMinecraft implements ExampleMinecraft {

  @Inject(method = "runTick", at = @At("HEAD"))
  private void example$helloWorld(boolean f, CallbackInfo ci) {
    System.out.println("Hello, World!");
    this.weAreIn();
  }

  @Override
  public void weAreIn() {
    System.out.println("Poggers");
  }
}
