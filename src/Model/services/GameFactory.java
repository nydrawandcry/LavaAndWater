package Model.services;

import Model.Game;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.units.interactive.ExitToken;
import Model.units.interactive.Player;
import Model.units.interactive.IronBlock;
import Model.units.solid.Wall;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

import java.util.ArrayList;
import java.util.List;

public class GameFactory {

    private static final int HEIGHT = 11;
    private static final int WIDTH = 16;

    public Game createGame() {
        Gamefield field = new Gamefield(HEIGHT, WIDTH);

        placeWalls(field);
        placeIronBlocks(field);

        Player player = placePlayer(field, 2, 8);
        Exit exit = placeExit(field, 14, 3);

        List<ExitToken> tokens = placeTokens(field);
        for(ExitToken token : tokens) {
            token.addExitTokenListener(exit.getTokenListener()); //подписываю выход на события каждого жетончика
        }

        Lava lava = new Lava();
        Water water = new Water();

        placeLavaSources(field, lava);
        placeWaterSources(field, water);

        return new Game(field, player, lava, water);
    }

    private void placeWalls(Gamefield field) {
        for (int x = 0; x < WIDTH; x++) {
            field.getCell(x, 0).putUnit(new Wall());
            field.getCell(x, HEIGHT - 1).putUnit(new Wall());
        }

        for (int y = 1; y < HEIGHT - 1; y++) {
            field.getCell(0, y).putUnit(new Wall());
            field.getCell(WIDTH - 1, y).putUnit(new Wall());
        }

        field.getCell(4, 9).putUnit(new Wall());
        field.getCell(4, 8).putUnit(new Wall());
        field.getCell(4, 7).putUnit(new Wall());

        field.getCell(8, 9).putUnit(new Wall());
        field.getCell(8, 8).putUnit(new Wall());
        field.getCell(8, 7).putUnit(new Wall());

        field.getCell(5, 7).putUnit(new Wall());
        field.getCell(7, 7).putUnit(new Wall()); //это я добавила область для водички

        field.getCell(7, 2).putUnit(new Wall());
        field.getCell(8, 2).putUnit(new Wall());
        field.getCell(9, 2).putUnit(new Wall());
        field.getCell(10, 2).putUnit(new Wall());
        field.getCell(11, 2).putUnit(new Wall());
        field.getCell(12, 2).putUnit(new Wall());
        field.getCell(13, 2).putUnit(new Wall());
        field.getCell(14, 2).putUnit(new Wall());
        field.getCell(13, 3).putUnit(new Wall());//а это стенка рядом с лавой
    }

    private void placeIronBlocks(Gamefield field) {
        field.getCell(6, 6).putUnit(new IronBlock());
    }

    private Player placePlayer(Gamefield field, int x, int y) {
        Player player = new Player();
        field.getCell(x, y).putUnit(player);
        return player;
    }

    private Exit placeExit(Gamefield field, int x, int y) {
        Exit exit = new Exit();
        field.getCell(x, y).putUnit(exit);
        return exit;
    }

    private void placeLavaSources(Gamefield field, Lava lava) {
        lava.addSource(field.getCell(11, 1));
    }

    private void placeWaterSources(Gamefield field, Water water) {
        water.addSource(field.getCell(6, 8));
    }

    private List<ExitToken> placeTokens(Gamefield field) {
        List<ExitToken> tokens = new ArrayList<>();

        ExitToken t1 = new ExitToken();
        field.getCell(2, 6).putUnit(t1);
        tokens.add(t1);

        ExitToken t2 = new ExitToken();
        field.getCell(12, 8).putUnit(t2);
        tokens.add(t2);

        return tokens;
    }
}
