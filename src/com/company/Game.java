package com.company;

import java.util.Scanner;

class Game {
    static private int size = 3;

    public int GetSize() {
        return size;
    }

    private State[][] map = new State[size][size];

    public State[][] GetMap() {
        return map;
    }

    private static int[] stats = new int[3];

    public static void GetStats() {
        System.out.println("Statistics: Player X wins, Draws, Player O wins");
        System.out.println(stats[0] + "   " + stats[1] + "    " + stats[2]);
    }

    public Game(Player playerX, Player playerO) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = State.Clear;
            }
        }
        this.playerX = playerX;
        this.playerO = playerO;
    }

    public static void SetStats(int stat) {
        switch (stat) {
            case 0:
                stats[0]++;
                break;
            case 1:
                stats[1]++;
                break;
            case 2:
                stats[2]++;
                break;
        }
    }

    Player playerX;
    Player playerO;

    public int Start() {
        int stat;
        System.out.println("Game started!!!");
        ShowField();
        do {
            System.out.println("PLAYER X:");
            Point point;
            do {
                point = playerX.Move(this);
            } while (!ChangeMap(point, State.X));
            ShowField();
            if (CheckGameState() != State.Clear) {
                System.out.println("Player X WIN!");
                stat = 0;
                break;
            }
            if (IsMapEnded()) {
                System.out.println("DRAW!");
                stat = 1;
                break;
            }
            System.out.println("PLAYER O:");
            do {
                point = playerO.Move(this);
            } while (!ChangeMap(point, State.O));
            ShowField();
            if (CheckGameState() != State.Clear) {
                System.out.println("Player O WIN!");
                stat = 2;
                break;
            }
            if (IsMapEnded()) {
                System.out.println("DRAW!");
                stat = 1;
                break;
            }
        } while (true);
        return stat;
    }

    public void ShowField() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] == State.Clear) {
                    System.out.print("_" + " ");
                } else {
                    System.out.print(map[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println("//////////");
    }

    public boolean IsMapEnded() {
        boolean isEnded = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (map[i][j] == State.Clear) {
                    isEnded = false;
                }
            }
        }
        return isEnded;
    }

    public State CheckGameState() {
        State winner = State.Clear;
        int equal = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                if (map[i][j] == map[i][j + 1] && map[i][j] != State.Clear) {
                    equal++;
                }
            }
            if (equal == size - 1) {
                winner = map[i][0];
                break;
            } else equal = 0;
        }
        if (winner == State.Clear) {
            for (int j = 0; j < size; j++) {
                for (int i = 0; i < size - 1; i++) {
                    if (map[i][j] == map[i + 1][j] && map[i][j] != State.Clear) {
                        equal++;
                    }
                }
                if (equal == size - 1) {
                    winner = map[0][j];
                    break;
                } else equal = 0;
            }
        }
        if (winner == State.Clear) {
            for (int i = 0, j = 0; i < size - 1; i++, j++) {
                if (map[i][j] == map[i + 1][j + 1] && map[i][j] != State.Clear) {
                    equal++;
                }
            }
            if (equal == size - 1) {
                winner = map[0][0];
            } else equal = 0;
        }
        if (winner == State.Clear) {
            for (int i = 0, j = size - 1; i < size - 1; i++, j--) {
                if (map[i][j] == map[i + 1][j - 1] && map[i][j] != State.Clear) {
                    equal++;
                }
            }
            if (equal == size - 1) {
                winner = map[0][size - 1];
            } else equal = 0;
        }
        return winner;
    }

    Game() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = State.Clear;
            }
        }
    }

    public boolean ChangeMap(Point point, State sender) {
        boolean changeConfirmed = true;
        if (point.GetX() < 0 || point.GetX() > GetSize() - 1 || point.GetY() < 0 || point.GetY() > GetSize() - 1 ||
                GetMap()[point.GetX()][point.GetY()] != State.Clear) {
            changeConfirmed = false;
        } else {
            map[point.GetX()][point.GetY()] = sender;
        }
        return changeConfirmed;
    }

    public void Clear() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = State.Clear;
            }
        }
    }
}
