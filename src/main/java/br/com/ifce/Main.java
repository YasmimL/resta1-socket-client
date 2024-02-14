package br.com.ifce;

import br.com.ifce.view.MainView;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
//        var controller = new MainViewController(new SocketIntegrationService());


        var view = new MainView();
        view.renderBoardPanel(getBoard());
        view.show();
    }

    // TODO: TO BE REMOVED!!!
    private static int[][] getBoard() {
        final int rows = 7;
        final int columns = 7;
        final int[][] board = new int[rows][columns];
        final var middleSpot = 3;
        final var invalidSpots = new int[]{0, 1, 5, 6};

        IntStream.range(0, rows).forEach(row ->
                IntStream.range(0, columns).forEach(column -> board[row][column] = 1)
        );

        board[middleSpot][middleSpot] = 0;

        for (int row : invalidSpots) {
            for (int column : invalidSpots) {
                board[row][column] = -1;
            }
        }

        return board;
    }
}