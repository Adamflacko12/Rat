package me.aaaaadam.rat.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;

public class EnchantmentRegistrar {

	public static void registerCustomEnchantments() {
		try {
			// Bypass the registration lock
			Field acceptingField = Enchantment.class.getDeclaredField("acceptingNew");
			acceptingField.setAccessible(true);
			acceptingField.set(null, true);

			// Register the enchantment
			NamespacedKey key = new NamespacedKey("replanter_plugin", ReplanterEnchantment.KEY);
			Enchantment.registerEnchantment(new ReplanterEnchantment(key));
		} catch (Exception e) {
			e.printStackTrace();
			// Handle error: plugin might not load if registration fails
		}
	}
}