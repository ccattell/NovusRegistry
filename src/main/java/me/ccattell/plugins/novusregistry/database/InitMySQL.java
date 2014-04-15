package me.ccattell.plugins.novusregistry.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import me.ccattell.plugins.novusregistry.NovusRegistry;
import org.bukkit.ChatColor;

/**
 *
 * @author Charlie
 */
public class InitMySQL {

    NovusRegistry plugin;

    public InitMySQL(NovusRegistry plugin) {
        this.plugin = plugin;
    }

    Database service = Database.getInstance();
    public Connection connection = service.getConnection();
    public Statement statement = null;

    public void initMYSQL() {
        String RegistryTable = plugin.getConfig().getString("MySQLDatabase.RegistryTable");
        try {
            statement = connection.createStatement();
            String queryRegistry = "CREATE TABLE IF NOT EXISTS " + RegistryTable + " (player_name text NOT NULL, email text NOT NULL, password text NOT NULL, name text NOT NULL, age int(11) NOT NULL, last_login int(11) NOT NULL) CHARACTER SET utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(queryRegistry);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Could not create MySQL tables: " + e.getMessage() + ChatColor.RESET);
        }
    }
}
