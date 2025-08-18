package me.aaaaadam.rat.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.enchant.NmsEnchant;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;

import java.lang.reflect.Field;

/**
 * Handle class that bridges Foundation's SimpleEnchantment with Bukkit's Enchantment system
 * This class extends Bukkit's Enchantment and implements Foundation's NmsEnchant interface
 */
public final class EnchantmentHandle extends Enchantment implements NmsEnchant {

	private final CustomEnchantment simpleEnchantment;
	private final NamespacedKey namespacedKey;
	private boolean registered = false;

	/**
	 * Constructor required by Foundation
	 * Foundation will automatically instantiate this with the SimpleEnchantment
	 *
	 * @param simpleEnchantment The Foundation SimpleEnchantment this handle wraps
	 */
	public EnchantmentHandle(CustomEnchantment simpleEnchantment) {
		// Call super() with no args for newer Bukkit versions
		super();
		this.simpleEnchantment = simpleEnchantment;
		this.namespacedKey = new NamespacedKey(SimplePlugin.getInstance(), simpleEnchantment.getNamespacedName());
	}

	@Override
	public void register() {
		if (registered) {
			return;
		}

		try {
			// Unfreeze the enchantment registry to allow new registrations
			Remain.unfreezeEnchantRegistry();

			registered = true;

		} catch (Exception e) {
			throw new RuntimeException("Failed to register enchantment: " + simpleEnchantment.getName(), e);
		}
	}

	@Override
	public Enchantment toBukkit() {
		return this;
	}

	// Required by newer Bukkit versions
	@Override
	public NamespacedKey getKey() {
		return namespacedKey;
	}

	@Override
	public String translationKey() {
		return "enchantment." + namespacedKey.getNamespace() + "." + namespacedKey.getKey();
	}

	// Delegate all Bukkit Enchantment methods to the SimpleEnchantment

	@Override
	public String getName() {
		return simpleEnchantment.getName();
	}

	@Override
	public int getMaxLevel() {
		return simpleEnchantment.getMaxLevel();
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		// Convert Foundation's target to Bukkit's target
		switch (simpleEnchantment.getTarget()) {
			case ARMOR:
				return EnchantmentTarget.ARMOR;
			case ARMOR_FEET:
				return EnchantmentTarget.ARMOR_FEET;
			case ARMOR_LEGS:
				return EnchantmentTarget.ARMOR_LEGS;
			case ARMOR_TORSO:
				return EnchantmentTarget.ARMOR_TORSO;
			case ARMOR_HEAD:
				return EnchantmentTarget.ARMOR_HEAD;
			case WEAPON:
				return EnchantmentTarget.WEAPON;
			case TOOL:
				return EnchantmentTarget.TOOL;
			case BOW:
				return EnchantmentTarget.BOW;
			case FISHING_ROD:
				return EnchantmentTarget.FISHING_ROD;
			case BREAKABLE:
			default:
				return EnchantmentTarget.BREAKABLE;
		}
	}

	@Override
	public boolean isTreasure() {
		return simpleEnchantment.isTreasure();
	}

	@Override
	public boolean isCursed() {
		return simpleEnchantment.isCursed();
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return simpleEnchantment.conflictsWith(other);
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return simpleEnchantment.canEnchantItem(item);
	}

	/**
	 * Check if this handle is registered
	 * @return true if registered with Minecraft
	 */
	public boolean isRegistered() {
		return registered;
	}

	/**
	 * Get the SimpleEnchantment this handle wraps
	 * @return the SimpleEnchantment instance
	 */
	public CustomEnchantment getSimpleEnchantment() {
		return simpleEnchantment;
	}
}