package br.com.ifce.view;

import br.com.ifce.model.ChatMessage;
import br.com.ifce.model.Circle;
import br.com.ifce.model.Movement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

public class MainView {
    private final JFrame frame;

    private CirclePanel[][] circlePanels;

    private JTextPane statusPane;

    private JPanel chatPanel;

    public MainView() {
        this.frame = new JFrame();
        this.frame.setSize(1000, 1000);
        this.frame.setLayout(new BorderLayout(20, 15));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setTitle(String title) {
        this.frame.setTitle(title);
    }

    public void renderStatusPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        this.statusPane = new JTextPane();
        this.statusPane.setEditable(false);
        this.statusPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 60));
        this.statusPane.setForeground(new Color(183, 28, 28));

        StyledDocument doc = this.statusPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        panel.add(this.statusPane, BorderLayout.CENTER);

        this.frame.add(panel, BorderLayout.NORTH);
    }

    public void setStatusText(String text) {
        this.statusPane.setText(text);
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
        boardPanel.add(gridPanel);

        this.frame.add(boardPanel, BorderLayout.CENTER);
    }

    public void renderChatPanel() {
        this.chatPanel = new JPanel(new GridBagLayout());
        this.chatPanel.setLayout(new BoxLayout(this.chatPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(this.chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        scrollPane.setBorder(new EmptyBorder(0, 200, 20, 200));

        this.frame.add(scrollPane, BorderLayout.SOUTH);
    }

    public void addChatMessage(ChatMessage message) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel header = new JPanel();

        JTextPane sender = new JTextPane();
        sender.setEditable(false);
        sender.setBackground(Color.BLACK);
        sender.setOpaque(false);
        sender.setText(message.getSender());
        header.add(sender, BorderLayout.WEST);

        JTextPane time = new JTextPane();
        time.setEditable(false);
        time.setText(message.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        header.add(time, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        JTextPane body = new JTextPane();
        body.setEditable(false);
        body.setText(message.getText());
        panel.add(body, BorderLayout.SOUTH);

        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1;
//        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        this.chatPanel.add(panel, constraints);
        this.chatPanel.validate();
        this.chatPanel.repaint();

        SwingUtilities.invokeLater(() -> panel.scrollRectToVisible(panel.getBounds()));
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
