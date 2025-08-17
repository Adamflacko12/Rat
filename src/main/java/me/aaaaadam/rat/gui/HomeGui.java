package me.aaaaadam.rat.gui;

import me.aaaaadam.rat.data.HomeData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class HomeGui extends Menu {

	private final HomeData homeData;

	@Position(10)
	private final Button homeButton;


	public HomeGui(HomeData homeData) {
		this.homeData = homeData;
		this.setTitle("&9Home Menu");
		this.setSize(9 * 3);

		this.homeButton = new Button() {

			@Override
			public void onClickedInMenu(Player player, Menu menu, ClickType click) {

			}

			@Override
			public ItemStack getItem() {
				return null;
			}

			private boolean isHome() {
				return getViewer().get(homeData.getHome(homeData.getHomeNames()));
			}
		};
	}

}
