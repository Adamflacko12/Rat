package me.aaaaadam.rat.listener;

import me.aaaaadam.rat.itemstack.TreeAxeItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@AutoRegister
public final class TreeAxeDrop implements Listener {

	private final Random random = new Random();
	private final List<ItemStack> dropItems;

	public TreeAxeDrop() {
		JavaPlugin plugin = JavaPlugin.getProvidingPlugin(TreeAxeDrop.class);
		TreeAxeItem.init(plugin);
		this.dropItems = Arrays.asList(TreeAxeItem.createTreeAxe());
	}

	@EventHandler
	public void onKill(EntityDeathEvent e) {
		if (random.nextDouble() < 0.001) {
			ItemStack drop = dropItems.get(random.nextInt(dropItems.size()));
			e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), drop.clone());
			e.getDrops().clear();
		}
	}

    /*
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (random.nextDouble() < 0.1) { // Example: 10% chance to drop
            ItemStack drop = dropItems.get(random.nextInt(dropItems.size()));
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), drop.clone());
            e.setDropItems(false);
        }
    */
}