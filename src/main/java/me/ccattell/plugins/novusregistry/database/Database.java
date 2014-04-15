package me.ccattell.plugins.novusregistry.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import me.ccattell.plugins.novusregistry.NovusRegistry;

/**
 *
 * @author Charlie and Buwaroblahblah die
 */
public class Database {

    NovusRegistry plugin;
    private static final Database instance = new Database();
    public Connection connection = null;
    public Statement statement = null;

    public static synchronized Database getInstance() {
        return instance;
    }

    public void setConnection() throws Exception {
        try {
            plugin.console.sendMessage(plugin.pluginName + "Loading driver...");
            Class.forName("com.mysql.jdbc.Driver");
            plugin.console.sendMessage(plugin.pluginName + "Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find the driver in the classpath!", e);
        }
        String host = "jdbc:mysql://localhost:3306/" + plugin.getConfig().getString("MySQLDatabase.DBName");
        String user = plugin.getConfig().getString("MySQLDatabase.Username");
        String pass = plugin.getConfig().getString("MySQLDatabase.Password");
        try {
            plugin.console.sendMessage(plugin.pluginName + "Connecting database...");
            connection = DriverManager.getConnection(host, user, pass);
            plugin.console.sendMessage(plugin.pluginName + "Database connected!");
        } catch (SQLException e) {
            throw new RuntimeException("Cannot connect the database!", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setPlugin(NovusRegistry plugin) {
        this.plugin = plugin;
    }
}
