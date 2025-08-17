package me.aaaaadam.rat.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class ReplanterListener implements Listener {

	private final Enchantment replanter;

	public ReplanterListener(Enchantment replanter) {
		this.replanter = replanter;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.isCancelled()) return;

		Player player = e.getPlayer();
		ItemStack tool = player.getInventory().getItemInMainHand();
		if (tool == null || !tool.containsEnchantment(replanter)) return;

		Block block = e.getBlock();
		if (!(block.getBlockData() instanceof Ageable ageable) || ageable.getAge() != ageable.getMaximumAge()) {
			return; // not a fully grown crop
		}

		Material cropType = block.getType();
		Material seedType = getSeedForCrop(cropType);
		if (seedType == null) return;

		// normal drops and check for inventory for a seed to replant

		PlayerInventory inv = player.getInventory();
		if (inv.contains(seedType)) {
			// replant here
			block.setType(cropType);
			Ageable newAgeable = (Ageable) block.getBlockData();
			newAgeable.setAge(0);
			block.setBlockData(newAgeable);

			// take one seed
			inv.removeItem(new ItemStack(seedType, 1));
		}
	}

	private Material getSeedForCrop(Material crop) {
		return switch (crop) {
			case WHEAT -> Material.WHEAT_SEEDS;
			case CARROTS -> Material.CARROT;
			case POTATOES -> Material.POTATO;
			case BEETROOTS -> Material.BEETROOT_SEEDS;
			// Add more crops like nether wart, etc., as needed
			default -> null;
		};
	}
}
