package snakegame.frame;

import snakegame.util.GameConfig;
import snakegame.util.Sound;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * @author 失去同步
 */
public class GameField extends JPanel {
    private int column = (int) GameConfig.getConfig(GameConfig.COLUMN_NUM);
    private int row = (int) GameConfig.getConfig(GameConfig.ROW_NUM);
    private int size = (int) GameConfig.getConfig(GameConfig.SIZE);
    private Color snackColor = (Color) GameConfig.getConfig(GameConfig.SNAKE_COLOR);
    private Color foodColor = (Color) GameConfig.getConfig(GameConfig.FOOD_COLOR);
    private Color thornColor = (Color) GameConfig.getConfig(GameConfig.THORN_COLOR);
    private Color eyesColor = (Color) GameConfig.getConfig(GameConfig.EYES_COLOR);
    private Color blockColor = (Color) GameConfig.getConfig(GameConfig.BLOCK_COLOR);
    private int[] speedLevels = (int[]) GameConfig.getConfig(GameConfig.GAME_SPEED);
    private int[] thornLevels = (int[]) GameConfig.getConfig(GameConfig.GAME_THORN);
    private int[] gameLevels = (int[]) GameConfig.getConfig(GameConfig.GAME_LEVELS);
    private int snakeLength = (int) GameConfig.getConfig(GameConfig.SNAKE_LEN);
    private ArrayList<Point> snake = new ArrayList<>();
    private ArrayList<Point> thorns = new ArrayList<>();
    private int direction;
    private Point foodPosition;
    private boolean autoMove = true;
    private int currentLevel;
    private int maxLevel;
    private double[] usedTime;
    private Date startTime;
    private boolean isStarted;
    private boolean canControl;

    private Sound bgmSound = new Sound((String) GameConfig.getConfig(GameConfig.BGM_SOUND));

    private JTextArea loggerArea;

    @SuppressWarnings("WeakerAccess")
    public GameField() {
        loggerArea = new JTextArea();
        loggerArea.setEditable(false);
        loggerArea.setFocusable(false);
        loggerArea.setFont(new Font(Font.DIALOG, Font.PLAIN, size));
        Color logColor = (Color) GameConfig.getConfig(GameConfig.LOG_COLOR);
        loggerArea.setForeground(logColor);
        loggerArea.setWrapStyleWord(true);
        loggerArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(loggerArea);
        scrollPane.setSize(size * 15, size * (row - 2));
        scrollPane.setLocation(column * size + size, size);
        log("classPath=" + getClass().getResource("").getPath());
        log("file path=" + new File("").getAbsolutePath());

        currentLevel = 0;
        maxLevel = gameLevels.length;
        usedTime = new double[maxLevel];
        gameInit();
        this.setLayout(null);
        this.add(scrollPane);
        this.setSize((int) GameConfig.getConfig(GameConfig.WINDOW_WIDTH),
                (int) GameConfig.getConfig(GameConfig.WINDOW_HEIGHT));
        this.setBackground(Color.WHITE);
        new GameThread().start();
    }

    private void gameInit() {
        bgmSound.loopStop();
        log("game init, current level: " + currentLevel);
        log("current level goal:" + gameLevels[currentLevel]);
        log("current level speed:" + speedLevels[currentLevel]);
        log("current level thorn number:" + thornLevels[currentLevel]);
        isStarted = false;
        setAutoMove(false);
        snakeLength = (int) GameConfig.getConfig(GameConfig.SNAKE_LEN);
        direction = GameConfig.DOWN;
        log("current direction: " + GameConfig.DOWN);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createSnake();
        createFood();
        createThorns();
        bgmSound = new Sound((String) GameConfig.getConfig(GameConfig.BGM_SOUND));
        bgmSound.loopStart();
        canControl = true;
    }

    private void createSnake() {
        log("create snake");
        if (snake.size() > 0){
            snake.clear();
        }
        for (int i = 0; i < snakeLength; i++) {
            snake.add(new Point(1, snakeLength - i - 1));
        }
    }

