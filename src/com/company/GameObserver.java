package com.company;

import java.sql.*;

public class GameObserver implements Observer {

    @Override
    public void handleEvent(Game game) {
        try {
            String username = "root";
            String password = "1234";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/tictactoedb?serverTimezone=Europe/Minsk&useSSL=false", username, password);
            if (game.getGameStatus() == Status.CREATED) {
                String sql = "INSERT game(playerX_name,playerO_name) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, game.getPlayerX().GetName());
                preparedStatement.setString(2, game.getPlayerO().GetName());
                preparedStatement.executeUpdate();
                sql = "SELECT id FROM game ORDER BY id DESC LIMIT 1";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    game.setId(resultSet.getInt(1));
                }
            }
            String sql = "INSERT game_status_log(game_id,status) VALUES (?,?) ON DUPLICATE KEY UPDATE status = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, game.getId());
            preparedStatement.setString(2, game.getGameStatus().toString());
            preparedStatement.setString(3, game.getGameStatus().toString());
            preparedStatement.executeUpdate();
            if (game.getGameStatus() == Status.FINISHED) {
                String sqlStat = "INSERT game_stats(game_id, playerX_win, playerO_win) VALUES (?,?,?)";
                preparedStatement = connection.prepareStatement(sqlStat);
                preparedStatement.setInt(1, game.getId());
                switch (game.getGameStat()) {
                    case 0:
                        preparedStatement.setString(2, "win");
                        preparedStatement.setString(3, "lose");
                        break;
                    case 1:
                        preparedStatement.setString(2, "draw");
                        preparedStatement.setString(3, "draw");
                        break;
                    case 2:
                        preparedStatement.setString(2, "lose");
                        preparedStatement.setString(3, "win");
                        break;
                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(game.getGameStatus().toString());
    }
}
