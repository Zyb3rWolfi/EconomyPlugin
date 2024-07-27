package io.github.zyb3rwolfi.Economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ShowBalance implements CommandExecutor {

    private Economy economy;

    public ShowBalance(Economy economy) {
        this.economy = economy;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player");
            return true;
        }

        Player player = (Player) sender;

        int money = getBalanceFromDB(player);


        economy.sendMessage("Your balance: " + money, player);
        return true;
    }

    int getBalanceFromDB( Player player) {
        try {
            return economy.getDatabaseManager().getPlayerBalance(player);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
            return 0;
        }
    }
}