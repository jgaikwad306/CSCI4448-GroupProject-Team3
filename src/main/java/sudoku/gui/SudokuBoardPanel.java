package sudoku.gui;

import sudoku.model.SudokuGame;
import sudoku.observer.GameEvent;
import sudoku.observer.GameObserver;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Draws the 9x9 board and turns mouse/keyboard input into calls on the model.
 * It never changes its own display state directly; it just repaints whenever
 * the model notifies it.
 */
public class SudokuBoardPanel extends JPanel implements GameObserver {

    private static final int SIZE = SudokuGame.SIZE;
    private static final int BOX = SudokuGame.BOX_SIZE;
    private static final int PREFERRED_CELL = 60;

    private static final Color SELECTED_COLOR  = new Color(187, 222, 251);
    private static final Color HIGHLIGHT_COLOR = new Color(232, 240, 254);
    private static final Color GIVEN_COLOR     = Color.BLACK;
    private static final Color ENTRY_COLOR     = new Color(25, 90, 200);
    private static final Color CONFLICT_COLOR  = new Color(210, 40, 40);

    private final SudokuGame game;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public SudokuBoardPanel(SudokuGame game) {
        this.game = game;
        game.addObserver(this);

        setPreferredSize(new Dimension(PREFERRED_CELL * SIZE + 1, PREFERRED_CELL * SIZE + 1));
        setBackground(Color.WHITE);
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cell = cellSize();
                int col = (e.getX() - offsetX()) / cell;
                int row = (e.getY() - offsetY()) / cell;
                if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
                    selectedRow = row;
                    selectedCol = col;
                    repaint();
                }
                requestFocusInWindow();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e);
            }
        });
    }

    private void handleKey(KeyEvent e) {
        if (selectedRow < 0) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP    -> moveSelection(-1, 0);
            case KeyEvent.VK_DOWN  -> moveSelection(1, 0);
            case KeyEvent.VK_LEFT  -> moveSelection(0, -1);
            case KeyEvent.VK_RIGHT -> moveSelection(0, 1);
            case KeyEvent.VK_BACK_SPACE, KeyEvent.VK_DELETE, KeyEvent.VK_0, KeyEvent.VK_NUMPAD0 ->
                    game.setValue(selectedRow, selectedCol, SudokuGame.EMPTY);
            default -> {
                char c = e.getKeyChar();
                if (c >= '1' && c <= '9') {
                    game.setValue(selectedRow, selectedCol, c - '0');
                }
            }
        }
    }

    private void moveSelection(int dRow, int dCol) {
        selectedRow = Math.floorMod(selectedRow + dRow, SIZE);
        selectedCol = Math.floorMod(selectedCol + dCol, SIZE);
        repaint();
    }

    // --- Observer ---

    @Override
    public void onGameEvent(GameEvent event) {
        // Swing must only be touched on the Event Dispatch Thread
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> onGameEvent(event));
            return;
        }
        if (event.getType() == GameEvent.Type.BOARD_RESET) {
            selectedRow = -1;
            selectedCol = -1;
        }
        repaint();
    }

    // --- Layout helpers (board stays square and centered when resized) ---

    private int cellSize() {
        return Math.max(1, Math.min(getWidth(), getHeight()) / SIZE);
    }

    private int offsetX() {
        return (getWidth() - cellSize() * SIZE) / 2;
    }

    private int offsetY() {
        return (getHeight() - cellSize() * SIZE) / 2;
    }

    // --- Painting ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int cell = cellSize();
        int x0 = offsetX();
        int y0 = offsetY();

        paintHighlights(g2, cell, x0, y0);
        paintNumbers(g2, cell, x0, y0);
        paintGrid(g2, cell, x0, y0);

        g2.dispose();
    }

    private void paintHighlights(Graphics2D g2, int cell, int x0, int y0) {
        if (selectedRow < 0) {
            return;
        }
        int boxRow = selectedRow / BOX;
        int boxCol = selectedCol / BOX;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                boolean sameUnit = r == selectedRow || c == selectedCol
                        || (r / BOX == boxRow && c / BOX == boxCol);
                if (r == selectedRow && c == selectedCol) {
                    g2.setColor(SELECTED_COLOR);
                } else if (sameUnit) {
                    g2.setColor(HIGHLIGHT_COLOR);
                } else {
                    continue;
                }
                g2.fillRect(x0 + c * cell, y0 + r * cell, cell, cell);
            }
        }
    }

    private void paintNumbers(Graphics2D g2, int cell, int x0, int y0) {
        Font givenFont = new Font(Font.SANS_SERIF, Font.BOLD, cell / 2);
        Font entryFont = new Font(Font.SANS_SERIF, Font.PLAIN, cell / 2);

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int value = game.getValue(r, c);
                if (value == SudokuGame.EMPTY) {
                    continue;
                }
                boolean given = game.isGiven(r, c);
                g2.setFont(given ? givenFont : entryFont);
                if (game.hasConflict(r, c)) {
                    g2.setColor(CONFLICT_COLOR);
                } else {
                    g2.setColor(given ? GIVEN_COLOR : ENTRY_COLOR);
                }

                String text = String.valueOf(value);
                FontMetrics fm = g2.getFontMetrics();
                int tx = x0 + c * cell + (cell - fm.stringWidth(text)) / 2;
                int ty = y0 + r * cell + (cell - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, tx, ty);
            }
        }
    }

    private void paintGrid(Graphics2D g2, int cell, int x0, int y0) {
        int boardSize = cell * SIZE;
        g2.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= SIZE; i++) {
            g2.setStroke(new BasicStroke(i % BOX == 0 ? 3f : 1f));
            int offset = i * cell;
            g2.drawLine(x0 + offset, y0, x0 + offset, y0 + boardSize);   // vertical
            g2.drawLine(x0, y0 + offset, x0 + boardSize, y0 + offset);   // horizontal
        }
    }
}