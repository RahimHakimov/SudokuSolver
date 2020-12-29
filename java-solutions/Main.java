import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Rakhim Khakimov (ramhakimov@niuitmo.ru)
 */

public class Main {
    public static Scanner in;

    private static boolean isCharCorrect(char c) {
        return (c <= '9' && c > '0') || c == '.';
    }

    private static boolean checkIsBoardCorrect(int[][] board) {
        return checkIsRowsCorrect(board) &&
                checkIsColumnsCorrect(board) &&
                checkIsBlockCorrect(board, 0, 0) &&
                checkIsBlockCorrect(board, 0, 3) &&
                checkIsBlockCorrect(board, 0, 6) &&
                checkIsBlockCorrect(board, 3, 0) &&
                checkIsBlockCorrect(board, 3, 3) &&
                checkIsBlockCorrect(board, 3, 6) &&
                checkIsBlockCorrect(board, 6, 0) &&
                checkIsBlockCorrect(board, 6, 3) &&
                checkIsBlockCorrect(board, 6, 6);
    }

    private static boolean checkIsRowsCorrect(int[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] check = new boolean[10];
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0 && check[board[i][j]]) {
                    return false;
                } else {
                    check[board[i][j]] = true;
                }
            }
        }
        return true;
    }

    private static boolean checkIsColumnsCorrect(int[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] check = new boolean[10];
            for (int j = 0; j < 9; j++) {
                if (board[j][i] != 0 && check[board[j][i]]) {
                    return false;
                } else {
                    check[board[j][i]] = true;
                }
            }
        }
        return true;
    }

    private static boolean checkIsBlockCorrect(int[][] board, int x, int y) {
        boolean[] check = new boolean[10];
        for (int i = x; i < x + 3; i++) {
            for (int j = y; j < y + 3; j++) {
                if (board[i][j] != 0 && check[board[i][j]]) {
                    return false;
                } else {
                    check[board[i][j]] = true;
                }
            }
        }

        return true;
    }

    private static int[][] inputBoard() {
        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            String cur = in.nextLine();
            while (true) {
                if (cur.length() != 9) {
                    System.out.println("Пожалуйста, проверьте входные данные, и попробуйте ввести это строку заново!");
                    cur = in.nextLine();
                } else {
                    for (int j = 0; j < 9; j++) {
                        if (!isCharCorrect(cur.charAt(j))) {
                            System.out.println("Пожалуйста, проверьте входные данные, и попробуйте ввести это строку заново!");
                            cur = in.nextLine();
                            break;
                        }
                    }

                    for (int j = 0; j < 9; j++) {
                        board[i][j] = (cur.charAt(j) != '.') ? cur.charAt(j) - '0' : 0;
                    }
                    break;
                }
            }
        }
        return board;
    }

    private static void printBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (j % 3 == 0) System.out.print("|");
                System.out.print((board[i][j] != 0 ? board[i][j] : ".") + "");
            }
            System.out.println();
            if ((i+1) % 3 == 0)
                System.out.println("____________");
        }
    }

    public static void main(String[] args) {
        in = new Scanner(System.in);
        System.out.println("Пожалуйста введите доску Судоку(9 строк), \nкаждая строка должна соответствовать '.', если эта клетка пуста,\n либо число от 1 до 9 включительно.\n(между символами не нужно ставить никаких символов)");
        int[][] board = inputBoard();
        if (!checkIsBoardCorrect(board) || !fillBoard(board)) {
            System.out.println("Эту доску нельзя заполнить правильно");
            return;
        }
        System.out.println("===========\nРезультат:");
        printBoard(board);
    }

    private static boolean fillBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    System.out.println("====================");
                    System.out.println("Подстановка элемента на позицию " + (i + 1) + " " + (j + 1));
                    for (int value : possibleValues(board, i, j)) {
                        board[i][j] = value;
                        System.out.println("Подставляем " + value);
                        printBoard(board);
                        if (fillBoard(board)) {
                            return true;
                        }
                        board[i][j] = 0;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static List<Integer> possibleValues(int[][] board, int x, int y) {
        List<Integer> result = new LinkedList<>();

        boolean[] check = new boolean[10];
        for (int i = 0; i < 9; i++) {
            check[board[x][i]] = true;
            check[board[i][y]] = true;
        }

        int blockX = blocksRow(x);
        int blockY = blocksColumn(y);

        for (int i = blockX; i < blockX + 3; i++) {
            for (int j = blockY; j < blockY + 3; j++) {
                check[board[i][j]] = true;
            }
        }

        for (int i = 1; i < 10; i++) {
            if (!check[i]) {
                result.add(i);
            }
        }
        return result;
    }

    private static int blocksRow(int x) {
        if (x >= 0 && x < 3)
            return 0;
        if (x >= 3 && x < 6)
            return 3;
        if (x >= 6 && x < 9)
            return 6;
        return -1;
    }

    private static int blocksColumn(int y) {
        if (y >= 0 && y < 3)
            return 0;
        if (y >= 3 && y < 6)
            return 3;
        if (y >= 6 && y < 9)
            return 6;
        return -1;
    }
}

/*TEST
53..7....
6..195...
.98....6.
8...6...3
4..8.3..1
7...2...6
.6....28.
...419..5
....8..79
 */