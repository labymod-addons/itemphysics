package net.labymod.addons.itemphysic.core;

import com.google.inject.Singleton;
import net.labymod.addons.itemphysic.core.configuration.DefaultItemPhysicConfiguration;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.configuration.loader.impl.JsonConfigLoader;
import net.labymod.api.configuration.settings.type.SettingRegistry;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.addon.lifecycle.AddonPostEnableEvent;
import net.labymod.api.event.labymod.config.ConfigurationSaveEvent;
import net.labymod.api.models.addon.annotation.AddonListener;

@AddonListener
@Singleton
public class ItemPhysicAddon {

	private DefaultItemPhysicConfiguration itemPhysicConfiguration;

	public ItemPhysicAddon() {
	}

	@Subscribe
	public void onConfig(AddonPostEnableEvent event) {
		// Load config
		try {
			JsonConfigLoader loader = JsonConfigLoader.createDefault();
			this.itemPhysicConfiguration = loader.load(DefaultItemPhysicConfiguration.class);

			// Create registry
			SettingRegistry registry = SettingRegistry.namespace(Laby.getLabyAPI(), this);
			registry.addSettings(this.itemPhysicConfiguration);
			Laby.getLabyAPI().getCoreSettingRegistry().addSetting(registry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DefaultItemPhysicConfiguration getItemPhysicConfiguration() {
		return this.itemPhysicConfiguration;
	}
}
