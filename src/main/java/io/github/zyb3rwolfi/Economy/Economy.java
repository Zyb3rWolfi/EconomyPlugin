package io.github.zyb3rwolfi.Economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.PKCS12Attribute;
import java.sql.Connection;
import java.sql.SQLException;
import io.github.zyb3rwolfi.Economy.EconomyAPI;

import javax.xml.crypto.Data;

public final class Economy extends JavaPlugin implements EconomyAPI {
    private Connection connection;
    private String prefix;
    private static EconomyAPI instance;
    private DatabaseManager dataBaseManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        saveResource("config.yml", false);
        saveDefaultConfig();
        int startingBalance = getConfig().getInt("starting_balance");
        prefix = getConfig().getString("prefix");

        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }
            dataBaseManager = new DatabaseManager(getDataFolder().getAbsoluteFile() + "/money.db");
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("Failed: " + e.getMessage());
        }

        getCommand("balance").setExecutor(new ShowBalance(this));
        getCommand("set").setExecutor(new SetMoney(this));
        getCommand("give").setExecutor(new GiveMoney(this));
        getCommand("baltop").setExecutor(new TopBalance(this));
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(this, startingBalance), this);

    }

    public DatabaseManager getDatabaseManager() {
        return dataBaseManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void sendMessage(String message, Player player) {
        String msg = prefix + message;
        player.sendMessage(msg);
    }

    public static EconomyAPI getInstance() {
        return instance;
    }
    @Override
    public double getBalance(Player player) {
        try {
            return dataBaseManager.getPlayerBalance(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public double giveMoney(Player player, int amount) {
        try {
            dataBaseManager.addMonetToPlayer(player, amount);
            return dataBaseManager.getPlayerBalance(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double takeMoney(Player player, int amount) {
        try {
            dataBaseManager.takeMoneyFromPlayer(player, amount);
            return dataBaseManager.getPlayerBalance(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
