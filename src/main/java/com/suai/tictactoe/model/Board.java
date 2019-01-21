package com.suai.tictactoe.model;

public class Board {
    private char[][] board = new char[10][10];

    private boolean checkDiagonal(char symbol) {
        int numTrueToRight;
        int numTrueToLeft;
        int numTrueToRightMirror;
        int numTrueToLeftMirror;
        for (int j = 4; j < 10; j++) {
            numTrueToLeft = 0;
            numTrueToRight = 0;
            numTrueToRightMirror = 0;
            numTrueToLeftMirror = 0;
            for (int i = 0; i < j; i++) {
                if (board[i][i] == symbol) {
                    numTrueToRight += 1;
                } else {
                    numTrueToRight = 0;
                }
                if (board[j - i - 1][i] == symbol) {
                    numTrueToLeft += 1;
                } else {
                    numTrueToLeft = 0;
                }
                if (board[9 - i][9 - i] == symbol) {
                    numTrueToRightMirror += 1;
                } else {
                    numTrueToRightMirror = 0;
                }
                if (board[9  - (j - i - 1)][9 - i] == symbol) {
                    numTrueToLeftMirror += 1;
                } else {
                    numTrueToLeftMirror = 0;
                }
            }

            if (numTrueToLeft == 5 || numTrueToRight == 5 || numTrueToRightMirror == 5 || numTrueToLeftMirror == 5){
                return true;
            }
        }

        return false;
    }

    private boolean checkLanes(char symb) {
        int numTrueCols;
        int numTrueRosw;
        for (int col = 0; col < 10; col++) {
            numTrueCols = 0;
            numTrueRosw = 0;
            for (int row = 0; row < 10; row++) {
                if (board[col][row] == symb){
                    numTrueCols += 1;
                }
                if (board[row][col] == symb){
                    numTrueRosw += 1;
                }
            }

            if (numTrueCols == 5 || numTrueRosw == 5) {
                return true;
            }
        }

        return false;
    }

    public int checkWin() { // -1 - никто, 0 - ничья, 1 - X, 2 - Y
        if (checkDiagonal('X') || checkLanes('X')) {
            return 1;
        }
        if (checkDiagonal('O') || checkLanes('O')) {
            return 2;
        }
        return -1;
    }

    public boolean canSet(int x, int y) {
        return board[x][y] != 'X' && board[x][y] != 'O';
    }

    public void setMark(int x, int y, char mark) {
        board[x][y] = mark;
    }

    public char getMark(int x, int y) {
        return board[x][y];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n   1  2  3  4  5  6  7  8  9  10\n");
        for (int i = 0; i < 10; i++) {
            if (i != 9) {
                sb.append(i + 1).append("  ");
            } else {
                sb.append(i + 1).append(" ");
            }
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == 'X' || board[i][j] == 'O') {
                    sb.append(board[i][j]).append("  ");
                } else {
                    sb.append("*").append("  ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
