package me.aaaaadam.rat.itemstack;

import me.aaaaadam.rat.enchants.ReplanterEnchantment;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class EnchantBookUtil {

	public static ItemStack createReplanterBook() {
		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
		if (meta != null) {
			Enchantment replanter = Enchantment.getByKey(new NamespacedKey("replanter_plugin", ReplanterEnchantment.KEY));
			if (replanter != null) {
				meta.addStoredEnchant(replanter, 1, true);
				book.setItemMeta(meta);
			}
		}
		return book;
	}
}
