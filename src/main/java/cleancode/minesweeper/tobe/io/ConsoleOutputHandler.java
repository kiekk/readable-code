package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.GameException;
import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;
import cleancode.minesweeper.tobe.position.CellPosition;

import java.util.List;
import java.util.stream.IntStream;

import static cleancode.minesweeper.tobe.GameApplication.ONE;
import static cleancode.minesweeper.tobe.GameApplication.ZERO;

public class ConsoleOutputHandler implements OutputHandler {

    private static final String EMPTY_SIGN = "■";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String FLAG_SIGN = "⚑";
    private static final String UNCHECKED_SIGN = "□";

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
                CellPosition cellPosition = CellPosition.of(row, col);
                CellSnapshot snapshot = gameBoard.getSnapshot(cellPosition);
                String cellSign = decideCellSignFrom(snapshot);
                System.out.print(cellSign + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private String decideCellSignFrom(CellSnapshot snapshot) {
        CellSnapshotStatus status = snapshot.getStatus();

        switch (status) {
            case EMPTY -> {
                return EMPTY_SIGN;
            }
            case FLAG -> {
                return FLAG_SIGN;
            }
            case LAND_MINE -> {
                return LAND_MINE_SIGN;
            }
            case NUMBER -> {
                int nearbyLandMineCount = snapshot.getNearbyLandMineCount();
                return nearbyLandMineCount == ZERO ? EMPTY_SIGN : String.valueOf(nearbyLandMineCount);
            }
            case UNCHECKED -> {
                return UNCHECKED_SIGN;
            }
            default -> throw new IllegalArgumentException("확인할 수 없는 셀입니다.");
        }
    }

    private String generateColAlphabets(GameBoard gameBoard) {
        List<String> alphabets = IntStream.range(ZERO, gameBoard.getColSize())
                .mapToObj(index -> (char) ('a' + index))
                .map(Object::toString)
                .toList();
        return String.join(" ", alphabets);
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
