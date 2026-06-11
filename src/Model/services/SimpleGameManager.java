package Model.services;

import Model.Game;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.units.interactive.Boat;
import Model.units.interactive.ExitScore;
import Model.units.interactive.IronBlock;
import Model.units.interactive.Player;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;
import Model.units.solid.Wall;

import java.util.ArrayList;
import java.util.List;

public class SimpleGameManager extends GameManager{

    public SimpleGameManager(Gamefield field, Game game) {
        super(field, game);
    }

    @Override
    protected void placeInnerWalls(Gamefield field) {
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

    @Override
    protected void placeIronBlocks(Gamefield field) {
        field.getCell(6, 6).putUnit(new IronBlock());
    }

    @Override
    protected Player placePlayer(Gamefield field, int x, int y) {
        Player player = new Player();
        player.addPlayerReachListener(getGame().getPlayerListener());
        field.getCell(x, y).putUnit(player);
        return player;
    }

    @Override
    protected Exit placeExit(Gamefield field, int x, int y) {
        Exit exit = new Exit();
        field.getCell(x, y).putUnit(exit);
        return exit;
    }

    @Override
    protected void placeLavaSources(Gamefield field, Lava lava) {
        lava.addLiquidSystemCollisionListener(getDetector().getLiquidListener());
        lava.addSource(field.getCell(11, 1));
    }

    @Override
    protected void placeWaterSources(Gamefield field, Water water) {
        water.addLiquidSystemCollisionListener(getDetector().getLiquidListener());
        water.addSource(field.getCell(6, 8));
    }

    @Override
    protected void placeExitScores(Gamefield field, Exit exit) {
        List<ExitScore> scores = new ArrayList<>();

        ExitScore t1 = new ExitScore();
        field.getCell(2, 6).putUnit(t1);
        scores.add(t1);

        ExitScore t2 = new ExitScore();
        field.getCell(12, 8).putUnit(t2);
        scores.add(t2);

        exit.deactivate();

        for(ExitScore score : scores) {
            exit.addExitScore(score);
            score.addExitScoreListener(exit.getExitScoreListener());
        }
    }

    protected void placeBoats(Gamefield field) {
        field.getCell(3,7).putUnit(new Boat());
    }
}
