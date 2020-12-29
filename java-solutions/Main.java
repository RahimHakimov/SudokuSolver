import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Rakhim Khakimov (ramhakimov@niuitmo.ru)
 */

public class Main {
    public static Scanner in;
    public static boolean log;

    private static boolean isCharCorrect(char c) {
        return (c <= '9' && c > '0') || c == '.';
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
            if ((i + 1) % 3 == 0)
                System.out.println("____________");
        }
    }

    public static void main(String[] args) {
        in = new Scanner(System.in);
        System.out.println("Пожалуйста введите доску Судоку(9 строк), \nкаждая строка должна состоять из 9 символов, \nкаждый из которых должен соответствовать '.', если эта клетка пуста,\nлибо число от 1 до 9 включительно.\n(между символами не нужно ставить никаких символов)");
        int[][] board = inputBoard();
        System.out.println("Хотите, чтобы выводилось состояние доски на каждом шаге?\n(0 - нет\n1 - да)");
        int isLog = in.nextInt();
        while (isLog != 0 && isLog != 1) {
            System.out.println("Хотите, чтобы выводилось состояние доски на каждом шаге?\n(0 - нет\n1 - да)");
            isLog = in.nextInt();
        }
        if (isLog == 1) log = true;
        if (!checkIsBoardCorrect(board) || !fillBoard(board)) {
            System.out.println("Эту доску нельзя заполнить правильно!");
            return;
        }
        System.out.println("===============================\nРезультат:");
        printBoard(board);
    }

    private static boolean fillBoard(int[][] board) {
        List<Integer> listOfPossible = new LinkedList<>();
        int minLength = 11;
        int indexI = 0, indexJ = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    List<Integer> temp = possibleValues(board, i, j);
                    if (temp.size() < minLength) {
                        minLength = temp.size();
                        listOfPossible = temp;
                        indexI = i;
                        indexJ = j;
                    }
                }
            }
        }

        if (board[indexI][indexJ] == 0) {
            if (log) {
                System.out.println("========================================");
                System.out.println("Клетка с минимальным числом оставшихся возможностей имеет индексы " + (indexI + 1) + " " + (indexJ + 1));
                System.out.println("Всего " + minLength + " возможностей заполнить эту клетку.");
            }
            for (int value : listOfPossible) {
                board[indexI][indexJ] = value;
                if (log) {
                    System.out.println("Подставляем " + value);
                    printBoard(board);
                }
                if (fillBoard(board)) {
                    return true;
                }
                board[indexI][indexJ] = 0;
            }
            return false;
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

        int blockX = blocksRowOrColumn(x);
        int blockY = blocksRowOrColumn(y);

        for (int i = blockX; i < blockX + 3; i++) {
            for (int j = blockY; j < blockY + 3; j++) {
                check[board[i][j]] = true;
            }
        }

        for (int i = 1; i < 10; i++) {
            if (!check[i])
                result.add(i);
        }
        return result;
    }

    private static int blocksRowOrColumn(int i) {
        return (i / 3) * 3;
    }

    private static boolean checkIsBoardCorrect(int[][] board) {
        boolean result = checkIsRowsAndColumnsCorrect(board);
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                result &= checkIsBlockCorrect(board, i, j);
            }
        }
        return result;
    }

    private static boolean checkIsRowsAndColumnsCorrect(int[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] checkRow = new boolean[10];
            boolean[] checkColumn = new boolean[10];
            for (int j = 0; j < 9; j++) {
                if ((board[i][j] != 0 && checkRow[board[i][j]]) ||
                        (board[j][i] != 0 && checkColumn[board[j][i]])) {
                    return false;
                } else {
                    checkRow[board[i][j]] = true;
                    checkColumn[board[j][i]] = true;
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
}

/*
TEST-1(взят из ВикипедиИ):
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