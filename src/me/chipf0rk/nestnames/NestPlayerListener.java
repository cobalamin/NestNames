package me.chipf0rk.nestnames;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class NestPlayerListener implements Listener {
	public NestNames plugin;
	
	public NestPlayerListener(NestNames newPlugin) {
		this.plugin = newPlugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	// EVENT HANDLERS <
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		message = message.replace("%", "%%"); // so that percent signs in messages don't get interpreted as formatting instructions
		
		event.setFormat(
			plugin.chatFormatter().format(event.getPlayer(), message)
		);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.initializeRealName(event.getPlayer());
	}
	// EVENT HANDLERS >
}