    private void createThorns() {
        log("create thorns");
        if (thorns.size() > 0){
            thorns.clear();
        }
        int x;
        int y;
        Point point;
        Random random = new Random();
        for (int i = 0; i < thornLevels[currentLevel]; i++) {
            do {
                x = random.nextInt(column);
                y = random.nextInt(row);
                point = new Point(x, y);
            } while (((x == 1 && y <= snakeLength + 5)
                    || foodPosition.equals(point)
                    || thorns.contains(point)
                    || snake.contains(point))
                    && thorns.size() <= row * column - (gameLevels[currentLevel] + snakeLength) * 3);
            log("thorn " + i + ":(" + point.x + "," + point.y + ")");
            thorns.add(point);
        }
    }

    void moveSnake(int direction) {
        Point head = new Point(snake.get(0));
        switch (direction) {
            case GameConfig.UP:
                if (--head.y < 0){
                    head.y = row - 1;
                }
                break;
            case GameConfig.DOWN:
                if (++head.y > row - 1){
                    head.y = 0;
                }
                break;
            case GameConfig.LEFT:
                if (--head.x < 0){
                    head.x = column - 1;
                }
                break;
            case GameConfig.RIGHT:
                if (++head.x > column - 1){
                    head.x = 0;
                }
                break;
            default:
        }
        if (!snake.contains(head) && !thorns.contains(head)){
            snake.add(0, head);
            this.direction = direction;
            eatFood(head);
        } else if (snake.indexOf(head) > 1 || thorns.contains(head)){
            gameover();
        }
        repaint();
    }

    private void levelUp() {
        log("current level goal:" + gameLevels[currentLevel]);
        if (snakeLength >= gameLevels[currentLevel]){
            Sound levelUpSound = new Sound((String) GameConfig.getConfig(GameConfig.LEVEL_UP_SOUND));
            StringBuilder message;
            canControl = false;
            if (++currentLevel >= maxLevel){
                message = new StringBuilder("You WIN!!!\n");
                message.append(getUsedTimeInfo());
                currentLevel = 0;
            } else {
                usedTime[currentLevel - 1] = (System.currentTimeMillis() - startTime.getTime()) / 1000.0;
                message = new StringBuilder(String.format("LEVEL UP! Current LEVEL : %d \n Used Time: %.2f s",
                        currentLevel, usedTime[currentLevel - 1]));
            }
            log(message.toString());
            bgmSound.loopStop();
            levelUpSound.start();
            JOptionPane.showMessageDialog(this, message.toString(),
                    "Congratulate!", JOptionPane.INFORMATION_MESSAGE);
            levelUpSound.stop();
            gameInit();
        }
    }

    private String getUsedTimeInfo() {
        StringBuilder message = new StringBuilder();
        message.append("Level   Time\n");
        for (int i = 0; i < maxLevel; i++) {
            message.append(String.format("%5d : %4.2f\n", i, usedTime[i]));
        }
        return message.toString();
    }

