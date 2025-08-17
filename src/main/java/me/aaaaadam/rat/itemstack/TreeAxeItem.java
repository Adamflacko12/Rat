package me.aaaaadam.rat.itemstack;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class TreeAxeItem {
	private static NamespacedKey treeAxeKey;

	public static void init(JavaPlugin plugin) {
		treeAxeKey = new NamespacedKey(plugin, "treeAxeKey");
	}

	public static ItemStack createTreeAxe() {

		ItemStack treeAxe = new ItemStack(Material.NETHERITE_AXE);
		ItemMeta meta = treeAxe.getItemMeta();


		if (meta instanceof Damageable) {
			((Damageable) meta).setDamage(1400);
		}

		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5Lucky Axe"));
		meta.setLore(Arrays.asList("Rare Drop!"));
		meta.getPersistentDataContainer().set(treeAxeKey, PersistentDataType.BYTE, (byte) 1);
		meta.addEnchant(Enchantment.UNBREAKING, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		treeAxe.setItemMeta(meta);

		return treeAxe;
	}

	public static NamespacedKey getTreeAxeKey() {
		return treeAxeKey;
	}
}

