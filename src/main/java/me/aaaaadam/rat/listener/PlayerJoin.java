package me.aaaaadam.rat.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompToastStyle;
import org.mineacademy.fo.remain.Remain;

@AutoRegister
public final class PlayerJoin implements Listener {

	public PlayerJoin() {
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5, 100));
		Remain.sendToast(player, "Our Forever World", CompMaterial.SUNFLOWER, CompToastStyle.TASK);
	}
}