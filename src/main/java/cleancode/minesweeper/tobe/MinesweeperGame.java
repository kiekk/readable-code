package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final int LAND_MINE_COUNT = 10;

    public static final String FLAG_SIGN = "⚑";
    public static final String LAND_MINE_SIGN = "☼";
    public static final String CLOSED_CELL_SIGN = "□";
    public static final String OPENED_CELL_SIGN = "■";
    public static final Scanner SCANNER = new Scanner(System.in);

    private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COL_SIZE];

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        initializeGame();

        while (true) {
            try {
                showBoard();

                if (doesUserWinTheGame()) {
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doesUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("알 수 없는 오류가 발생했습니다.");
            }
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
            checkIfGameIsOver();
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                BOARD[selectedRowIndex][selectedColIndex] = LAND_MINE_SIGN;
                changeGameStatusToLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }

        System.out.println("잘못된 번호를 선택하셨습니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -ONE;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return LAND_MINES[selectedRowIndex][selectedColIndex];
    }

    private static boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(ONE);
        return convertRowFrom(cellInputRow);
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(ZERO);
        return convertColFrom(cellInputCol);
    }

    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -ONE;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == ONE;
    }

    private static void checkIfGameIsOver() {
        boolean isAllOpened = isAllCellOpened();
        if (isAllOpened) {
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = ONE;
    }

    private static boolean isAllCellOpened() {
        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                .noneMatch(CLOSED_CELL_SIGN::equals);
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - ONE;

        if (rowIndex >= BOARD_ROW_SIZE) {
            throw new AppException("잘못된 입력입니다.");
        }

        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                throw new AppException("잘못된 입력입니다.");
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = ZERO; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + ONE);
            for (int col = ZERO; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = ZERO; row < BOARD_ROW_SIZE; row++) {
            for (int col = ZERO; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = CLOSED_CELL_SIGN;
            }
        }

        for (int i = ZERO; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            LAND_MINES[row][col] = true;
        }

        for (int row = ZERO; row < BOARD_ROW_SIZE; row++) {
            for (int col = ZERO; col < BOARD_COL_SIZE; col++) {
                if (isLandMineCell(row, col)) {
                    NEARBY_LAND_MINE_COUNTS[row][col] = ZERO;
                    continue;
                }

                NEARBY_LAND_MINE_COUNTS[row][col] = countNearbyLandMines(row, col);
            }
        }
    }

    private static int countNearbyLandMines(int row, int col) {
        int count = ZERO;
        if (row - ONE >= ZERO && col - ONE >= ZERO && isLandMineCell(row - ONE, col - ONE)) {
            count++;
        }
        if (row - ONE >= ZERO && isLandMineCell(row - ONE, col)) {
            count++;
        }
        if (row - ONE >= ZERO && col + ONE < BOARD_COL_SIZE && isLandMineCell(row - ONE, col + ONE)) {
            count++;
        }
        if (col - ONE >= ZERO && isLandMineCell(row, col - ONE)) {
            count++;
        }
        if (col + ONE < BOARD_COL_SIZE && isLandMineCell(row, col + ONE)) {
            count++;
        }
        if (row + ONE < BOARD_ROW_SIZE && col - ONE >= ZERO && isLandMineCell(row + ONE, col - ONE)) {
            count++;
        }
        if (row + ONE < BOARD_ROW_SIZE && isLandMineCell(row + ONE, col)) {
            count++;
        }
        if (row + ONE < BOARD_ROW_SIZE && col + ONE < BOARD_COL_SIZE && isLandMineCell(row + ONE, col + ONE)) {
            count++;
        }
        return count;
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < ZERO || row >= BOARD_ROW_SIZE || col < ZERO || col >= BOARD_COL_SIZE) {
            return;
        }

        if (!CLOSED_CELL_SIGN.equals(BOARD[row][col])) {
            return;
        }

        if (isLandMineCell(row, col)) {
            return;
        }

        if (NEARBY_LAND_MINE_COUNTS[row][col] != ZERO) {
            BOARD[row][col] = String.valueOf(NEARBY_LAND_MINE_COUNTS[row][col]);
            return;
        }

        BOARD[row][col] = OPENED_CELL_SIGN;

        open(row - ONE, col - ONE);
        open(row - ONE, col);
        open(row - ONE, col + ONE);
        open(row, col - ONE);
        open(row, col + ONE);
        open(row + ONE, col - ONE);
        open(row + ONE, col);
        open(row + ONE, col + ONE);
    }

}
