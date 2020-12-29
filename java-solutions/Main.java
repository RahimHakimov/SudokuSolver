import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Rakhim Khakimov (ramhakimov@niuitmo.ru)
 */

public class Main {
    public static Scanner in;

    private static boolean isCharCorrect(char c) {
        return (c <= '9' && c >= '0');
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
                        board[i][j] = cur.charAt(j) - '0';
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
                System.out.print(board[i][j] + "");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        in = new Scanner(System.in);
        System.out.println("Пожалуйста введите доску Судоку(9 строк), каждая строка должна соответствовать 0, если эта клетка пуста, либо число от 1 до 9 включительно.");
        int[][] board = inputBoard();
        if (!checkIsBoardCorrect(board) || !fillBoard(board)) {
            System.out.println("Эту доску нельзя заполнить правильно");
            return;
        }
        printBoard(board);
    }

    private static boolean fillBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    for (int value : possibleValues(i, j)) {
                        board[i][j] = value;
                        if (checkIsBoardCorrect(board) && fillBoard(board)) {
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
        List<Integer> result = new LinkedList<Integer>();
        if (board[x][y] != 0) {
             result.add(board[x][y]);
            return result;
        }

        boolean[] check = new boolean[10];
        for (int i = 0; i<10; i++) {
            check[board[x][i]] = true;
            check[board[i][y]] = true;
        }

        

        for (int i = 1; i<10; i++) {
            if (!check[i]) {
                result.add(i);
            }
        }
        return result;
    }
}

/*TEST
530070000
600195000
098000060
800060003
400803001
700020006
060000280
000419005
000080079
 */