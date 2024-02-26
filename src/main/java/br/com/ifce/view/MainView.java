package br.com.ifce.view;

import br.com.ifce.model.ChatMessage;
import br.com.ifce.model.Circle;
import br.com.ifce.model.Message;
import br.com.ifce.model.Movement;
import br.com.ifce.model.enums.MessageType;
import br.com.ifce.service.IntegrationService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

public class MainView {
    private final JFrame frame;

    private CirclePanel[][] circlePanels;

    private JTextPane statusPane;

    private JPanel messageListPanel;

    private JButton passTurnButton;

    private JButton giveUpButton;

    private JButton restartButton;

    public MainView() {
        this.frame = new JFrame();
        this.frame.setSize(1000, 800);
        JPanel contentPanel = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        contentPanel.setBorder(padding);
        this.frame.setContentPane(contentPanel);
        this.frame.setLayout(new BorderLayout(20, 15));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setTitle(String title) {
        this.frame.setTitle(title);
    }

    public void renderStatusPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 30, 0), BorderFactory.createLineBorder(Color.GRAY)));

        this.statusPane = new JTextPane();
        this.statusPane.setEditable(false);
        this.statusPane.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 60));
        this.statusPane.setForeground(new Color(1, 87, 155));

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

        this.frame.add(boardPanel, BorderLayout.WEST);
    }

    public JPanel renderChatPanel() {
        final int width = 350;
        final int height = 400;

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setPreferredSize(new Dimension(width, height));

        JTextPane header = new JTextPane();
        header.setEditable(false);
        header.setBackground(new Color(1, 87, 155));
        header.setForeground(Color.WHITE);
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        header.setText("Chat");
        StyledDocument doc = header.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        chatPanel.add(header, BorderLayout.NORTH);

        this.messageListPanel = new JPanel();
        this.messageListPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.weighty = Integer.MAX_VALUE;
        this.messageListPanel.add(new Label(), constraints);

        JScrollPane scrollPane = new JScrollPane(this.messageListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(width, 200));
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        final int characterLimit = 27;
        JPanel formPanel = new JPanel(new BorderLayout());
        JTextField textField = new JPlaceholderTextField("Digite sua mensagem...");
        textField.setPreferredSize(new Dimension(250, 25));
        textField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent event) {
                if (textField.getText().length() >= characterLimit) event.consume();
            }
        });
        formPanel.add(textField, BorderLayout.WEST);

        JButton button = new JButton("Enviar");
        button.setPreferredSize(new Dimension(100, textField.getPreferredSize().height));
        button.addActionListener(event -> {
            IntegrationService.getInstance().send(
                    new Message<>(
                            MessageType.CHAT,
                            textField.getText()
                    )
            );
            textField.setText("");
        });
        formPanel.add(button, BorderLayout.EAST);

        chatPanel.add(formPanel, BorderLayout.SOUTH);

        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER));
        container.setBorder(new EmptyBorder(0, 0, 20, 0));
        container.add(chatPanel);

        return chatPanel;
    }

    public JPanel renderActions() {
        IntegrationService service = IntegrationService.getInstance();

        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new GridLayout(3, 1, 10, 10));
        actionsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        this.passTurnButton = new JButton("Passar a vez");
        this.passTurnButton.setEnabled(!service.isGameFinished() && service.isPlayerTurn());
        this.passTurnButton.addActionListener(event -> service.send(new Message<>(MessageType.PASS_TURN, null)));
        actionsPanel.add(this.passTurnButton);

        this.giveUpButton = new JButton("Desistir");
        this.giveUpButton.setEnabled(!service.isGameFinished());
        this.giveUpButton.addActionListener(event -> service.send(new Message<>(MessageType.GIVE_UP, null)));
        actionsPanel.add(this.giveUpButton);

        this.restartButton = new JButton("Jogar de novo");
        this.restartButton.setEnabled(service.isGameFinished());
        this.restartButton.addActionListener(event -> service.send(new Message<>(MessageType.RESTART_GAME, null)));
        actionsPanel.add(this.restartButton);

        return actionsPanel;
    }

    public void updateActionButtons() {
        IntegrationService service = IntegrationService.getInstance();
        this.passTurnButton.setEnabled(!service.isGameFinished() && service.isPlayerTurn());
        this.giveUpButton.setEnabled(!service.isGameFinished());
        this.restartButton.setEnabled(service.isGameFinished());
    }

    public void renderSideMenu() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(this.renderChatPanel());
        container.add(this.renderActions());
        this.frame.add(container, BorderLayout.EAST);
    }

    public void addChatMessage(ChatMessage message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(129, 212, 250));

        JTextPane sender = new JTextPane();
        sender.setEditable(false);
        sender.setBackground(Color.BLACK);
        sender.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        sender.setOpaque(false);
        sender.setText(message.getSender());
        header.add(sender, BorderLayout.WEST);

        JTextPane time = new JTextPane();
        time.setBackground(new Color(129, 212, 250));
        time.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        time.setEditable(false);
        time.setText(message.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        header.add(time, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        JTextPane body = new JTextPane();
        body.setEditable(false);
        body.setText(message.getText());
        panel.add(body, BorderLayout.SOUTH);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.weightx = 1;
        constraints.weighty = 1;

        this.messageListPanel.add(panel, constraints);
        this.messageListPanel.validate();
        this.messageListPanel.repaint();

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
