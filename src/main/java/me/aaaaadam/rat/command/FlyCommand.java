package me.aaaaadam.rat.command;

import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompToastStyle;
import org.mineacademy.fo.remain.Remain;

@AutoRegister
public final class FlyCommand extends SimpleCommand {

	public FlyCommand() {
		super("fly");
	}

	@Override
	protected void onCommand() {
		checkConsole();
		Player player = getPlayer();

		if (player.isFlying()) {
			player.setAllowFlight(false);
			player.setFlying(false);
			Remain.sendToast(player, "You've stopped flying!", CompMaterial.SUNFLOWER, CompToastStyle.TASK);
		} else {

			player.setAllowFlight(true);
			player.setFlying(true);
			Remain.sendToast(player, "You're flying!", CompMaterial.SUNFLOWER, CompToastStyle.TASK);
		}
	}
}
