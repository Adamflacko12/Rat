package me.aaaaadam.rat.listener;

import me.aaaaadam.rat.command.BackCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class PlayerDeath implements Listener {
	private final BackCommand backCommand;

	public PlayerDeath() {
		this.backCommand = BackCommand.getInstance();
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		backCommand.setDeathLocation(player, player.getLocation());
		Common.tell(player, "&aYou can use /back to return to where you died <3");
	}
}