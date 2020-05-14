package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Game implements Observed {

    List<Observer> subscribers = new ArrayList<>();

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private Player playerX;
    private Player playerO;

    public Player getPlayerX() {
        return playerX;
    }

    public Player getPlayerO() {
        return playerO;
    }

    static private int size = 3;

    public int GetSize() {
        return size;
    }

    private Status gameStatus;

    public Status getGameStatus() {
        return gameStatus;
    }

    private State[][] map = new State[size][size];

    public State[][] GetMap() {
        return map;
    }

    private static int[] stats = new int[3];

    private int gameStat;

    public int getGameStat() {
        return gameStat;
    }

    private void setGameStat(int gameStat) {
        this.gameStat = gameStat;
    }

    public static void GetStats() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/tictactoedb?serverTimezone=Europe/Minsk&useSSL=false", "root", "1234");
            String sql = "SELECT * FROM game_stats";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("Статистика игр:");
            System.out.println("id\tИгрокX\tИгрокO");
            while (resultSet.next()) {
                int idStat = resultSet.getInt(1);
                String playerXStat = resultSet.getString(2);
                String playerOStat = resultSet.getString(3);
                System.out.println(idStat + " " + playerXStat + " " + playerOStat);
            }
            System.out.println();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Statistics: Player X wins, Draws, Player O wins");
            System.out.println(stats[0] + "   " + stats[1] + "    " + stats[2]);
        }
    }

    public Game(Player playerX, Player playerO) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = State.Clear;
            }
        }
        this.playerX = playerX;
        this.playerO = playerO;
        this.addObserver(new GameObserver());
        gameStatus = Status.CREATED;
        notifyObservers();
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

    public int Start() {
        gameStatus = Status.STARTED;
        notifyObservers();
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
        setGameStat(stat);
        gameStatus = Status.FINISHED;
        notifyObservers();
        return stat;
    }

    private void ShowField() {
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

    private boolean IsMapEnded() {
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

    private State CheckGameState() {
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

    private boolean ChangeMap(Point point, State sender) {
        boolean changeConfirmed = true;
        if (point.GetX() < 0 || point.GetX() > GetSize() - 1 || point.GetY() < 0 || point.GetY() > GetSize() - 1 ||
                GetMap()[point.GetX()][point.GetY()] != State.Clear) {
            changeConfirmed = false;
        } else {
            map[point.GetX()][point.GetY()] = sender;
        }
        return changeConfirmed;
    }

    private void Clear() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = State.Clear;
            }
        }
    }

    @Override
    public void addObserver(Observer observer) {
        this.subscribers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.subscribers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : subscribers) {
            observer.handleEvent(this);
        }
    }
}
