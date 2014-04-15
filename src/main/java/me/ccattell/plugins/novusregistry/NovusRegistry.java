package me.ccattell.plugins.novusregistry;

import me.ccattell.plugins.novusregistry.commands.RegisterCommand;
import me.ccattell.plugins.novusregistry.commands.RankCommand;
import me.ccattell.plugins.novusregistry.database.Database;
import me.ccattell.plugins.novusregistry.database.InitMySQL;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NovusRegistry extends JavaPlugin implements Listener {

    public static NovusRegistry plugin;
    Database service;
    public ConsoleCommandSender console;
    public String pluginName;

    @Override
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }

    @Override
    public void onEnable() {
        service = Database.getInstance();
        service.setPlugin(this);
        console = getServer().getConsoleSender();
        pluginName = ChatColor.DARK_PURPLE + "[Novus Registry]" + ChatColor.RESET + " ";
        saveDefaultConfig();
        try {
            console.sendMessage(pluginName + "Loading MySQL Database");
            service.setConnection();
            new InitMySQL(this).initMYSQL();
        } catch (Exception e) {
            console.sendMessage(pluginName + ChatColor.GOLD + "Connection and Tables Error: " + e);
        }
        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("rank").setExecutor(new RankCommand(this));
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin = this;
        String DefaultGroup = plugin.getConfig().getString("DefaultGroup");
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            //add player to database with IGN and UUID
            //if permbukkit set player to DefaultGroup rank
        }
    }
}
