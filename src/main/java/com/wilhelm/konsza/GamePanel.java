package com.wilhelm.konsza;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    private static final int DELAY = 85;
    private final int[] xCoordinates = new int[GAME_UNITS];
    private final int[] yCoordinates = new int[GAME_UNITS];
    private int bodyParts = 4;
    private int applesEaten;
    private int appleX;
    private int appleY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics graphics) {
        //This method is needed to draw something on JPanel other than drawing the background color
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {

        if (running) {
            // Grid for visualisation
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            //apple
            graphics.setColor(Color.WHITE);
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            //snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(Color.GRAY);
                } else {
                    graphics.setColor(Color.DARK_GRAY);
                }
                graphics.fillRect(xCoordinates[i], yCoordinates[i], UNIT_SIZE, UNIT_SIZE);

            }
        } else
            gameOver(graphics);

    }

    public void newApple() {
        // generates coordinates of new apple
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {
        //shifting the body parts of the snake around

        for (int i = bodyParts; i > 0; i--) {
            //shifting the coordinates over by one spot
            xCoordinates[i] = xCoordinates[i - 1];
            yCoordinates[i] = yCoordinates[i - 1];
        }
        //changing directions of snake
        switch (direction) {
            case 'U' -> yCoordinates[0] -= UNIT_SIZE;
            case 'D' -> yCoordinates[0] += UNIT_SIZE;
            case 'R' -> xCoordinates[0] += UNIT_SIZE;
            case 'L' -> xCoordinates[0] -= UNIT_SIZE;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }

    }

    public void checkApple() {
        if (xCoordinates[0] == appleX && yCoordinates[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        //checks if head collided with body case
        for (int i = bodyParts; i > 0; i--) {
            if (xCoordinates[0] == xCoordinates[i] && yCoordinates[0] == yCoordinates[i]) {
                running = false;
                break;
            }
        }
        //checks if head touches left border
        if (xCoordinates[0] < 0)
            running = false;
        //checks if head touches left border
        if (xCoordinates[0] >= SCREEN_WIDTH)
            running = false;
        //checks if head touches top border
        if (yCoordinates[0] < 0)
            running = false;
        //checks if head touches bottom border
        if (yCoordinates[0] >= SCREEN_HEIGHT)
            running = false;

        if (!running)
            timer.stop();

    }

    public void gameOver(Graphics graphics) {
        //game over text
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Pixeboy", Font.PLAIN, 140));
        //aligning to center
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        //score
        graphics.setFont(new Font("Pixeboy", Font.PLAIN, 40));
        FontMetrics metricsScore = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metricsScore.stringWidth("Score: " + applesEaten)) / 2, SCREEN_HEIGHT / 2 + 40);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R')
                        direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L')
                        direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D')
                        direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U')
                        direction = 'D';
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + e.getKeyCode());
            }
            super.keyPressed(e);
        }
    }


}
