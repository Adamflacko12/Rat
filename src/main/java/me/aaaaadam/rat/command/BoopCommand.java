package me.aaaaadam.rat.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompToastStyle;
import org.mineacademy.fo.remain.Remain;

@AutoRegister
public final class BoopCommand extends SimpleCommand {

	public BoopCommand() {
		super("boop");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		Player sender = getPlayer();
		String targetName = args[0];

		Player target = Bukkit.getPlayer(targetName);

		if (target == null) {
			Common.tell(sender, "&cPlayer not found!");
			return;
		}

		if (target.equals(sender)) {
			Common.tell(sender, "&cI've changed the code to where now I'm booping you here :)");
			return;
		}

		// Send the boop message to the target player
		Remain.sendToast(target, "Boop", CompMaterial.SUNFLOWER, CompToastStyle.TASK);
	}
}