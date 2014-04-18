package me.chipf0rk.nestnames;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatFormatter {
	private NestNames plugin;
	
	public ChatFormatter(NestNames plugin) {
		this.plugin = plugin;
	}
	
	public String format(Player player, String message) {
		return ChatColor.RESET + "<"+ formattedPlayerName(player) + ChatColor.RESET + "> " + message;
	}
	
	// HELPERS <
	public String formattedPlayerName(Player player) {
		String formattedName = player.getDisplayName();
		
		if(formattedName == null || formattedName == "null") {
			formattedName = player.getName();
		}
		
		if(plugin.perms() != null) {
			String group = plugin.perms().getPrimaryGroup(player);
			if(group == null || group == "null")
				group = "default";
			
			String groupColorName = plugin.getConfig().getString("groups." + group.toLowerCase() + ".color");
			
			ChatColor groupColor;
			if(groupColorName != null && groupColorName != "" && groupColorName != "null") {
				groupColor = ChatColor.valueOf(groupColorName);
			}
			else {
				plugin.logWarning("No group color value found for player " + player.getName() + ", in group " + group);
				groupColor = ChatColor.DARK_GRAY;
			}
			
			formattedName = groupColor + formattedName;
		}
		
		return formattedName;
	}
	// HELPERS >
}