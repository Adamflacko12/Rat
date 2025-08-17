package me.aaaaadam.rat.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.HashMap;
import java.util.UUID;

@AutoRegister
public final class BackCommand extends SimpleCommand {
	private static final BackCommand instance = new BackCommand();
	private final HashMap<UUID, Location> deathLocations = new HashMap<>();

	// Private no-arguments constructor for singleton
	private BackCommand() {
		super("back");
	}

	// Public getter for the singleton instance
	public static BackCommand getInstance() {
		return instance;
	}

	@Override
	protected void onCommand() {
		checkConsole();
		Player player = getPlayer();
		UUID playerId = player.getUniqueId();

		if (!deathLocations.containsKey(playerId)) {
			tellError("No death location found!");
			return;
		}

		Location deathLoc = deathLocations.get(playerId);
		player.teleport(deathLoc);
		tellSuccess(ChatColor.GREEN + "&aYou have been sent to your last death location!");
	}

	public void setDeathLocation(Player player, Location location) {
		deathLocations.put(player.getUniqueId(), location);
	}
}