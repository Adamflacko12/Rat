package me.aaaaadam.rat.command;

import me.aaaaadam.rat.data.HomeData;
import me.aaaaadam.rat.gui.HomeGui;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class HomeCommand implements CommandExecutor, TabCompleter {

	private final JavaPlugin plugin;

	public HomeCommand(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Only players can use this command
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be used by players!");
			return true;
		}

		Player player = (Player) sender;
		HomeData homeData = HomeData.getInstance(plugin);

		// If no arguments, open the GUI
		if (args.length == 0) {
			HomeGui gui = new HomeGui(homeData, player);
			gui.displayTo(player);
			return true;
		}

		// Handle subcommands
		if (args.length > 0) {
			String subCommand = args[0].toLowerCase();

			switch (subCommand) {
				case "set":
					if (args.length < 2) {
						player.sendMessage(ChatColor.RED + "Usage: /home set <homeName>");
						return true;
					}
					String homeName = args[1];
					if (!homeName.matches("[a-zA-Z0-9_]+")) {
						player.sendMessage(ChatColor.RED + "Home name can only contain letters, numbers, and underscores!");
						return true;
					}
					homeData.setHome(player, homeName, player.getLocation());
					player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' set at your current location!");
					return true;

				case "delete":
				case "del":
					if (args.length < 2) {
						player.sendMessage(ChatColor.RED + "Usage: /home delete <homeName>");
						return true;
					}
					homeName = args[1];
					if (!homeData.hasHome(player, homeName)) {
						player.sendMessage(ChatColor.RED + "Home '" + homeName + "' does not exist!");
						return true;
					}
					homeData.deleteHome(player, homeName);
					player.sendMessage(ChatColor.GREEN + "Home '" + homeName + "' deleted!");
					return true;

				case "list":
					List<String> homeNames = homeData.getHomeNames(player);
					if (homeNames.isEmpty()) {
						player.sendMessage(ChatColor.RED + "You don't have any homes set!");
						return true;
					}
					player.sendMessage(ChatColor.GREEN + "Your homes: " + String.join(", ", homeNames));
					return true;

				case "gui":
				case "menu":
					// Alternative way to open GUI
					HomeGui gui = new HomeGui(homeData, player);
					gui.displayTo(player);
					return true;

				default:
					// Try to teleport to a home if the first argument is not a subcommand
					homeName = args[0];
					if (!homeData.hasHome(player, homeName)) {
						player.sendMessage(ChatColor.RED + "Home '" + homeName + "' not found! Use /home list to see your homes.");
						return true;
					}
					homeData.teleportHome(player, homeName);
					return true;
			}
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!(sender instanceof Player)) {
			return new ArrayList<>();
		}

		Player player = (Player) sender;
		HomeData homeData = HomeData.getInstance(plugin);

		if (args.length == 1) {
			List<String> completions = new ArrayList<>();
			String input = args[0].toLowerCase();
			List<String> subcommands = Arrays.asList("set", "delete", "list", "gui", "menu");

			// Suggest subcommands
			for (String subcommand : subcommands) {
				if (subcommand.startsWith(input)) {
					completions.add(subcommand);
				}
			}

			// Suggest home names for direct teleport
			for (String homeName : homeData.getHomeNames(player)) {
				if (homeName.toLowerCase().startsWith(input)) {
					completions.add(homeName);
				}
			}
			return completions;
		}

		if (args.length == 2 && (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del"))) {
			// Suggest home names for delete command
			List<String> completions = new ArrayList<>();
			String input = args[1].toLowerCase();
			for (String homeName : homeData.getHomeNames(player)) {
				if (homeName.toLowerCase().startsWith(input)) {
					completions.add(homeName);
				}
			}
			return completions;
		}

		return new ArrayList<>();
	}
}