package io.github.zyb3rwolfi.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TopBalance implements CommandExecutor {

    private Economy economy;

    public TopBalance(Economy economy) {
        this.economy = economy;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        try {

            StringBuilder results = economy.getDatabaseManager().getTopBalance();
            Player player = (Player) sender;
            economy.sendMessage(results.toString(), player);

        } catch (SQLException e) {
            System.out.println(e);
        }
        return true;
    }
}