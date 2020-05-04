package com.company;

class BotPlayer implements Player {

    public Point Move(Game game) {
        int x = (int) (Math.random() * game.GetSize()), y = (int) (Math.random() * game.GetSize());
        while (game.GetMap()[x][y] != State.Clear) {
            x = (int) (Math.random() * game.GetSize());
            y = (int) (Math.random() * game.GetSize());
        }
        return new Point(x, y);
    }

    @Override
    public String GetName() {
        return "bot";
    }
}
