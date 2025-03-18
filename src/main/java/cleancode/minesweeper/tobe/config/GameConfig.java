package cleancode.minesweeper.tobe.config;

import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;

public class GameConfig {

    private final GameLevel gameLevel;
    private final ConsoleInputHandler inputHandler;
    private final ConsoleOutputHandler outputHandler;

    public GameConfig(GameLevel gameLevel, ConsoleInputHandler inputHandler, ConsoleOutputHandler outputHandler) {
        this.gameLevel = gameLevel;
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    public GameLevel getGameLevel() {
        return gameLevel;
    }

    public ConsoleInputHandler getInputHandler() {
        return inputHandler;
    }

    public ConsoleOutputHandler getOutputHandler() {
        return outputHandler;
    }
}
