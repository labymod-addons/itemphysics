package net.labymod.addons.itemphysic.core.configuration;

import com.google.inject.Singleton;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ConfigName;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
@ConfigName("settings")
@Singleton
public class DefaultItemPhysicConfiguration extends Config {

	@SwitchSetting
	private boolean enabled = true;

	@SliderSetting(min = 1, max = 3)
	private float rotationSpeed = 1;

	public boolean isEnabled() {
		return this.enabled;
	}

	public float getRotationSpeed() {
		return this.rotationSpeed;
	}
}
