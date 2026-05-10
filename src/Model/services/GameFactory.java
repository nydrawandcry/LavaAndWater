package Model.services;

import Model.Game;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.units.moving.Player;
import Model.units.moving.IronBlock;
import Model.units.solid.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

public class GameFactory {

    private static final int HEIGHT = 11;
    private static final int WIDTH = 16;

    public Game createGame() {
        Gamefield field = new Gamefield(HEIGHT, WIDTH);

        placeWalls(field);
        placeIronBlocks(field);

        Player player = placePlayer(field, 3, 3);
        placeExit(field, 8, 15);

        Lava lava = new Lava();
        Water water = new Water();

        placeLavaSources(field, lava);
        placeWaterSources(field, water);

        return new Game(field, player, lava, water);
    }

    private void placeWalls(Gamefield field) {
        for (int col = 0; col < WIDTH; col++) {
            field.getCell(0, col).putUnit(new Wall());
            field.getCell(HEIGHT - 1, col).putUnit(new Wall());
        }

        for (int row = 1; row < HEIGHT - 1; row++) {
            field.getCell(row, 0).putUnit(new Wall());
            field.getCell(row, WIDTH - 1).putUnit(new Wall());
        }

        field.getCell(5, 2).putUnit(new Wall());
        field.getCell(5, 3).putUnit(new Wall());
        field.getCell(5, 4).putUnit(new Wall());

        field.getCell(9, 2).putUnit(new Wall());
        field.getCell(9, 3).putUnit(new Wall());
        field.getCell(9, 4).putUnit(new Wall());

        field.getCell(6, 4).putUnit(new Wall());
        field.getCell(8, 4).putUnit(new Wall()); //это я добавила область для водички

        field.getCell(8, 9).putUnit(new Wall());
        field.getCell(9, 9).putUnit(new Wall());
        field.getCell(10, 9).putUnit(new Wall());
        field.getCell(11, 9).putUnit(new Wall());
        field.getCell(12, 9).putUnit(new Wall());
        field.getCell(13, 9).putUnit(new Wall());
        field.getCell(14, 9).putUnit(new Wall());
        field.getCell(15, 9).putUnit(new Wall());
        field.getCell(14, 8).putUnit(new Wall());
    }

    private void placeIronBlocks(Gamefield field) {
        field.getCell(7, 5).putUnit(new IronBlock());
    }

    private Player placePlayer(Gamefield field, int row, int col) {
        Player player = new Player();
        field.getCell(row, col).putUnit(player);
        return player;
    }

    private void placeExit(Gamefield field, int row, int col) {
        field.getCell(row, col).putUnit(new Exit());
    }

    private void placeLavaSources(Gamefield field, Lava lava) {
        lava.addSource(field.getCell(12, 10));
    }

    private void placeWaterSources(Gamefield field, Water water) {
        water.addSource(field.getCell(7, 3));
    }
}
