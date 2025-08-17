package me.aaaaadam.rat.listener;

import me.aaaaadam.rat.itemstack.EnchantBookUtil;
import org.bukkit.Material;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.mineacademy.fo.annotation.AutoRegister;

import java.util.Random;

@AutoRegister
public class VillagerTradeHandler implements Listener {

	private static final Random RANDOM = new Random();

	public VillagerTradeHandler() {
	}

	@EventHandler
	public void onAcquireTrade(VillagerAcquireTradeEvent e) {
		if (!(e.getEntity() instanceof Villager villager) || villager.getProfession() != Villager.Profession.LIBRARIAN) {
			return;
		}

		if (RANDOM.nextDouble() > 0.3) return;

		MerchantRecipe recipe = e.getRecipe();

		if (recipe.getResult().getType() != Material.ENCHANTED_BOOK) return;

		MerchantRecipe newRecipe = new MerchantRecipe(EnchantBookUtil.createReplanterBook(), 1, 5, true, 10, 0.2f);
		newRecipe.addIngredient(new ItemStack(Material.EMERALD, RANDOM.nextInt(11) + 10));
		e.setRecipe(newRecipe);
	}
}
