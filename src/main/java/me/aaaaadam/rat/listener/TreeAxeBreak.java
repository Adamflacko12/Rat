package me.aaaaadam.rat.listener;

import me.aaaaadam.rat.itemstack.TreeAxeItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

@AutoRegister
public final class TreeAxeBreak implements Listener {

	private final Set<Material> logTypes = new HashSet<>(Arrays.asList(
			Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.JUNGLE_LOG,
			Material.ACACIA_LOG, Material.DARK_OAK_LOG, Material.MANGROVE_LOG, Material.CHERRY_LOG,
			Material.STRIPPED_OAK_LOG, Material.STRIPPED_BIRCH_LOG, Material.STRIPPED_SPRUCE_LOG,
			Material.STRIPPED_JUNGLE_LOG, Material.STRIPPED_ACACIA_LOG, Material.STRIPPED_DARK_OAK_LOG,
			Material.STRIPPED_MANGROVE_LOG, Material.STRIPPED_CHERRY_LOG
	));

	private final Set<Material> leafTypes = new HashSet<>(Arrays.asList(
			Material.OAK_LEAVES, Material.BIRCH_LEAVES, Material.SPRUCE_LEAVES, Material.JUNGLE_LEAVES,
			Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES, Material.MANGROVE_LEAVES, Material.CHERRY_LEAVES
	));

	private final int maxBlocks = 100;

	private final BlockFace[] adjacentFaces = {
			BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST,
			BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH_EAST,
			BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
	};

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e.isCancelled()) return;

		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		if (item == null || !isTreeAxe(item)) {
			return;
		}

		Block block = e.getBlock();
		if (!logTypes.contains(block.getType())) {
			return;
		}

		// Prevent the original block from dropping items and XP
		e.setDropItems(false);
		e.setExpToDrop(0);

		// Break the tree using iterative approach to prevent stack overflow
		breakTreeIterative(block);
	}

	private boolean isTreeAxe(ItemStack item) {
		if (item.hasItemMeta() && item.getItemMeta() != null) {
			return item.getItemMeta().getPersistentDataContainer()
					.has(TreeAxeItem.getTreeAxeKey(), PersistentDataType.BYTE);
		}
		return false;
	}

	private void breakTreeIterative(Block startBlock) {
		Set<Block> processed = new HashSet<>();
		Stack<Block> toProcess = new Stack<>();
		toProcess.push(startBlock);

		int blocksBroken = 0;

		while (!toProcess.isEmpty() && blocksBroken < maxBlocks) {
			Block current = toProcess.pop();

			if (processed.contains(current)) {
				continue;
			}

			processed.add(current);

			// Check if this block should be broken
			if (logTypes.contains(current.getType()) || leafTypes.contains(current.getType())) {
				current.breakNaturally();
				blocksBroken++;

				// Add adjacent blocks to process
				for (BlockFace face : adjacentFaces) {
					Block relative = current.getRelative(face);
					if (!processed.contains(relative) &&
							(logTypes.contains(relative.getType()) || leafTypes.contains(relative.getType()))) {
						toProcess.push(relative);
					}
				}
			}
		}
	}
}