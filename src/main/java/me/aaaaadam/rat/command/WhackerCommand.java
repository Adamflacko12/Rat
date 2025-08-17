package me.aaaaadam.rat.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Arrays;

@AutoRegister
public final class WhackerCommand extends SimpleCommand {

	public WhackerCommand() {
		super("whacker");

	}

	protected void onCommand() {
		checkConsole();
		Player player = getPlayer();

		ItemStack stick = new ItemStack(Material.STICK);
		ItemMeta meta = stick.getItemMeta();

		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bWhacker"));
		meta.setLore(Arrays.asList("", "Troll Item"));

		stick.setItemMeta(meta);
		stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 255);

		player.give(stick);

	}

}
