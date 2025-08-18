package me.aaaaadam.rat.enchants;

import org.mineacademy.fo.enchant.SimpleEnchantment;

/**
 * Base class for all custom enchantments in the Rat plugin
 * Extends Foundation's SimpleEnchantment for proper integration
 */
public abstract class CustomEnchantment extends SimpleEnchantment {

	/**
	 * Constructor for custom enchantments
	 *
	 * @param name     The display name of the enchantment (e.g., "Speed Boost")
	 * @param maxLevel Maximum level for this enchantment
	 */
	protected CustomEnchantment(String name, int maxLevel) {
		super(name, maxLevel);
	}

	/**
	 * Get a description of what this enchantment does
	 * Used for tooltips or help information
	 *
	 * @return description of the enchantment
	 */
	public abstract String getDescription();

	/**
	 * Get the color code for this enchantment's lore
	 * Override this to customize the color of your enchantment
	 *
	 * @return color code (e.g., "&a" for green)
	 */
	protected String getLoreColor() {
		return "&7"; // Default gray color
	}

	@Override
	public String getLore(int level) {
		String lore = super.getLore(level);
		return lore != null ? getLoreColor() + lore : null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{name='" + getName() + "', maxLevel=" + getMaxLevel() + "}";
	}
}