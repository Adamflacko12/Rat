package me.aaaaadam.rat;

import me.aaaaadam.rat.command.HomeCommand;
import me.aaaaadam.rat.data.HomeData;
import me.aaaaadam.rat.itemstack.TreeAxeItem;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompSound;

/**
 * Rat plugin main class with various utility commands and features
 * Uses Foundation for fast and efficient development process.
 */
public final class Rat extends SimplePlugin {

	private HomeData homeData;
	private static Rat instance;

	/**
	 * Automatically perform setup when the plugin starts.
	 */
	@Override
	protected void onPluginStart() {
		instance = this;

		// Initialize HomeData
		homeData = HomeData.getInstance(this);
		homeData.loadHomes();

		// Initialize TreeAxeItem
		TreeAxeItem.init(this);

		// Register the home command
		HomeCommand homeCommand = new HomeCommand(this);
		if (getCommand("home") != null) {
			getCommand("home").setExecutor(homeCommand);
			getCommand("home").setTabCompleter(homeCommand);
		}

		getLogger().info("Rat plugin has started successfully!");
	}

	/**
	 * Automatically perform setup when the plugin starts and each time it is reloaded.
	 */
	@Override
	protected void onReloadablesStart() {
		// Add any reloadable components here
	}

	@Override
	protected void onPluginPreReload() {
		// Save homes before shutdown
		if (homeData != null) {
			homeData.shutdown();
		}
		getLogger().info("Rat plugin has stopped");
	}

	/**
	 * Get the plugin instance
	 * @return plugin instance
	 */
	public static Rat getInstance() {
		return instance;
	}

	/**
	 * Get the HomeData instance
	 * @return HomeData instance
	 */
	public HomeData getHomeData() {
		return homeData;
	}
}