    private void gameover() {
        canControl = false;
        bgmSound.loopStop();
        Sound crashSound = new Sound((String) GameConfig.getConfig(GameConfig.CRASH_SOUND));
        Sound failSound = new Sound((String) GameConfig.getConfig(GameConfig.FAIL_SOUND));
        try {
            crashSound.start();
            Thread.sleep(200);
            failSound.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringBuilder message;
        message = new StringBuilder("GAME OVER\n");
        if (currentLevel > 0){
            message.append(getUsedTimeInfo());
        }
        log(message.toString());
        JOptionPane.showMessageDialog(this, message,
                "GAME OVER", JOptionPane.ERROR_MESSAGE);
        currentLevel = 0;
        crashSound.stop();
        failSound.stop();
        gameInit();
    }

    private void eatFood(Point head) {
        if (head.equals(foodPosition)){
            ++snakeLength;
            Sound eatSound = new Sound((String) GameConfig.getConfig(GameConfig.EAT_SOUND));
            eatSound.start();
            log("eat food, current length:" + snakeLength);
            createFood();
            levelUp();
        } else {
            snake.remove(snakeLength);
        }
    }

    private void createFood() {
        log("create food");
        int x;
        int y;
        Point point;
        Random random = new Random();
        do {
            x = random.nextInt(column);
            y = random.nextInt(row);
            point = new Point(x, y);
        } while (snake.contains(point) || thorns.contains(point));
        log("food :(" + point.x + "," + point.y + ")");
        foodPosition = point;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintFood(g);
        paintSnake(g);
        paintThorn(g);
        paintBlock(g);
        printTime(g);
    }

    private void printTime(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.black);
        Graphics2D d = (Graphics2D) g;
        d.setFont(new Font(Font.DIALOG, Font.BOLD, size));
        double usedTime = 0;
        if (isStarted){
            usedTime = (System.currentTimeMillis() - startTime.getTime()) / 1000.0;
        }
        String s = String.format("Used Time: %.1f s", usedTime);
        d.drawString(s, column * size + size, row * size);
        g.setColor(c);
    }

    private void paintSnake(Graphics g) {
        Color c = g.getColor();
        g.setColor(snackColor);
        for (Point point : snake) {
            g.fillRect(point.x * size, point.y * size, size, size);
        }
        g.setColor(eyesColor);
        Point head = snake.get(0);
        switch (direction) {
            case GameConfig.UP:
                g.fillOval(head.x * size + size / 5, head.y * size + size / 3 * 2, size / 5, size / 3);
                g.fillOval(head.x * size + size / 5 * 3, head.y * size + size / 3 * 2, size / 5, size / 3);
                break;
            case GameConfig.DOWN:
                g.fillOval(head.x * size + size / 5, head.y * size, size / 5, size / 3);
                g.fillOval(head.x * size + size / 5 * 3, head.y * size, size / 5, size / 3);
                break;
            case GameConfig.LEFT:
                g.fillOval(head.x * size + size / 3 * 2, head.y * size + size / 5, size / 3, size / 5);
                g.fillOval(head.x * size + size / 3 * 2, head.y * size + size / 5 * 3, size / 3, size / 5);
                break;
            case GameConfig.RIGHT:
                g.fillOval(head.x * size, head.y * size + size / 5, size / 3, size / 5);
                g.fillOval(head.x * size, head.y * size + size / 5 * 3, size / 3, size / 5);
                break;
            default:
        }
        g.setColor(c);
    }

    private void paintBlock(Graphics g) {
        Color c = g.getColor();
        int x, y;
        g.setColor(blockColor);
        for (int i = 0; i < row; i++) {
            y = i * size;
            for (int j = 0; j < column; j++) {
                x = j * size;
                g.drawRect(x, y, size, size);
            }
        }
        g.setColor(c);
    }

    private void paintThorn(Graphics g) {
        Color c = g.getColor();
        g.setColor(thornColor);
        for (Point point : thorns) {
            g.fillRect(point.x * size, point.y * size, size, size);
        }
        g.setColor(c);
    }

    private void paintFood(Graphics g) {
        g.setColor(foodColor);
        g.fillRect(foodPosition.x * size, foodPosition.y * size, size, size);
    }

    class GameThread extends Thread {
        @Override
        public void run() {
            while (true) {
                if (autoMove){
                    if (!isStarted){
                        startTime = new Date();
                        isStarted = true;
                    }
                    moveSnake(direction);
                }
                repaint();
                try {
                    Thread.sleep(1000 / speedLevels[currentLevel]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void setAutoMove(boolean b) {
        autoMove = b;
    }

    private void log(String log) {
        loggerArea.append(log + "\n");
        loggerArea.setCaretPosition(loggerArea.getText().length());
    }

    public boolean canControl() {
        return canControl;
    }
}
