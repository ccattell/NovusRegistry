package me.ccattell.plugins.novusregistry.commands;

import me.ccattell.plugins.novusregistry.NovusRegistry;
import me.ccattell.plugins.novusregistry.database.QueryFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Charlie
 */
public class RegisterCommand implements CommandExecutor {

    NovusRegistry plugin;

    public RegisterCommand(NovusRegistry plugin) {
        this.plugin = plugin;
    }

    QueryFactory qf = new QueryFactory(plugin);
    public String found_player;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("register")) {
            Player player;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(plugin.pluginName + "The register command cannot be used from the console!");
                return true;
            }

            String registerCheck = qf.checkPlayerRegister(player.getName());
            if (registerCheck.equalsIgnoreCase("none")) {
                player.sendMessage(plugin.pluginName + "You are not registered yet!");
            } else if (registerCheck.equalsIgnoreCase("registered")) {
                player.sendMessage(plugin.pluginName + "Congratulations, you are now a memner!");
            } else if (registerCheck.equalsIgnoreCase("active")) {
                player.sendMessage(plugin.pluginName + "You are already a member!");
            }
        }
        return true;
    }
}
