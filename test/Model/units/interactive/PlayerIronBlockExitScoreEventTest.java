package Model.units.interactive;

import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.events.player.PlayerReachListener;
import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerIronBlockExitScoreEventTest {

    @Test
    void playerPushesIronBlock_eventChainIsStrictlyOrdered_andEveryCallbackChecksCurrentState() {
        Gamefield field = new Gamefield(1, 3);
        Cell playerCell = field.getCell(0, 0);
        Cell blockCell = field.getCell(1, 0);
        Cell blockDestination = field.getCell(2, 0);

        Player player = new Player();
        IronBlock block = new IronBlock();

        assertTrue(playerCell.putUnit(player));
        assertTrue(blockCell.putUnit(block));

        List<String> events = new ArrayList<>();

        blockCell.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                if (e.getUnit() == block) {
                    fail("blockCell не должна получать unitPlaced для блока при толчке");
                }

                // unitPlaced(player) здесь допустим.
                // Его состояние проверяется отдельным listener-ом ниже.
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                events.add("blockExtracted");

                assertSame(block, e.getUnit());
                assertNull(block.owner());
                assertNull(blockCell.getUnit(IronBlock.class));
                assertTrue(blockDestination.isEmpty());
                assertSame(playerCell, player.owner());
            }
        });

        block.addUnitActivationListener(() -> {
            events.add("blockActivatedInNewCell");

            assertSame(blockDestination, block.owner());
            assertSame(block, blockDestination.getUnit(IronBlock.class));
            assertNull(blockCell.getUnit(IronBlock.class));
            assertSame(playerCell, player.owner());
        });

        blockDestination.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                events.add("blockPlaced");

                assertSame(block, e.getUnit());
                assertSame(blockDestination, block.owner());
                assertSame(block, blockDestination.getUnit(IronBlock.class));
                assertSame(playerCell, player.owner());
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                fail("blockDestination не должна получать unitExtracted");
            }
        });

        block.addIronBlockActionListener(() -> {
            events.add("ironBlockMoved");

            // Событие IronBlock идет после извлечения и установки блока,
            // но до перемещения игрока.
            assertSame(blockDestination, block.owner());
            assertSame(block, blockDestination.getUnit(IronBlock.class));
            assertNull(blockCell.getUnit(IronBlock.class));
            assertSame(playerCell, player.owner());
            assertNull(blockCell.getUnit(Player.class));
        });

        playerCell.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                fail("playerCell не должна получать unitPlaced при уходе игрока");
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                events.add("playerExtracted");

                assertSame(player, e.getUnit());
                assertNull(player.owner());
                assertNull(playerCell.getUnit(Player.class));
                assertNull(blockCell.getUnit(Player.class));
                assertSame(blockDestination, block.owner());
            }
        });

        player.addUnitActivationListener(() -> {
            events.add("playerActivatedInNewCell");

            assertSame(blockCell, player.owner());
            assertSame(player, blockCell.getUnit(Player.class));
            assertSame(blockDestination, block.owner());
        });

        blockCell.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                if (e.getUnit() == player) {
                    events.add("playerPlaced");

                    assertSame(blockCell, player.owner());
                    assertSame(player, blockCell.getUnit(Player.class));
                    assertSame(blockDestination, block.owner());
                }
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                // уже проверено первым listener-ом на этой же клетке
            }
        });

        player.addViewPlayerMovementListener(() -> {
            events.add("playerMoved");

            assertSame(blockCell, player.owner());
            assertSame(player, blockCell.getUnit(Player.class));
            assertSame(blockDestination, block.owner());
            assertNull(playerCell.getUnit(Player.class));
        });

        // Снимок ДО цепочки событий.
        assertSame(playerCell, player.owner());
        assertSame(blockCell, block.owner());
        assertTrue(blockDestination.isEmpty());

        assertTrue(player.moveTo(Direction.EAST));

        assertEquals(
                List.of(
                        "blockExtracted",
                        "blockActivatedInNewCell",
                        "blockPlaced",
                        "ironBlockMoved",
                        "playerExtracted",
                        "playerActivatedInNewCell",
                        "playerPlaced",
                        "playerMoved"
                ),
                events
        );

        // Снимок ПОСЛЕ цепочки.
        assertSame(blockCell, player.owner());
        assertSame(blockDestination, block.owner());
        assertNull(playerCell.getUnit(Player.class));
        assertSame(player, blockCell.getUnit(Player.class));
        assertSame(block, blockDestination.getUnit(IronBlock.class));
    }

    @Test
    void playerCollectsLastExitScore_exitModelListenerRunsBeforeVisualScoreListener_thenPlayerReachesExit() {
        Gamefield field = new Gamefield(1, 2);
        Cell playerCell = field.getCell(0, 0);
        Cell exitCell = field.getCell(1, 0);

        Player player = new Player();
        Exit exit = new Exit();
        ExitScore score = new ExitScore();

        assertTrue(playerCell.putUnit(player));
        assertTrue(exitCell.putUnit(exit));
        assertTrue(exitCell.putUnit(score));

        exit.deactivate();
        exit.addExitScore(score);

        // Сначала вычислительная модель: Exit обновляется раньше визуального listener-а.
        score.addModelExitScoreListener(exit.getExitScoreListener());

        List<String> events = new ArrayList<>();

        exitCell.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                if (e.getUnit() == player) {
                    events.add("playerPlacedIntoExitCell");

                    assertSame(exitCell, player.owner());
                    assertSame(player, exitCell.getUnit(Player.class));
                    assertNull(exitCell.getUnit(ExitScore.class));
                    assertTrue(exit.isActive());
                }
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                if (e.getUnit() == score) {
                    events.add("scoreExtracted");

                    // Снимок внутри destroy(): score уже вынут из клетки,
                    // но еще не помечен destroyed.
                    assertNull(score.owner());
                    assertNull(exitCell.getUnit(ExitScore.class));
                    assertFalse(score.isDestroyed());
                    assertTrue(score.isActive());

                    // Exit еще не обработал scoreCollected.
                    assertFalse(exit.isActive());
                    assertEquals(List.of(score), exit.getLeftScores());
                }
            }
        });

        exit.addUnitActivationListener(() -> {
            events.add("exitActivatedByScoreCollection");

            assertTrue(exit.isActive());
            assertTrue(exit.getLeftScores().isEmpty());
            assertTrue(score.isDestroyed());
            assertNull(exitCell.getUnit(ExitScore.class));
            assertSame(playerCell, player.owner());
        });

        // Затем визуальное отображение: оно видит уже обновленную вычислительную модель.
        score.addViewExitScoreListener(collectedScore -> {
            events.add("scoreCollectedVisual");

            assertSame(score, collectedScore);
            assertTrue(score.isDestroyed());
            assertNull(score.owner());
            assertNull(exitCell.getUnit(ExitScore.class));
            assertTrue(exit.isActive());
            assertTrue(exit.getLeftScores().isEmpty());
            assertSame(playerCell, player.owner());
        });

        playerCell.addCellActionListener(new CellActionListener() {
            @Override
            public void unitPlaced(CellActionEvent e) {
                fail("playerCell не должна получать unitPlaced");
            }

            @Override
            public void unitExtracted(CellActionEvent e) {
                if (e.getUnit() == player) {
                    events.add("playerExtracted");

                    assertNull(player.owner());
                    assertNull(playerCell.getUnit(Player.class));
                    assertNull(exitCell.getUnit(Player.class));
                    assertTrue(exit.isActive());
                    assertTrue(score.isDestroyed());
                }
            }
        });

        player.addUnitActivationListener(() -> {
            events.add("playerActivatedInExitCell");

            assertSame(exitCell, player.owner());
            assertSame(player, exitCell.getUnit(Player.class));
            assertTrue(exit.isActive());
            assertNull(exitCell.getUnit(ExitScore.class));
        });

        player.addViewPlayerMovementListener(() -> {
            events.add("playerMoved");

            assertSame(exitCell, player.owner());
            assertSame(player, exitCell.getUnit(Player.class));
            assertTrue(exit.isActive());
            assertNull(exitCell.getUnit(ExitScore.class));
        });

        player.addPlayerReachListener(new PlayerReachListener() {
            @Override
            public void playerInLava() {
                fail("Лавы в сценарии нет");
            }

            @Override
            public void playerInWall() {
                fail("Стены в сценарии нет");
            }

            @Override
            public void playerInExit() {
                events.add("playerInExit");

                assertSame(exitCell, player.owner());
                assertSame(player, exitCell.getUnit(Player.class));
                assertTrue(exit.isActive());
                assertTrue(exit.getLeftScores().isEmpty());
            }
        });

        // Снимок ДО цепочки.
        assertSame(playerCell, player.owner());
        assertSame(exit, exitCell.getUnit(Exit.class));
        assertSame(score, exitCell.getUnit(ExitScore.class));
        assertFalse(exit.isActive());
        assertEquals(List.of(score), exit.getLeftScores());

        assertTrue(player.moveTo(Direction.EAST));

        assertEquals(
                List.of(
                        "scoreExtracted",
                        "exitActivatedByScoreCollection",
                        "scoreCollectedVisual",
                        "playerExtracted",
                        "playerActivatedInExitCell",
                        "playerPlacedIntoExitCell",
                        "playerMoved",
                        "playerInExit"
                ),
                events
        );

        // Снимок ПОСЛЕ цепочки.
        assertSame(exitCell, player.owner());
        assertSame(player, exitCell.getUnit(Player.class));
        assertSame(exit, exitCell.getUnit(Exit.class));
        assertNull(exitCell.getUnit(ExitScore.class));
        assertTrue(exit.isActive());
        assertTrue(exit.getLeftScores().isEmpty());
        assertTrue(score.isDestroyed());
    }
}
