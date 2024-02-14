package br.com.ifce.view;

import javax.swing.*;
import java.awt.*;

public class Circle extends JPanel {
    private Color color = Color.BLUE;

    public Circle() {
//        addMouseListener(new MouseAdapter(){
//            public void mousePressed(MouseEvent e) {
//                if (isDraw()) {
//                    setDraw(false);
//                } else {
//                    setDraw(true);
//                }
//            }
//        });
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(75, 75);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval(0, 0, getWidth(), getHeight());
    }
}
