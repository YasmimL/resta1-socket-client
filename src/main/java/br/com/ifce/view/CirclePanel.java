package br.com.ifce.view;

import br.com.ifce.model.Circle;
import br.com.ifce.model.Message;
import br.com.ifce.model.Movement;
import br.com.ifce.model.enums.MessageType;
import br.com.ifce.service.IntegrationService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;

public class CirclePanel extends JPanel implements DragGestureListener, DragSourceListener {
    private final IntegrationService service;

    private final Circle circle;

    @Getter
    private final Color initialColor = new Color(183, 28, 28);

    private Color color = this.initialColor;

    private boolean draw = true;

    protected final DataFlavor dataFlavor = new DataFlavor(Circle.class, Circle.class.getSimpleName());

    public CirclePanel(Circle circle) {
        this.service = IntegrationService.getInstance();
        this.circle = circle;

        DragSource ds = new DragSource();
        ds.addDragSourceListener(this);
        ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);

        new DropTargetListener(this);
    }

    public void setColor(Color color) {
        this.color = color;
        repaint();
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
        repaint();
    }

    public void makeSpotFree() {
        this.setColor(new Color(189, 189, 189));
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void makeSpotBusy() {
        this.setColor(this.initialColor);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(70, 70);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.draw) {
            g.setColor(color);
            g.fillOval(0, 0, getWidth(), getHeight());
        }
    }

    public int getValue() {
        return this.circle.getValue();
    }

    public void setValue(int value) {
        this.circle.setValue(value);
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {
        if (!this.service.isPlayerTurn()) return;

        CirclePanel panel = (CirclePanel) event.getComponent();
        if (panel.circle.getValue() == 0) return;
        panel.setColor(new Color(229, 115, 115));

        Cursor cursor = null;
        if (event.getDragAction() == DnDConstants.ACTION_COPY) {
            cursor = DragSource.DefaultCopyDrop;
        }

        event.startDrag(cursor, new TransferableCircle(panel.circle));
    }

    public void sendMessage(Message<?> message) {
        this.service.send(message);
    }

    @Override
    public void dragEnter(DragSourceDragEvent event) {
    }

    @Override
    public void dragOver(DragSourceDragEvent event) {
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent event) {
    }

    @Override
    public void dragExit(DragSourceEvent dse) {
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent event) {
        if (!event.getDropSuccess()) this.setColor(this.initialColor);
    }

    @AllArgsConstructor
    private class TransferableCircle implements Transferable {

        private final Circle circle;

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{dataFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(dataFlavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(dataFlavor))
                return this.circle;
            else
                throw new UnsupportedFlavorException(flavor);
        }
    }

    private class DropTargetListener extends DropTargetAdapter {
        private final CirclePanel panel;

        public DropTargetListener(CirclePanel panel) {
            this.panel = panel;
            new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        }

        @Override
        public void drop(DropTargetDropEvent event) {
            try {
                Transferable transferable = event.getTransferable();
                Circle circle = (Circle) transferable.getTransferData(dataFlavor);
                if (event.isDataFlavorSupported(dataFlavor)) {
                    event.acceptDrop(DnDConstants.ACTION_COPY);

                    var movement = new Movement(
                            new int[]{circle.getRow(), circle.getColumn()},
                            new int[]{this.panel.circle.getRow(), this.panel.circle.getColumn()}
                    );

                    this.panel.sendMessage(new Message<>(
                            MessageType.MOVEMENT,
                            movement
                    ));

                    event.dropComplete(true);

                    return;
                }
                event.rejectDrop();
            } catch (Exception e) {
                e.printStackTrace();
                event.rejectDrop();
            }
        }
    }
}
