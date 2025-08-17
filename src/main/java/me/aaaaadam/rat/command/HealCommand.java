package me.aaaaadam.rat.command;

import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompToastStyle;
import org.mineacademy.fo.remain.Remain;

@AutoRegister
public final class HealCommand extends SimpleCommand {

	public HealCommand() {
		super("heal");
	}

	@Override
	protected void onCommand() {
		checkConsole();

		Player player = getPlayer();
		player.setHealth(20);
		player.setFoodLevel(20);
		Remain.sendToast(player, "You've been healed!", CompMaterial.SUNFLOWER, CompToastStyle.TASK);
	}
}
