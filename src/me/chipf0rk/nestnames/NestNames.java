package me.chipf0rk.nestnames;

import java.util.logging.Level;

import net.milkbowl.vault.permission.Permission;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class NestNames extends JavaPlugin {
	private Permission perms;
	private ChatFormatter chatFormatter;
	private RealNameManager realNameManager;
	NestPlayerListener playerListener;
	
	// GETTERS
	public Permission perms() {
		return perms;
	}
	public ChatFormatter chatFormatter() {
		return chatFormatter;
	}
	
	// ENABLE / DISABLE
	@Override
	public void onEnable() {
		if(!setupPermissions()) {
			logSevere("failed hooking into Vault. The plugin will not run!");
		}
		else {
			logInfo("successfully hooked into Vault.");
			
			playerListener = new NestPlayerListener(this);
			chatFormatter = new ChatFormatter(this);
			realNameManager = new RealNameManager(this);
			
			saveDefaultConfig();
			saveConfig();
			
			logInfo("v" + version() + " has been enabled.");
		}
	}
	
	@Override
	public void onDisable() {
		perms = null;
		
		chatFormatter = null;
		realNameManager = null;
		
		HandlerList.unregisterAll(this);
		playerListener = null;
		
		logInfo("v" + version() + " has been disabled.");
	}
	
	// COMMANDS <
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("realname")) {
			if( !(sender instanceof Player) ) {
				sender.sendMessage("Only players can access this command."); 
				return true;
			}
			if(args.length < 1) { return false;	}
			
			Player player = (Player) sender;
			boolean success = realNameManager.setRealName(player, args);
			if(success) {
				sendMessage(player, "Yup " + args[0] + ", you're all set dawg.");
			}
			else {
				sendMessage(player, "I'd appreciate it if you tried to set a proper real name bro <3");
			}
			
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("dawgs")) {
			sender.sendMessage( getPlayerList() );
			return true;
		}
		
		return false;
	}
	// COMMANDS >
	
	// ACTIONS <
	public void initializeRealName(Player player) {
		String realName = realNameManager.getRealName(player);
		
		if(realName != "" && realName != null && realName != "null") {
			player.setDisplayName(realName);
			player.setPlayerListName(
				chatFormatter.formattedPlayerName(player)
			);
		}
	}
	// ACTIONS >
	
	// HELPERS <
	private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            perms = permissionProvider.getProvider();
        }
        return (perms != null);
    }
	
	private String getPlayerList() {
		Player[] onlinePlayers = this.getServer().getOnlinePlayers();
		String[] playerList = new String[ onlinePlayers.length ];
		
		Player currentPlayer;
		String currentPlayerName, currentPlayerDisplayName;
		
		for(int i = 0; i < onlinePlayers.length; i++) {
			currentPlayer = onlinePlayers[i];
			
			if(currentPlayer != null) { // for all I know this might actually happen, idk
				currentPlayerName = currentPlayer.getName();
				currentPlayerDisplayName = currentPlayer.getDisplayName();
				
				playerList[i] =	chatFormatter.formattedPlayerName(currentPlayer);
				
				if(currentPlayerName != currentPlayerDisplayName) {
					playerList[i] += ChatColor.GRAY + " (" + currentPlayerName + ")" + ChatColor.RESET;
				}
			}
		}
		
		return StringUtils.join(playerList, ", ");
	}
	
	void sendMessage(Player player, String message) {
		String formattedMessage =
			ChatColor.RESET + "[" +
			ChatColor.GOLD + this.getName() +
			ChatColor.RESET + "] " +
			message;
		
		player.sendMessage(formattedMessage);
	}

	void logInfo(String msg) {
		this.getLogger().info(msg);
	}
	void logWarning(String msg) {
		this.getLogger().warning(msg);
	}
	void logSevere(String msg) {
		this.getLogger().severe(msg);
	}
	void logException(Exception e, String msg) {
		this.getLogger().log(Level.SEVERE, msg, e);
	}
	private String version() {
		return this.getDescription().getVersion();
	}
	// HELPERS >
}
