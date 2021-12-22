import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * MouseAndCheeseMazeView is a graphical user interface on a MouseAndCheeseMaze.
 */
public class MouseAndCheeseMazeView extends JPanel {
    /**
     * The border width that encloses the maze view.
     */
    private static final int BORDER = 5;

    /**
     * Image resource for drawing the "mouse."
     */
    private static BufferedImage MOUSE = null;

    /**
     * Image resource for drawing the "cheese."
     */
    private static BufferedImage CHEESE = null;

    /**
     * The mouse-and-cheese maze that is displayed/controlled by this view.
     */
    private MouseAndCheeseMaze mouseAndCheeseMaze;

    /**
     * The initial mouse x-coordinate held by the mouse-and-cheese maze.
     */
    private int startX;

    /**
     * The initial mouse y-coordinate held by the mouse-and-cheese maze.
     */
    private int startY;

    /**
     * The action that starts the solver process.
     */
    private Action startAction;

    /**
     * The timer which periodically updates the state of the solver.
     */
    private Timer solverTimer;

    /**
     * A label for displaying solver status.
     */
    private JLabel resultLabel;

    /**
     * The maze view used within this view.
     */
    private MazeView mazeView;

    /**
     * Creates the MazeSolverView.
     */
    public MouseAndCheeseMazeView(MouseAndCheeseMaze mouseAndCheeseMaze) {
        this.mouseAndCheeseMaze = mouseAndCheeseMaze;
        startX = this.mouseAndCheeseMaze.getMouse().x;
        startY = this.mouseAndCheeseMaze.getMouse().y;

        startAction = new AbstractAction("Start") {
            /**
             * Implments the Start action.
             *
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             */
            public void actionPerformed(ActionEvent aevt) {
                startSolverTimer();
            }
        };

        /*ActionListener updateMazeActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateViewState();
            }
        }*/

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> updateViewState());

        // TODO: Create an ActionListener that updates the mouse and cheese maze state
        // Hint: the method the listener should call is defined in this class!


        // (Optional) TODO: make the solver speed customizable with a command-line arg
        solverTimer = new Timer(500, e -> updateViewState());

        mazeView = new MazeView(this.mouseAndCheeseMaze.getMaze());
        resultLabel = new JLabel("Click on Start to begin searching.");

