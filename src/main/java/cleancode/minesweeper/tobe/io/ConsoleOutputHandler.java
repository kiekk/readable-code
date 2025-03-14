package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.GameException;

import java.util.List;
import java.util.stream.IntStream;

import static cleancode.minesweeper.tobe.GameApplication.ONE;
import static cleancode.minesweeper.tobe.GameApplication.ZERO;

public class ConsoleOutputHandler implements OutputHandler {

    @Override
    public void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void showBoard(GameBoard gameBoard) {
        String alphabets = generateColAlphabets(gameBoard);

        System.out.println("    " + alphabets);
        for (int row = ZERO; row < gameBoard.getRowSize(); row++) {
            System.out.printf("%2d  ", row + ONE);
            for (int col = ZERO; col < gameBoard.getColSize(); col++) {
                System.out.print(gameBoard.getSign(row, col) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private String generateColAlphabets(GameBoard gameBoard) {
        List<String> alphabets = IntStream.range(0, gameBoard.getColSize())
                .mapToObj(index -> (char) ('a' + index))
                .map(Object::toString)
                .toList();
        String joiningAlphabets = String.join(" ", alphabets);
        return joiningAlphabets;
    }

    @Override
    public void showGameWinningComment() {
        System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
    }

    @Override
    public void showGameLosingComment() {
        System.out.println("지뢰를 밟았습니다. GAME OVER!");
    }

    @Override
    public void showCommentForSelectingCell() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
    }

    @Override
    public void showCommentForUserAction() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
    }

    @Override
    public void showExceptionMessage(GameException e) {
        System.out.println(e.getMessage());
    }

    @Override
    public void showSimpleMessage(String message) {
        System.out.println(message);
    }
}
