package me.aaaaadam.rat.listener;

import me.aaaaadam.rat.itemstack.TreeAxeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AutoRegister
public final class TreeAxeBreak implements Listener {

	private final JavaPlugin plugin;

	private final Set<Material> logTypes = new HashSet<>(Arrays.asList(
			Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.JUNGLE_LOG,
			Material.ACACIA_LOG, Material.DARK_OAK_LOG, Material.MANGROVE_LOG, Material.CHERRY_LOG
	));

	private final Set<Material> leafTypes = new HashSet<>(Arrays.asList(
			Material.OAK_LEAVES, Material.BIRCH_LEAVES, Material.SPRUCE_LEAVES, Material.JUNGLE_LEAVES,
			Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES, Material.MANGROVE_LEAVES, Material.CHERRY_LEAVES
	));

	private final int maxBlocks = 100;

	private int blocksBroken;

	public TreeAxeBreak() {
		this.plugin = JavaPlugin.getProvidingPlugin(TreeAxeItem.class);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		if (item == null || !isTreeAxe(item)) {
			return;
		}

		Block block = e.getBlock();
		if (!logTypes.contains(block.getType())) {
			return;
		}

		e.setDropItems(false);
		e.setExpToDrop(0);

		blocksBroken = 0;
		breakTree(block);
	}

	private boolean isTreeAxe(ItemStack item) {
		if (item.hasItemMeta() && item.getItemMeta() != null) {
			return item.getItemMeta().getPersistentDataContainer().has(TreeAxeItem.getTreeAxeKey(), PersistentDataType.BYTE);
		}
		return false;
	}

	private void breakTree(Block block) {
		if (blocksBroken >= maxBlocks || (!logTypes.contains(block.getType()) && !leafTypes.contains(block.getType()))) {
			return;
		}

		block.breakNaturally();
		blocksBroken++;

		for (BlockFace face : new BlockFace[] {
				BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,
				BlockFace.NORTH_NORTH_EAST, BlockFace.NORTH_NORTH_WEST, BlockFace.SOUTH_SOUTH_EAST, BlockFace.SOUTH_SOUTH_WEST
		}) {
			Block relative = block.getRelative(face);
			if (logTypes.contains(relative.getType()) || leafTypes.contains(relative.getType())) {
				breakTree(relative);
			}
		}

	}
}
