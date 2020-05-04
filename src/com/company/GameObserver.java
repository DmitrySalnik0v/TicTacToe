package com.company;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;

public class GameObserver implements Observer {

    @Override
    public void handleEvent(Game game) {
        try {
            String username = "root";
            String password = "ыйгфвцшзу";
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/tictactoedb?serverTimezone=Europe/Minsk&useSSL=false", username, password);
            String sql = "INSERT game_status_log(game_id,status) VALUES (?,?) ON DUPLICATE KEY UPDATE status = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, game.getId());
            preparedStatement.setString(2, game.getGameStatus().toString());
            preparedStatement.setString(3, game.getGameStatus().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(game.getGameStatus().toString());
    }
}
