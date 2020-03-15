package com.company;

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
                Game game = new Game();
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

