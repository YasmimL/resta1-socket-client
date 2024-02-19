package br.com.ifce.view;

import br.com.ifce.model.Circle;
import br.com.ifce.model.Movement;

import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;

public class MainView {
    private final JFrame frame;

    private CirclePanel[][] circlePanels;

    public MainView() {
        this.frame = new JFrame();
        this.frame.setSize(1000, 1000);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setTitle("Test Frame");
    }

    public void renderBoardPanel(int[][] board) {
        var circlePanels = new CirclePanel[board.length][board.length];
        var gridPanel = new JPanel(new GridLayout(board.length, board.length, 10, 10));

        for (int i = 0; i < circlePanels.length; i++) {
            for (int j = 0; j < circlePanels[i].length; j++) {
                CirclePanel circlePanel = new CirclePanel(new Circle(i, j, board[i][j]));
                if (board[i][j] == -1) circlePanel.setDraw(false);
                if (board[i][j] == 0) circlePanel.makeSpotFree();
                if (board[i][j] == 1) circlePanel.makeSpotBusy();
                circlePanels[i][j] = circlePanel;
                gridPanel.add(circlePanel);
            }
        }

        this.circlePanels = circlePanels;

        JPanel boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(300, 300));
        boardPanel.add(gridPanel);

        this.frame.add(boardPanel);
    }

    public void updateBoard(int[][] board) {
        IntStream.range(0, board.length).forEach(row ->
                IntStream.range(0, board.length).forEach(column -> {
                            var circlePanel = this.circlePanels[row][column];
                            if (circlePanel.getValue() == board[row][column]) return;
                            circlePanel.setValue(board[row][column]);
                            if (circlePanel.getValue() == 0) circlePanel.makeSpotFree();
                            if (circlePanel.getValue() == 1) circlePanel.makeSpotBusy();
                        }
                )
        );
    }

    public void show() {
        this.frame.setVisible(true);
    }

    public void undoMove(Movement movement) {
        var row = movement.getSource()[0];
        var column = movement.getSource()[1];
        var panel = this.circlePanels[row][column];
        panel.setColor(panel.getInitialColor());
    }
}
