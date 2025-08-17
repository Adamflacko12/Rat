package me.aaaaadam.rat.gui;

import me.aaaaadam.rat.data.HomeData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompToastStyle;
import org.mineacademy.fo.remain.Remain;

import java.util.List;

public class HomeGui extends Menu {

	private final HomeData homeData;
	private List<String> homes;

	public HomeGui(HomeData homeData, Player player) {
		this.homeData = homeData;
		this.homes = homeData.getHomeNames(player);
		this.setTitle("&0Home Menu");
		this.setSize(9 * 3);
	}

	@Position(0)
	private final Button home0 = createHomeButton(0);

	@Position(1)
	private final Button home1 = createHomeButton(1);

	@Position(2)
	private final Button home2 = createHomeButton(2);

	@Position(3)
	private final Button home3 = createHomeButton(3);

	@Position(4)
	private final Button home4 = createHomeButton(4);

	@Position(5)
	private final Button home5 = createHomeButton(5);

	@Position(6)
	private final Button home6 = createHomeButton(6);

	@Position(7)
	private final Button home7 = createHomeButton(7);

	@Position(8)
	private final Button home8 = createHomeButton(8);

	@Position(9)
	private final Button home9 = createHomeButton(9);

	@Position(10)
	private final Button home10 = createHomeButton(10);

	@Position(11)
	private final Button home11 = createHomeButton(11);

	@Position(12)
	private final Button home12 = createHomeButton(12);

	@Position(13)
	private final Button home13 = createHomeButton(13);

	@Position(14)
	private final Button home14 = createHomeButton(14);

	@Position(15)
	private final Button home15 = createHomeButton(15);

	private Button createHomeButton(int index) {
		return new Button() {
			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {
				if (homes.size() > index) {
					String homeName = homes.get(index);
					homeData.teleportHome(player, homeName);
					player.closeInventory();
					Remain.sendToast(player, "You teleported to home!", CompMaterial.SUNFLOWER, CompToastStyle.TASK);
				}
			}

			@Override
			public ItemStack getItem() {
				if (homes.size() > index) {
					String homeName = homes.get(index);
					return ItemCreator.of(CompMaterial.BLUE_BED)
							.name("&e" + homeName)
							.make();
				}
				return null; // No button if no home at this index
			}
		};
	}
}