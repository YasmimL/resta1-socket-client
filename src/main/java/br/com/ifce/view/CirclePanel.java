package br.com.ifce.view;

import br.com.ifce.model.Circle;
import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;

public class CirclePanel extends JPanel implements DragGestureListener {
    private final Circle circle;

    private Color color = new Color(183, 28, 28);

    private boolean draw = true;

    protected final DataFlavor dataFlavor = new DataFlavor(Circle.class, Circle.class.getSimpleName());

    public CirclePanel(Circle circle) {
        this.circle = circle;

        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);

        new DropTargetListener(this);

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

    public void setDraw(boolean draw) {
        this.draw = draw;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(75, 75);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.draw) {
            g.setColor(color);
            g.fillOval(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {
        Cursor cursor = null;
        CirclePanel panel = (CirclePanel) event.getComponent();
        panel.setColor(new Color(229, 115, 115));

        if (event.getDragAction() == DnDConstants.ACTION_COPY) {
            cursor = DragSource.DefaultCopyDrop;
        }

        event.startDrag(cursor, new TransferableCircle(panel.circle));
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
        private DropTarget dropTarget;

        private CirclePanel panel;

        public DropTargetListener(CirclePanel panel) {
            this.panel = panel;
            this.dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
        }

        @Override
        public void drop(DropTargetDropEvent event) {
            try {
                Transferable transferable = event.getTransferable();
                Circle circle = (Circle) transferable.getTransferData(dataFlavor);
                if (event.isDataFlavorSupported(dataFlavor)) {
                    event.acceptDrop(DnDConstants.ACTION_COPY);

                    System.out.println("Origem:");
                    System.out.println("Linha: " + circle.getRow());
                    System.out.println("Coluna: " + circle.getColumn());
                    System.out.println("-------------------");
                    System.out.println("Destino:");
                    System.out.println("Linha: " + this.panel.circle.getRow());
                    System.out.println("Coluna: " + this.panel.circle.getColumn());

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
