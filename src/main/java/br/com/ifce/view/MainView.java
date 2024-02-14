package br.com.ifce.view;

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
        var circlePanels = new Circle[board.length][board.length];
        var gridPanel = new JPanel(new GridLayout(board.length, board.length, 10, 10));


        for (int i = 0; i < circlePanels.length; i++) {
            for (int j = 0; j < circlePanels[i].length; j++) {
                Circle circle = new Circle();
                circlePanels[i][j] = circle;
                gridPanel.add(circle);
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
