package net.labymod.addons.itemphysic.core.configuration;

import com.google.inject.Singleton;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.impl.AddonConfig;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
@ConfigName("settings")
@Singleton
public class DefaultItemPhysicConfiguration extends AddonConfig {

  @SwitchSetting
  private boolean enabled = true;

  @SliderSetting(min = 1, max = 3)
  private float rotationSpeed = 1;

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  public float getRotationSpeed() {
    return this.rotationSpeed;
  }
}
