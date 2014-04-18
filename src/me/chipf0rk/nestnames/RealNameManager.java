package me.chipf0rk.nestnames;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class RealNameManager {
	private NestNames plugin;
	
	public RealNameManager(NestNames plugin) {
		this.plugin = plugin;
		reloadConfig();
	}
	
	// ACTUAL METHODS
	public boolean setRealName(Player player, String[] args) {
		String newName = StringUtils.join(args, " ");
		
		if(newName == "" || newName == "null" || newName.length() > 20)
			return false;
		
		player.setDisplayName(newName);
		player.setPlayerListName(
			plugin.chatFormatter().formattedPlayerName(player)
		);
		getConfig().set(player.getName(), newName);
		saveConfig();
		
		return true;
	}
	
	public String getRealName(Player player) {
		return getConfig().getString(player.getName());
	}
	
	// === LAME IMPLEMENTATIONS YAWN
	private FileConfiguration config = null;
	private File configFile = null;
	public void reloadConfig() {
	    if (configFile == null) {
	    	configFile = new File(plugin.getDataFolder(), "real_names.yml");
	    }
	    config = YamlConfiguration.loadConfiguration(configFile);
	 
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource("real_names.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        config.setDefaults(defConfig);
	    }
	}
	public FileConfiguration getConfig() {
	    if (config == null) {
	        reloadConfig();
	    }
	    return config;
	}
	public void saveConfig() {
	    if (config == null || configFile == null) {
	        return;
	    }
	    try {
	        getConfig().save(configFile);
	    } catch (IOException ex) {
	        plugin.logException(ex, "Could not save config to " + configFile);
	    }
	}
}
