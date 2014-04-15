package me.ccattell.plugins.novusregistry.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import me.ccattell.plugins.novusregistry.NovusRegistry;
import org.bukkit.ChatColor;

/**
 * Do basic SQL INSERT, UPDATE and DELETE queries.
 *
 * @author Charlie
 */
public class QueryFactory {

    Database service;
    Connection connection;
    NovusRegistry plugin;

    public QueryFactory(NovusRegistry plugin) {
        this.service = Database.getInstance();
        this.connection = service.getConnection();
        this.plugin = plugin;
    }

    /**
     * Inserts data into an SQLite database table. This method builds a prepared
     * SQL statement from the parameters supplied and then executes the insert.
     *
     * @param table the database table name to insert the data into.
     * @param data a HashMap<String, Object> of table fields and values to
     * insert.
     * @return the number of records that were inserted
     */
    public int doInsert(String table, HashMap<String, Object> data) {
        PreparedStatement ps = null;
        ResultSet idRS = null;
        String fields;
        String questions;
        StringBuilder sbf = new StringBuilder();
        StringBuilder sbq = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sbf.append(entry.getKey()).append(",");
            sbq.append("?,");
        }
        fields = sbf.toString().substring(0, sbf.length() - 1);
        questions = sbq.toString().substring(0, sbq.length() - 1);
        try {
            ps = connection.prepareStatement("INSERT INTO " + table + " (" + fields + ") VALUES (" + questions + ")");
            int i = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue().getClass().equals(String.class)) {
                    ps.setString(i, entry.getValue().toString());
                } else {
                    if (entry.getValue().getClass().getName().contains("Float")) {
                        ps.setDouble(i, Float.parseFloat(entry.getValue().toString()));
                    } else if (entry.getValue().getClass().getName().contains("Long")) {
                        ps.setLong(i, Long.parseLong(entry.getValue().toString()));
                    } else {
                        ps.setInt(i, Integer.parseInt(entry.getValue().toString()));
                    }
                }
                i++;
            }
            data.clear();
            ps.executeUpdate();
            idRS = ps.getGeneratedKeys();
            return (idRS.next()) ? idRS.getInt(1) : -1;
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Update error for " + table + "! " + ChatColor.RESET + e.getMessage());
            return -1;
        } finally {
            try {
                if (idRS != null) {
                    idRS.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Error closing " + table + "! " + ChatColor.RESET + e.getMessage());
            }
        }
    }

    /**
     * Updates data in an SQLite database table. This method builds an SQL query
     * string from the parameters supplied and then executes the update.
     *
     * @param table the database table name to update.
     * @param data a HashMap<String, Object> of table fields and values update.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to update.
     * @return true or false depending on whether the database update was
     * successful
     */
    public boolean doUpdate(String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        PreparedStatement statement = null;
        String updates;
        String wheres;
        StringBuilder sbu = new StringBuilder();
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sbu.append(entry.getKey()).append(" = ?,");
        }
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("' AND ");
            } else {
                sbw.append(entry.getValue()).append(" AND ");
            }
        }
        where.clear();
        updates = sbu.toString().substring(0, sbu.length() - 1);
        wheres = sbw.toString().substring(0, sbw.length() - 5);
        String query = "UPDATE " + table + " SET " + updates + " WHERE " + wheres;
        try {
            statement = connection.prepareStatement(query);
            int s = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue().getClass().equals(String.class)) {
                    statement.setString(s, entry.getValue().toString());
                }
                if (entry.getValue() instanceof Integer) {
                    statement.setInt(s, (Integer) entry.getValue());
                }
                if (entry.getValue() instanceof Long) {
                    statement.setLong(s, (Long) entry.getValue());
                }
                s++;
            }
            data.clear();
            return (statement.executeUpdate() > 0);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Update error for " + table + "! " + ChatColor.RESET + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Error closing " + table + "! " + ChatColor.RESET + e.getMessage());
            }
        }
    }

    /**
     * Deletes rows from an SQLite database table. This method builds an SQL
     * query string from the parameters supplied and then executes the delete.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to delete.
     * @return true or false depending on whether the data was deleted
     * successfully
     */
    public boolean doDelete(String table, HashMap<String, Object> where) {
        Statement statement = null;
        String values;
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("' AND ");
            } else {
                sbw.append(entry.getValue()).append(" AND ");
            }
        }
        where.clear();
        values = sbw.toString().substring(0, sbw.length() - 5);
        String query = "DELETE FROM " + table + " WHERE " + values;
        try {
            statement = connection.createStatement();
            return (statement.executeUpdate(query) > 0);
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Delete error for " + table + "! " + ChatColor.RESET + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Error closing " + table + "! " + ChatColor.RESET + e.getMessage());
            }
        }
    }

    public String checkPlayerRegister(String player) {
        Statement statement = null;
        String RegistryTable = plugin.getConfig().getString("MySQLDatabase.RegistryTable");
        String query = "SELECT COUNT(*) AS rowcount FROM " + RegistryTable + " WHERE player_name = '" + player + "'";
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                rs.next();
                if (rs.getInt("rowcount") > 0) {
                    return "active";
                }
            }
        } catch (SQLException e) {
            plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get " + player + " info! " + ChatColor.RESET + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.console.sendMessage(plugin.pluginName + ChatColor.GOLD + "Couldn't get " + player + " info, closing Logins! " + ChatColor.RESET + e.getMessage());
            }
        }
        return "none";
    }

}
