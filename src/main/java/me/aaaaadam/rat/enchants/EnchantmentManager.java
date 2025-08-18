package me.aaaaadam.rat.enchants;

import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.enchant.SimpleEnchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all custom enchantments for the Rat plugin
 * Handles registration, retrieval, and management of enchantments using Foundation's system
 */
public final class EnchantmentManager {

	private static EnchantmentManager instance;
	private final JavaPlugin plugin;
	private final Map<String, CustomEnchantment> enchantments = new HashMap<>();
	private boolean initialized = false;

	private EnchantmentManager(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Get the singleton instance of EnchantmentManager
	 *
	 * @param plugin The plugin instance
	 * @return EnchantmentManager instance
	 */
	public static EnchantmentManager getInstance(JavaPlugin plugin) {
		if (instance == null) {
			synchronized (EnchantmentManager.class) {
				if (instance == null) {
					instance = new EnchantmentManager(plugin);
				}
			}
		}
		return instance;
	}

	/**
	 * Initialize and register all custom enchantments
	 * This should be called during plugin startup
	 */
	public void initialize() {
		if (initialized) {
			Common.log("EnchantmentManager already initialized!");
			return;
		}

		Common.log("Initializing Foundation custom enchantments...");

		try {
			// First, register our enchantment handle class with Foundation
			// This tells Foundation what class to use to bridge SimpleEnchantments with Bukkit
			SimpleEnchantment.registerEnchantmentHandle(EnchantmentHandle.class);
			Common.log("Registered EnchantmentHandle with Foundation");

			// Now register all our custom enchantments
			// Foundation will automatically create EnchantmentHandle instances for each
			//registerEnchantment(new SpeedEnchantment());

			initialized = true;
			Common.log("Successfully registered " + enchantments.size() + " custom enchantments with Foundation!");

		} catch (Exception e) {
			Common.error(e, "Failed to initialize custom enchantments!");
		}
	}

	/**
	 * Register a custom enchantment
	 * Foundation automatically handles the registration process
	 *
	 * @param enchantment The enchantment to register
	 */
	private void registerEnchantment(CustomEnchantment enchantment) {
		try {
			// Store in our local map for easy access
			enchantments.put(enchantment.getName().toLowerCase(), enchantment);
			enchantments.put(enchantment.getNamespacedName().toLowerCase(), enchantment);

			Common.log("Registered enchantment: " + enchantment.getName() +
					" (Available: " + enchantment.isAvailable() + ")");

		} catch (Exception e) {
			Common.error(e, "Failed to register enchantment: " + enchantment.getName());
		}
	}

	/**
	 * Get an enchantment by name or namespaced name
	 *
	 * @param name The enchantment name
	 * @return The enchantment, or null if not found
	 */
	public CustomEnchantment getEnchantment(String name) {
		return enchantments.get(name.toLowerCase());
	}

	/**
	 * Get all registered custom enchantments
	 *
	 * @return List of all enchantments
	 */
	public List<CustomEnchantment> getAllEnchantments() {
		return new ArrayList<>(enchantments.values());
	}

	/**
	 * Get all available (properly registered) enchantments
	 *
	 * @return List of available enchantments
	 */
	public List<CustomEnchantment> getAvailableEnchantments() {
		List<CustomEnchantment> available = new ArrayList<>();
		for (CustomEnchantment enchantment : enchantments.values()) {
			if (enchantment.isAvailable()) {
				available.add(enchantment);
			}
		}
		return available;
	}

	/**
	 * Check if an enchantment is registered
	 *
	 * @param name The enchantment name
	 * @return true if registered
	 */
	public boolean isRegistered(String name) {
		return enchantments.containsKey(name.toLowerCase());
	}

	/**
	 * Shutdown the enchantment manager
	 * Called when the plugin is being disabled
	 */
	public void shutdown() {
		try {
			// Foundation handles cleanup automatically
			enchantments.clear();
			initialized = false;

			Common.log("EnchantmentManager shutdown complete.");

		} catch (Exception e) {
			Common.error(e, "Error during EnchantmentManager shutdown!");
		}
	}

	/**
	 * Check if the manager is initialized
	 *
	 * @return true if initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}
}