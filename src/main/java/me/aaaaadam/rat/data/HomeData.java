package me.aaaaadam.rat.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class HomeData {

	private static HomeData instance;
	private final JavaPlugin plugin;
	private final String databasePath;
	private final Map<UUID, Map<String, Location>> homes = new ConcurrentHashMap<>();
	private HikariDataSource dataSource;

	private HomeData(JavaPlugin plugin) {
		this.plugin = plugin;
		this.databasePath = plugin.getDataFolder() + File.separator + "homes.db";
		plugin.getDataFolder().mkdirs();
		setupDataSource();
		initializeDatabase();
	}

	public static HomeData getInstance(JavaPlugin plugin) {
		if (instance == null) {
			synchronized (HomeData.class) {
				if (instance == null) {
					instance = new HomeData(plugin);
				}
			}
		}
		return instance;
	}

	private void setupDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:sqlite:" + databasePath);
		config.setConnectionTestQuery("SELECT 1");
		config.setPoolName("HomeDataPool");
		dataSource = new HikariDataSource(config);
	}

	private Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	private void initializeDatabase() {
		try (Connection connection = getConnection()) {
			String createTable = "CREATE TABLE IF NOT EXISTS homes (" +
					"uuid TEXT NOT NULL, " +
					"home_name TEXT NOT NULL, " +
					"world TEXT NOT NULL, " +
					"x DOUBLE NOT NULL, " +
					"y DOUBLE NOT NULL, " +
					"z DOUBLE NOT NULL, " +
					"yaw FLOAT NOT NULL, " +
					"pitch FLOAT NOT NULL, " +
					"PRIMARY KEY (uuid, home_name))";
			try (PreparedStatement statement = connection.prepareStatement(createTable)) {
				statement.execute();
			}
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to initialize database: " + e.getMessage(), e);
		}
	}

	public void loadHomes() {
		try (Connection connection = getConnection();
			 PreparedStatement statement = connection.prepareStatement("SELECT * FROM homes");
			 ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {
				try {
					UUID playerUUID = UUID.fromString(resultSet.getString("uuid"));
					String homeName = resultSet.getString("home_name");
					String worldName = resultSet.getString("world");
					double x = resultSet.getDouble("x");
					double y = resultSet.getDouble("y");
					double z = resultSet.getDouble("z");
					float yaw = resultSet.getFloat("yaw");
					float pitch = resultSet.getFloat("pitch");

					Location location = new Location(
							Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
					if (location.getWorld() != null) {
						homes.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>())
								.put(homeName, location);
					} else {
						plugin.getLogger().warning("Removing invalid home '" + homeName + "' for UUID: " + playerUUID +
								" (World '" + worldName + "' not found)");
						try (PreparedStatement deleteStmt = connection.prepareStatement(
								"DELETE FROM homes WHERE uuid = ? AND home_name = ?")) {
							deleteStmt.setString(1, playerUUID.toString());
							deleteStmt.setString(2, homeName);
							deleteStmt.execute();
						}
					}
				} catch (Exception e) {
					plugin.getLogger().warning("Failed to load home '" + resultSet.getString("home_name") +
							"' for UUID: " + resultSet.getString("uuid") + ": " + e.getMessage());
				}
			}
			plugin.getLogger().info("Loaded homes for " + homes.size() + " players from SQLite.");
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to load homes: " + e.getMessage(), e);
		}
	}

	public void saveHomes() {
		try (Connection connection = getConnection()) {
			connection.setAutoCommit(false);
			try (PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM homes");
				 PreparedStatement insertStmt = connection.prepareStatement(
						 "INSERT INTO homes (uuid, home_name, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

				deleteStmt.execute(); // Clear existing data

				for (Map.Entry<UUID, Map<String, Location>> playerEntry : homes.entrySet()) {
					UUID playerUUID = playerEntry.getKey();
					for (Map.Entry<String, Location> homeEntry : playerEntry.getValue().entrySet()) {
						String homeName = homeEntry.getKey();
						Location loc = homeEntry.getValue();
						insertStmt.setString(1, playerUUID.toString());
						insertStmt.setString(2, homeName);
						insertStmt.setString(3, loc.getWorld().getName());
						insertStmt.setDouble(4, loc.getX());
						insertStmt.setDouble(5, loc.getY());
						insertStmt.setDouble(6, loc.getZ());
						insertStmt.setFloat(7, loc.getYaw());
						insertStmt.setFloat(8, loc.getPitch());
						insertStmt.addBatch();
					}
				}
				insertStmt.executeBatch();
				connection.commit();
				plugin.getLogger().info("Saved homes for " + homes.size() + " players to SQLite.");
			} catch (SQLException e) {
				connection.rollback();
				plugin.getLogger().log(Level.SEVERE, "Failed to save homes: " + e.getMessage(), e);
			}
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Database error: " + e.getMessage(), e);
		}
	}

	public boolean hasHome(Player player, String homeName) {
		Map<String, Location> playerHomes = homes.get(player.getUniqueId());
		return playerHomes != null && playerHomes.containsKey(homeName);
	}

	public void setHome(Player player, String homeName, Location location) {
		if (location.getWorld() == null) {
			plugin.getLogger().warning("Cannot set home '" + homeName + "' for player " + player.getName() + ": Invalid world");
			return;
		}
		homes.computeIfAbsent(player.getUniqueId(), k -> new ConcurrentHashMap<>())
				.put(homeName, location.clone());
		runAsync(this::saveHomes);
	}

	public Location getHome(Player player, String homeName) {
		Map<String, Location> playerHomes = homes.get(player.getUniqueId());
		if (playerHomes == null) {
			return null;
		}
		Location home = playerHomes.get(homeName);
		return home != null ? home.clone() : null;
	}

	public void deleteHome(Player player, String homeName) {
		Map<String, Location> playerHomes = homes.get(player.getUniqueId());
		if (playerHomes != null) {
			playerHomes.remove(homeName);
			if (playerHomes.isEmpty()) {
				homes.remove(player.getUniqueId());
			}
			runAsync(this::saveHomes);
		}
	}

	public void teleportHome(Player player, String homeName) {
		Location home = getHome(player, homeName);
		if (home == null) {
			player.sendMessage(ChatColor.RED + "Home '" + homeName + "' not found!");
			return;
		}
		player.teleport(home);
		player.sendMessage(ChatColor.GREEN + "Teleported to home '" + homeName + "'!");
	}

	public List<String> getHomeNames(Player player) {
		Map<String, Location> playerHomes = homes.get(player.getUniqueId());
		return playerHomes != null ? new ArrayList<>(playerHomes.keySet()) : new ArrayList<>();
	}

	public void shutdown() {
		saveHomes();
		if (dataSource != null) {
			dataSource.close();
		}
	}

	private void runAsync(Runnable task) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					task.run();
				} catch (Exception e) {
					plugin.getLogger().log(Level.SEVERE, "Error in async task: " + e.getMessage(), e);
				}
			}
		}.runTaskAsynchronously(plugin);
	}

	private void runLaterAsync(long delayTicks, Runnable task) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					task.run();
				} catch (Exception e) {
					plugin.getLogger().log(Level.SEVERE, "Error in async task: " + e.getMessage(), e);
				}
			}
		}.runTaskLaterAsynchronously(plugin, delayTicks);
	}
}