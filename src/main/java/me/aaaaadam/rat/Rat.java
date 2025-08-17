package me.aaaaadam.rat;

import me.aaaaadam.rat.command.HomeCommand;
import me.aaaaadam.rat.data.HomeData;
import me.aaaaadam.rat.enchants.EnchantmentRegistrar;
import me.aaaaadam.rat.enchants.ReplanterEnchantment;
import me.aaaaadam.rat.itemstack.TreeAxeItem;
import me.aaaaadam.rat.listener.ReplanterListener;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.mineacademy.fo.plugin.SimplePlugin;

/**
 * PluginTemplate is a simple template you can use every time you make
 * a new plugin. This will save you time because you no longer have to
 * recreate the same skeleton and features each time.
 *
 * It uses Foundation for fast and efficient development process.
 */
public final class Rat extends SimplePlugin {

	private HomeData homeData;

	/**
	 * Automatically perform login ONCE when the plugin starts.
	 */
	@Override
	protected void onPluginStart() {
		// Initialize HomeStorage
		homeData = homeData.getInstance(this);
		homeData.loadHomes();

		TreeAxeItem.init(this);
		EnchantmentRegistrar.registerCustomEnchantments();

		Bukkit.getPluginManager().registerEvents(new ReplanterListener(Enchantment.getByKey(new NamespacedKey("replanter_plugin", ReplanterEnchantment.KEY))), this);

		// Register the home command
		HomeCommand homeCommand = new HomeCommand(this);
		getCommand("home").setExecutor(homeCommand);
		getCommand("home").setTabCompleter(homeCommand);
	}

	/**
	 * Automatically perform login when the plugin starts and each time it is reloaded.
	 */
	@Override
	protected void onReloadablesStart() {

		// You can check for necessary plugins and disable loading if they are missing
		//Valid.checkBoolean(HookManager.isVaultLoaded(), "You need to install Vault so that we can work with packets, offline player data, prefixes and groups.");

		// Uncomment to load variables
		// Variable.loadVariables();

		//
		// Add your own plugin parts to load automatically here
		// Please see @AutoRegister for parts you do not have to register manually
		//
	}

	@Override
	protected void onPluginPreReload() {

		// Close your database here if you use one
		//YourDatabase.getInstance().close();
		// Save homes before shutdown
		if (homeData != null) {
			homeData.shutdown();
		}

		getLogger().info("Core has stopped");
	}

	/* ------------------------------------------------------------------------------- */
	/* Events */
	/* ------------------------------------------------------------------------------- */

	/**
	 * An example event that checks if the right clicked entity is a cow, and makes an explosion.
	 * You can write your events to your main class without having to register a listener.
	 *
	 * @param event
	 */

	/* ------------------------------------------------------------------------------- */
	/* Static */
	/* ------------------------------------------------------------------------------- */

	/**
	 * Return the instance of this plugin, which simply refers to a static
	 * field already created for you in SimplePlugin but casts it to your
	 * specific plugin instance for your convenience.
	 *
	 * @return
	 */
	}