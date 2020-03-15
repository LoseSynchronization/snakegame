package snakegame.frame;

import snakegame.util.GameConfig;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;

/**
 * 游戏主窗口
 *
 * @author 失去同步
 */
public class MainFrame {
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("snake");
        GameField gameField = new GameField();
        frame.add(gameField);
        URL url = MainFrame.class.getResource("/snakegame/image/snake_game_icon.png");
        frame.setIconImage(ImageIO.read(url));
        frame.addKeyListener(new MainFrame.GameKeyListener(gameField));

        frame.setSize((int) GameConfig.getConfig(GameConfig.WINDOW_WIDTH),
                (int) GameConfig.getConfig(GameConfig.WINDOW_HEIGHT));
        frame.setLocation(100, 100);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    static class GameKeyListener implements KeyListener {
        private GameField gameField;

        public GameKeyListener(GameField gameField) {
            this.gameField = gameField;
        }

        @Override
        public void keyTyped(KeyEvent e) {
            moveSnake(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            gameField.setAutoMove(false);
            moveSnake(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            gameField.setAutoMove(true);
        }

        private void moveSnake(KeyEvent e) {
            if (!gameField.canControl()){
                return;
            }
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    gameField.moveSnake(GameConfig.UP);
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    gameField.moveSnake(GameConfig.DOWN);
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    gameField.moveSnake(GameConfig.LEFT);
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    gameField.moveSnake(GameConfig.RIGHT);
                    break;
                default:
                    gameField.setAutoMove(true);
                    break;
            }
        }
    }
}
