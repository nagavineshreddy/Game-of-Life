import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

public class MainPanel extends JPanel
        implements ActionListener, MouseListener, MouseMotionListener, KeyListener, ItemListener{

    int maxWidth = 1700;
    int maxHeight = 650;
    int cellSize = 10;
    int initValue = -1;
    int xSize = maxWidth / cellSize;
    int ySize = maxHeight / cellSize;
    int[][] initGrid = new int[xSize][ySize];
    int[][] finalGrid = new int[xSize][ySize];
    JToggleButton bStart = new JToggleButton("Start");
    JButton bReset = new JButton("Reset");
    Timer time; /* Timer for defining the speed of evolution of the game  */

    public MainPanel() {

        setSize(maxWidth, maxHeight);
        setLayout(new BorderLayout());
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        setBackground(Color.BLACK);
        JPanel panel = new JPanel();
        bStart.setPreferredSize(new Dimension(100, 75));
        bReset.setPreferredSize(new Dimension(100, 75));
        bStart.setBackground(Color.BLUE);
        bStart.setForeground(Color.WHITE);
        bReset.setBackground(Color.BLUE);
        bReset.setForeground(Color.WHITE);
        panel.add(bStart,BorderLayout.WEST);
        panel.add(bReset, BorderLayout.EAST);
        panel.setBackground(Color.gray);
        add(panel, BorderLayout.SOUTH);
        randomValueGrid();

        /**
         * Action Listener for Start/Stop button
         */
        bStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
                boolean selected = abstractButton.getModel().isSelected();

                if (selected) {
                    time.start();
                } else {
                    time.stop();
                }
                repaint();
            }
        });

        /**
         * Item Listener for Start/Stop button
         */
        bStart.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (bStart.isSelected()) {
                    bStart.setText("Stop"); /* If the game is running then change the text to stop */
                } else {
                    bStart.setText("Start"); /* If the game is stopped change the text to start */
                }
            }
        });

        /**
         * Action Listener for Reset Button
         */
        bReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGrid();
                repaint();
            }
        });

        time = new Timer(100, this);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.gray);
        generateGrid(g);
        fillRectanglesColor((g));
    }

    /**
     * Generate the grid.
     */
    public void generateGrid(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < initGrid.length; i++) {
            if (i * cellSize < maxHeight + 10) {
                g.drawLine(0, i * cellSize, maxWidth, i * cellSize);
            }
            g.drawLine(i * cellSize, 0, i * cellSize, maxHeight);

        }
    }

    /**
     * Generate the random 0 and 1 and fill the grid
     */
    public void randomValueGrid() {
        for (int i = 0; i < initGrid.length; i++) {
            for (int j = 0; j < ySize; j++) {
                if ((int) (Math.random() * 5) == 0) {
                    finalGrid[i][j] = 1;
                }
            }
        }
    }

    /**
     * Fill the rectangles with color which has '1' as value
     */
    public void fillRectanglesColor(Graphics g) {
        g.setColor(Color.GREEN);
        duplicateArray();

        for (int i = 0; i < initGrid.length; i++) {
            for (int j = 0; j < ySize; j++) {
                if (initGrid[i][j] == 1) {
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    /**
     * Create duplicate array to re-spawn
     */
    public void duplicateArray() {
        for (int i = 0; i < initGrid.length; i++) {
            for (int j = 0; j < ySize; j++) {
                initGrid[i][j] = finalGrid[i][j];
            }
        }
    }

    /**
     * Counting the neighbors
     */
    private int neighborCount(int x, int y) {
        int count = 0;

        count += initGrid[(x + xSize - 1) % xSize][(y + ySize - 1) % ySize];
        count += initGrid[(x + xSize) % xSize][(y + ySize - 1) % ySize];
        count += initGrid[(x + xSize + 1) % xSize][(y + ySize - 1) % ySize];
        count += initGrid[(x + xSize - 1) % xSize][(y + ySize) % ySize];
        count += initGrid[(x + xSize + 1) % xSize][(y + ySize) % ySize];
        count += initGrid[(x + xSize - 1) % xSize][(y + ySize + 1) % ySize];
        count += initGrid[(x + xSize) % xSize][(y + ySize + 1) % ySize];
        count += initGrid[(x + xSize + 1) % xSize][(y + ySize + 1) % ySize];

        return count;

    }

    /**
     * Clearing the grid
     */
    private void resetGrid() {
        for (int i = 0; i < initGrid.length; i++) {
            for (int j = 0; j < ySize; j++) {
                finalGrid[i][j] = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int count;
        for (int i = 0; i < initGrid.length; i++) {
            for (int j = 0; j < ySize; j++) {
                count = neighborCount(i, j);

                if (count == 3) {
                    finalGrid[i][j] = 1;
                } else if (count == 2 && initGrid[i][j] == 1) {
                    finalGrid[i][j] = 1;
                } else {
                    finalGrid[i][j] = 0;
                }
            }
        }

        repaint();

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Not Used
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not Used
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Not Used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int xPos = e.getX() / cellSize;
        int yPos = e.getY() / cellSize;

        if (initGrid[xPos][yPos] == 0) {
            initValue = 1;
        } else {
            initValue = 0;
        }

        if (initGrid[xPos][yPos] == 0 && initValue == 1) {
            finalGrid[xPos][yPos] = 1;
        } else if (initGrid[xPos][yPos] == 1 && initValue == 0) {
            finalGrid[xPos][yPos] = 0;
        }

        repaint();

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        initValue = -1;

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

}
