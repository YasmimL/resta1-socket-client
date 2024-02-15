package br.com.ifce.view;

import br.com.ifce.model.Circle;

import javax.swing.*;
import java.awt.*;

public class MainView {
    private final JFrame frame;

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
                CirclePanel circlePanel = new CirclePanel(new Circle(i, j));
                if (board[i][j] == -1) circlePanel.setDraw(false);
                if (board[i][j] == 0) circlePanel.setColor(new Color(189, 189, 189));
                if (board[i][j] == 1) circlePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                circlePanels[i][j] = circlePanel;
                gridPanel.add(circlePanel);
            }
        }

        var boardPanel = new JPanel();
        boardPanel.setPreferredSize(new Dimension(300, 300));
        boardPanel.add(gridPanel);

        this.frame.add(boardPanel);
    }

    public void show() {
        this.frame.setVisible(true);
    }
}
