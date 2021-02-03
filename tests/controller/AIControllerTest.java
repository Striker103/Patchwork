package controller;

import model.*;
import org.junit.Before;
import org.junit.Test;
import view.aui.ErrorAUI;
import view.aui.HintAUI;
import view.aui.LogAUI;
import view.aui.TurnAUI;

import static org.junit.Assert.assertTrue;

public class AIControllerTest {

    private final MainController mainController = new MainController();

    private final GamePreparationController gamePreparationController = mainController.getGamePreparationController();

    private final AIController aiController = mainController.getAIController();

    private final GameController gameController = mainController.getGameController();

    private final Tuple<String, PlayerType> humanPlayer = new Tuple<>("Adria", PlayerType.HUMAN);
    private final Tuple<String, PlayerType> easyAI = new Tuple<>("EasyAI", PlayerType.AI_EASY);
    private final Tuple<String, PlayerType> mediumAI = new Tuple<>("MediumAI", PlayerType.AI_MEDIUM);
    private final Tuple<String, PlayerType> hardAI = new Tuple<>("HardAI", PlayerType.AI_HARD);

    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> humanVsEasyAI = new Tuple<>(humanPlayer, easyAI);
    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> easyAIVsMediumAI = new Tuple<>(easyAI, mediumAI);
    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> mediumAIVsHardAI = new Tuple<>(mediumAI, hardAI);
    private final Tuple<Tuple<String, PlayerType>, Tuple<String, PlayerType>> HardAIVsEasyAI = new Tuple<>(hardAI, easyAI);

    private final DummyAUI dummyAUI = new DummyAUI();

    static class DummyAUI implements ErrorAUI, HintAUI, LogAUI, TurnAUI {
        public boolean error = false;

        @Override
        public void showError(String message) {
            error = true;
        }
        @Override
        public void showHintTakePatch(Patch patch, boolean[][] placing) {
            error = true;
        }

        @Override
        public void showHintAdvance() {
        }

        @Override
        public void updateLog(String log) {
            System.out.println(log);
        }

        @Override
        public void triggerPlayerTurn() {

        }

        @Override
        public void trigger1x1Placement() {

        }

        @Override
        public void reTriggerPatchPlacement() {

        }

        @Override
        public void updatePatches() {

        }

        @Override
        public void moveToken(String name, int time) {

        }
    }

    @Before
    public void setUp() {
      mainController.setErrorAUI(dummyAUI);
      mainController.setHintAUI(dummyAUI);
      mainController.setLogAUI(dummyAUI);
      mainController.setTurnAUI(dummyAUI);
    }

    /**
     * Test if doTurn runs trough if the current player is a human
     */
    @Test
    public void testDoTurnWithHuman() {
        gamePreparationController.startGame(humanVsEasyAI, null, 5,false);
        gameController.cloneGameState();

        aiController.doTurn();
    }

    /**
     * Test if error is shown if the current player is a human
     */
    @Test
    public void testDoTurnWithHumanShowError() {
        gamePreparationController.startGame(humanVsEasyAI, null, 5,false);
        gameController.cloneGameState();

        aiController.doTurn();
        assertTrue(dummyAUI.error);
    }

    /**
     * Test if doTurn runs trough if the current player is an EasyAI
     */
    @Test
    public void testDoTurnWithEasyAI() {
        gamePreparationController.startGame(easyAIVsMediumAI, null, 5,false);
        gameController.cloneGameState();

        aiController.doTurn();
    }

    /**
     * Test if doTurn runs trough if the current player is a MediumAI
     */
    @Test
    public void testDoTurnWithMediumAI() {
        gamePreparationController.startGame(mediumAIVsHardAI, null, 5,false);
        gameController.cloneGameState();

        aiController.doTurn();
    }

    /**
     * Test if doTurn runs trough if the current player is a HardAI
     */
    @Test
    public void testDoTurnWithHardAI() {
        gamePreparationController.startGame(HardAIVsEasyAI, null, 5,false);
        gameController.cloneGameState();
        long startTime = System.currentTimeMillis();
        aiController.doTurn();
        assertTrue(startTime-System.currentTimeMillis()<10000);
    }

    /**
     * Test if calculateHint runs trough if the current player is a human
     */
    @Test
    public void testCalculateHintWithHuman() {
        gamePreparationController.startGame(humanVsEasyAI, null, 5,false);
        gameController.cloneGameState();

        aiController.calculateHint();
    }

    /**
     * Test if calculateHint runs trough if the current player is an EasyAI
     */
    @Test
    public void testCalculateHintWithEasyAI() {
        gamePreparationController.startGame(easyAIVsMediumAI, null, 5,false);
        gameController.cloneGameState();

        aiController.calculateHint();
    }

    //TODO more tests will follow

}