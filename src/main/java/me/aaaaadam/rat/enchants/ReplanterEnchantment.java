package me.aaaaadam.rat.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class ReplanterEnchantment extends Enchantment {

	public static final String KEY = "replanter";

	public ReplanterEnchantment(NamespacedKey key) {
		super(key);
	}

	@Override
	public String getName() {
		return "Replanter";
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.TOOL;
	}

	@Override
	public boolean isTreasure() {
		return false; // Not treasure-only
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false; // No conflicts for simplicity
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return item.getType().name().endsWith("_HOE"); // Only hoes
	}
}
