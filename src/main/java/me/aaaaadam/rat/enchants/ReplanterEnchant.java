package me.aaaaadam.rat.enchants;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.enchant.SimpleEnchantment;

import java.util.HashMap;
import java.util.Map;

public class ReplanterEnchant extends SimpleEnchantment {

	// Map crop materials to their seed equivalents
	private static final Map<Material, Material> CROP_TO_SEED = new HashMap<>();

	static {
		CROP_TO_SEED.put(Material.WHEAT, Material.WHEAT_SEEDS);
		CROP_TO_SEED.put(Material.CARROTS, Material.CARROT);
		CROP_TO_SEED.put(Material.POTATOES, Material.POTATO);
		CROP_TO_SEED.put(Material.BEETROOTS, Material.BEETROOT_SEEDS);
		CROP_TO_SEED.put(Material.NETHER_WART, Material.NETHER_WART);
		CROP_TO_SEED.put(Material.MELON_STEM, Material.MELON_SEEDS);
		CROP_TO_SEED.put(Material.PUMPKIN_STEM, Material.PUMPKIN_SEEDS);
	}

	@Getter
	private static final ReplanterEnchant instance = new ReplanterEnchant();

	private ReplanterEnchant() {
		super("Replanter", 1);
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		// If event is already cancelled, don't process
		if (event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material blockType = block.getType();

		// Check if player has this enchantment on their tool
		ItemStack tool = player.getInventory().getItemInMainHand();
		if (!tool.getEnchantments().containsKey(this.hasEnchant(tool))) {
			return;
		}

		// Check if this is a valid crop and get its seed
		Material seedMaterial = CROP_TO_SEED.get(blockType);
		if (seedMaterial == null) {
			return;
		}

		// Check if crop is fully grown
		if (!(block.getBlockData() instanceof Ageable)) {
			return;
		}

		Ageable ageable = (Ageable) block.getBlockData();
		if (ageable.getAge() != ageable.getMaximumAge()) {
			return; // Not fully grown
		}

		// Check if player has seeds
		if (!player.getInventory().contains(seedMaterial)) {
			return;
		}

		// Remove one seed from inventory
		ItemStack seedStack = new ItemStack(seedMaterial, 1);
		player.getInventory().removeItem(seedStack);

		// Replant the crop (set it back to age 0)
		ageable.setAge(0);
		block.setBlockData(ageable);
	}
}