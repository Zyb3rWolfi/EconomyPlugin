package io.github.zyb3rwolfi.Economy;

import org.bukkit.entity.Player;

import java.sql.*;

public class DatabaseManager {

    private final Connection connection;

    public DatabaseManager(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
            CREATE TABLE IF NOT EXISTS players (
            uuid TEXT PRIMARY KEY,
            username TEXT NOT NULL,
            money INTEGER NOT NULL DEFAULT 0
            )
        
        """);
        }

    }



    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public int getPlayerBalance(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT money FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("money");
            } else {
                return 0;
            }
        }
    }

    public int addPlayerToDB(Player player, int startingBalance) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players VALUES (? , ?, ?)")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setInt(3, startingBalance);
            preparedStatement.executeUpdate();

            return 1;
        }
    }
    public void setPlayerMoney(Player player, int amount) {
        try {
            try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET money = ? WHERE uuid = ? ")) {
                preparedStatement.setString(2, player.getUniqueId().toString());
                preparedStatement.setInt(1, amount);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Error");
        }
    }
    public void addMonetToPlayer(Player player, int amount) {
        try {
            int money = getPlayerBalance(player);
            money += amount;
            try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET money = ? WHERE uuid = ? ")) {
                preparedStatement.setString(2, player.getUniqueId().toString());
                preparedStatement.setInt(1, money);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Error");
        }
    }
    public void takeMoneyFromPlayer(Player player, int amount) {
        try {
            int money = getPlayerBalance(player);
            money -= amount;
            try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET money = ? WHERE uuid = ? ")) {
                preparedStatement.setString(2, player.getUniqueId().toString());
                preparedStatement.setInt(1, money);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Error");
        }
    }

    public void giveMoneyToPlayer(Player player, Player givePlayer, int amount) throws SQLException {

        try{
            try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT money FROM players WHERE uuid = ?")) {
                preparedStatement.setString(1, player.getUniqueId().toString());
                ResultSet playerMoney = preparedStatement.executeQuery();

                if (playerMoney.next()) {
                    int money = playerMoney.getInt("money");
                    money -= amount;

                    try(PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE players SET money = ? WHERE uuid = ?")) {
                        preparedStatement1.setInt(1, money);
                        preparedStatement1.setString(2, player.getUniqueId().toString());
                        preparedStatement1.executeUpdate();

                        int givenPlayerMoney = getPlayerBalance(givePlayer);
                        givenPlayerMoney += amount;
                        addMonetToPlayer(givePlayer, givenPlayerMoney);
                    }

                } else {
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    public StringBuilder getTopBalance() throws SQLException {
        StringBuilder results = new StringBuilder("");
        int rank = 0;
        try (PreparedStatement statement = connection.prepareStatement("SELECT username, money FROM players ORDER BY money DESC LIMIT ?")) {
            statement.setInt(1, 10);
            ResultSet resultSeresults = statement.executeQuery();

            while (resultSeresults.next()) {
                rank += 1;
                results.append( rank + " - " + resultSeresults.getString("username")).append(" ").append(resultSeresults.getInt("money") + " \n");
            }
            return results;
        }
    }

}