        setLayout(new BorderLayout(0, BORDER));
        setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));

        add(mazeView, BorderLayout.CENTER);

        Box b = Box.createHorizontalBox();
        b.add(resultLabel);
        b.add(Box.createHorizontalGlue());
        b.add(new JButton(startAction));
        add(b, BorderLayout.SOUTH);
    }

    /**
     * Starts the solver process by launching a timer that updates the state of
     * the maze at a regular rate.
     */
    private void startSolverTimer() {
        resultLabel.setText("Starting...");
        solverTimer.start();
        startAction.setEnabled(false);
    }

    /**
     * Asks the mouse-and-cheese maze to update its state.
     */
    private void updateViewState() {
        MouseAndCheeseMaze.Result result = mouseAndCheeseMaze.updateMazeState();

        if (result.equals(MouseAndCheeseMaze.Result.STILL_LOOKING)) {
            Point mouse = mouseAndCheeseMaze.getMouse();
            resultLabel.setText("Rat is at (row " + mouse.y + ", column " + mouse.x + ").");
        } else {
            solverTimer.stop();
        }

        if (result.equals(MouseAndCheeseMaze.Result.CHEESE_FOUND)) {
            resultLabel.setText("Cheese found!  :)");
            JOptionPane.showMessageDialog(this,
                    new JLabel("<html><b>The cheese has been found!</b></html>"),
                    "Cheese Found", JOptionPane.INFORMATION_MESSAGE);
        } else if (result.equals(MouseAndCheeseMaze.Result.CHEESE_NOT_FOUND)) {
            resultLabel.setText("Unreachable cheese!  :(");
            JOptionPane.showMessageDialog(this,
                    new JLabel("<html><b>The cheese is unreachable!</b></html>"),
                    "Unreachable Cheese", JOptionPane.ERROR_MESSAGE);
        }

        repaint();
    }

    /**
     * Paints the cells of the maze view.
     *
     * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
     */
    @Override
    protected void paintChildren(Graphics g) {
        // First, paint the view's children as it normally would.
        super.paintChildren(g);

        // Precalculate some key values; namely, the pixel size of each maze cell.
        Maze maze = mouseAndCheeseMaze.getMaze();
        int rawWidth = mazeView.getWidth() / maze.getMazeWidth();
        int rawHeight = mazeView.getHeight() / maze.getMazeHeight();
        int cellWidth = Math.min(rawWidth, rawHeight);
        int cellHeight = cellWidth;
        int itemWidth = cellWidth / 4;
        int itemHeight = cellHeight / 4;

        // Draw the places that the walker has visited.
        boolean[][] beenThere = mouseAndCheeseMaze.getMazeWalker().getBeenThere();
        g.setColor(new Color(255, 0, 0, 128));
        for (int row = 0; row < beenThere.length; row++) {
            for (int column = 0; column < beenThere[row].length; column++) {
                if (beenThere[row][column]) {
                    int centerX = toPixel(column, cellWidth) + (cellWidth / 2);
                    int centerY = toPixel(row, cellHeight) + (cellHeight / 2);
                    g.fillOval(centerX - 8, centerY - 8, 16, 16);
                }
            }
        }

        // Draw the path taken by the walker so far.
        MazeWalker.WalkerState[] currentPath = mouseAndCheeseMaze.getMazeWalker().getCurrentPath();
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        int pathX = toPixel(startX, cellWidth) + (cellWidth / 2);
        int pathY = toPixel(startY, cellHeight) + (cellHeight / 2);
        for (MazeWalker.WalkerState walkerState : currentPath) {
            switch (walkerState) {
                case MOVE_LEFT:
                    g2.drawLine(pathX, pathY, pathX - cellWidth, pathY);
                    pathX -= cellWidth;
                    break;

                case MOVE_UP:
                    g2.drawLine(pathX, pathY, pathX, pathY - cellHeight);
                    pathY -= cellHeight;
                    break;

                case MOVE_RIGHT:
                    g2.drawLine(pathX, pathY, pathX + cellWidth, pathY);
                    pathX += cellWidth;
                    break;

                case MOVE_DOWN:
                    g2.drawLine(pathX, pathY, pathX, pathY + cellHeight);
                    pathY += cellHeight;
                    break;

                default:
                    // For all other states, do nothing.
                    break;
            }
        }

        // Next, draw the cheese.
        Point cheese = mouseAndCheeseMaze.getCheese();
        if (cheese != null) {
            int cornerX = toPixel(cheese.x, cellWidth);
            int cornerY = toPixel(cheese.y, cellHeight);
            final int cheeseMargin = 4;

            if (CHEESE != null) {
                g.drawImage(CHEESE, cornerX + cheeseMargin, cornerY + cheeseMargin,
                        cellWidth - cheeseMargin - cheeseMargin,
                        cellHeight - cheeseMargin - cheeseMargin, null);
            } else {
                g.setColor(Color.yellow);
                g.fillArc(cornerX + itemWidth, cornerY + itemHeight,
                        cellWidth / 2, cellHeight / 2, 170, 300);
            }
        }

        // Finally, draw the mouse.
        Point mouse = mouseAndCheeseMaze.getMouse();
        if (mouse != null) {
            int cornerX = toPixel(mouse.x, cellWidth);
            int cornerY = toPixel(mouse.y, cellHeight);
            final int mouseMargin = 4;

            if (MOUSE != null) {
                g.drawImage(MOUSE, cornerX + mouseMargin, cornerY + mouseMargin,
                        cellWidth - mouseMargin - mouseMargin,
                        cellHeight - mouseMargin - mouseMargin, null);
            } else {
                g.setColor(Color.darkGray);
                g.fillOval(cornerX + (cellWidth / 4), cornerY + (cellHeight / 4),
                        itemWidth, itemHeight);
                g.fillOval(cornerX + cellWidth - itemWidth - (cellWidth / 4),
                        cornerY + (cellHeight / 4), itemWidth, itemHeight);
                g.fillOval(cornerX + (cellWidth / 3), cornerY + (cellHeight / 3),
                        cellWidth / 3, cellHeight / 3);
            }
        }
    }

    /**
     * Utility method for converting a maze location into a pixel value within
     * this view.
     */
    private int toPixel(int cell, int dimension) {
        return cell * dimension + BORDER;
    }

    static {
        try {
            MOUSE = ImageIO.read(new File("rat.jpg"));
        } catch (IOException ioe) {
            // Fail quietly: image drawing is not required.
        }

        try {
            CHEESE = ImageIO.read(new File("cheese.jpg"));
        } catch (IOException ioe) {
            // Fail quietly: image drawing is not required.
        }
    }
}
