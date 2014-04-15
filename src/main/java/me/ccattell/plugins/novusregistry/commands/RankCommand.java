package me.ccattell.plugins.novusregistry.commands;

import me.ccattell.plugins.novusregistry.NovusRegistry;
import me.ccattell.plugins.novusregistry.database.QueryFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Charles
 */
public class RankCommand implements CommandExecutor {

    NovusRegistry plugin;

    public RankCommand(NovusRegistry plugin) {
        this.plugin = plugin;
    }

    QueryFactory qf = new QueryFactory(plugin);
    public String found_player;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //      /rank [rank name] [player]      sets specified user to given rank.... must be compatible with both PermissionsBukkit and GroupManager
        //      can be run from console  ( for use in buycraft )
        return true;
    }
}
