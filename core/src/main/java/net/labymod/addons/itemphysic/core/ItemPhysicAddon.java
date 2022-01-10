package net.labymod.addons.itemphysic.core;

import com.google.inject.Singleton;
import net.labymod.addons.itemphysic.core.configuration.DefaultItemPhysicConfiguration;
import net.labymod.api.Laby;
import net.labymod.api.configuration.loader.ConfigurationLoader;
import net.labymod.api.configuration.settings.SettingsRegistry;
import net.labymod.api.configuration.settings.gui.SettingCategoryRegistry;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameInitializeEvent;
import net.labymod.api.event.client.lifecycle.GameInitializeEvent.Lifecycle;
import net.labymod.api.event.labymod.config.ConfigurationSaveEvent;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
@Singleton
public class ItemPhysicAddon {

  private DefaultItemPhysicConfiguration itemPhysicConfiguration;

  public ItemPhysicAddon() {
  }

  @Subscribe
  public void onConfig(GameInitializeEvent event) {
    if (event.getLifecycle() != Lifecycle.POST_STARTUP) {
      return;
    }

    // Load config
    try {
      ConfigurationLoader loader = Laby.getLabyAPI().getConfigurationLoader();

      System.out.println("load config...");
      this.itemPhysicConfiguration = loader.load(DefaultItemPhysicConfiguration.class);

      // Create registry
      SettingsRegistry registry = this.itemPhysicConfiguration.initializeRegistry();

      SettingCategoryRegistry categoryRegistry = Laby.getInjected(SettingCategoryRegistry.class);
      // Register configuration categories
      categoryRegistry.register(registry.getId(), registry);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Subscribe
  public void onConfigurationSave(ConfigurationSaveEvent event) {
    try {
      event.getLoader().save(this.itemPhysicConfiguration);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public DefaultItemPhysicConfiguration getItemPhysicConfiguration() {
    return this.itemPhysicConfiguration;
  }
}
