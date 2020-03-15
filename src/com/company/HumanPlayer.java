package com.company;

import java.util.Scanner;

class HumanPlayer implements Player {
    public Point Move(Game game) {
        Scanner in = new Scanner(System.in);
        int x, y;
        do {
            System.out.println("Enter x (1.." + game.GetSize() + "):");
            x = in.nextInt() - 1;
            System.out.println("Enter y (1.." + game.GetSize() + "):");
            y = in.nextInt() - 1;
        } while (x < 0 || x > game.GetSize() - 1 || y < 0 || y > game.GetSize() - 1 || game.GetMap()[x][y] != State.Clear);
        return new Point(x, y);
    }
}
