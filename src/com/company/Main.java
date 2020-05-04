package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int command = 0;
        while (true) {
            do {
                try {
                    System.out.println("1-Start the game, 2-Statistics, 3-Exit");
                    Scanner in = new Scanner(System.in);
                    command = in.nextInt();
                } catch (Exception e) {
                    continue;
                }
            } while (command < 1 || command > 3);
            if (command == 3) break;
            if (command == 1) {
                Scanner in = new Scanner(System.in);
                String select1, select2;
                do {
                    System.out.println("Select Player 1: man/bot");
                    select1 = in.nextLine();
                    System.out.println("Select Player 2: man/bot");
                    select2 = in.nextLine();
                } while (!(select1.equals("man") || select1.equals("bot")) || !(select2.equals("man") || select2.equals("bot")));
                Player playerX, playerO;
                if (select1.equals("man")) {
                    playerX = new HumanPlayer();
                } else playerX = new BotPlayer();
                if (select2.equals("man")) {
                    playerO = new HumanPlayer();
                } else playerO = new BotPlayer();
                Game game = new Game(playerX, playerO);
                game.addObserver(new GameObserver());
                int stat = game.Start();
                Game.SetStats(stat);
            }
            if (command == 2) {
                Game.GetStats();
            }
        }
    }
}

enum State {
    Clear,
    X,
    O
}

enum Status {
    CREATED,
    STARTED,
    FINISHED
}

