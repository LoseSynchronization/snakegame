package snakegame.util;


import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 存放游戏的一些配置
 * 后期可以实现从外部配置文件中读取配置
 *
 * @author 失去同步
 */
public class GameConfig {
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;
    public static final int LEFT = 4;

    public static final String ROW_NUM = "row.number";
    public static final String COLUMN_NUM = "column.number";
    public static final String SIZE = "block.size";
    public static final String WINDOW_HEIGHT = "window.height";
    public static final String WINDOW_WIDTH = "window.width";
    public static final String SNAKE_LEN = "snake.length";
    public static final String SNAKE_COLOR = "snake.color";
    public static final String FOOD_COLOR = "food.color";
    public static final String THORN_COLOR = "thorn.color";
    public static final String BLOCK_COLOR = "block.color";
    public static final String EYES_COLOR = "eyes.color";
    public static final String LOG_COLOR = "log.color";
    public static final String GAME_LEVELS = "game.levels";
    public static final String GAME_SPEED = "game.speed.levels";
    public static final String GAME_THORN = "game.thorn.levels";
    public static final String EAT_SOUND = "eat.sound.path";
    public static final String CRASH_SOUND = "crash.sound.path";
    public static final String BGM_SOUND = "bgm.path";
    public static final String LEVEL_UP_SOUND = "levelUp.sound.path";
    public static final String FAIL_SOUND = "FAIL.sound.path";
    private static Map<String, Object> configuration;

    static {
        //初始配置(默认配置)
        configuration = new HashMap<>();
        configuration.put(ROW_NUM, 40);
        configuration.put(COLUMN_NUM, 50);
        configuration.put(SIZE, 15);
        configuration.put(SNAKE_LEN, 4);
        configuration.put(SNAKE_COLOR, Color.cyan);
        configuration.put(FOOD_COLOR, Color.green);
        configuration.put(THORN_COLOR, Color.red);
        configuration.put(BLOCK_COLOR, Color.black);
        configuration.put(EYES_COLOR, Color.black);
        configuration.put(LOG_COLOR, Color.black);
        configuration.put(GAME_LEVELS, new int[]{30, 40, 40, 30, 20, 20});
        configuration.put(GAME_SPEED, new int[]{2, 4, 6, 6, 8, 10});
        configuration.put(GAME_THORN, new int[]{10, 20, 40, 60, 80, 100});
        configuration.put(EAT_SOUND, "/snakegame/sound/eat.wav");
        configuration.put(CRASH_SOUND, "/snakegame/sound/crash.wav");
        configuration.put(BGM_SOUND, "/snakegame/sound/bgm.wav");
        configuration.put(LEVEL_UP_SOUND, "/snakegame/sound/levelup_1.wav");
        configuration.put(FAIL_SOUND, "/snakegame/sound/fail.wav");

        File propertyFile = getDefaultPropertyFile();
        if (propertyFile.exists()){
            Map<String, Object> config = readPropertyFile(propertyFile);
            if (config != null && config.size() > 0){
                for (Map.Entry<String, Object> entry : config.entrySet()) {
                    if (entry.getValue() != null){
                        configuration.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        } else {
            createDefaultPropertyFile(configuration, propertyFile);
        }
        configuration.put(WINDOW_WIDTH, ((int) configuration.get(COLUMN_NUM) + 18) * (int) configuration.get(SIZE));
        configuration.put(WINDOW_HEIGHT, ((int) configuration.get(ROW_NUM) + 4) * (int) configuration.get(SIZE));
    }

    public static Object getConfig(String property) {
        return configuration.get(property);
    }

    private static File getDefaultPropertyFile() {
        File file = new File("");
        String propertyFileName = "config.properties";
        return new File(file.getAbsolutePath() + "\\" + propertyFileName);
    }

    private static Map<String, Object> readPropertyFile(File propertyFile) {
        Map<String, Object> config = null;
        try {
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream(propertyFile), StandardCharsets.UTF_8));
            config = new HashMap<>();

            config.put(ROW_NUM, Integer.parseInt(properties.getProperty(ROW_NUM)));
            config.put(COLUMN_NUM, Integer.parseInt(properties.getProperty(COLUMN_NUM)));
            config.put(SIZE, Integer.parseInt(properties.getProperty(SIZE)));
            config.put(SNAKE_LEN, Integer.parseInt(properties.getProperty(SNAKE_LEN)));

            config.put(SNAKE_COLOR, strToColor(properties.getProperty(SNAKE_COLOR)));
            config.put(FOOD_COLOR, strToColor(properties.getProperty(FOOD_COLOR)));
            config.put(THORN_COLOR, strToColor(properties.getProperty(THORN_COLOR)));
            config.put(BLOCK_COLOR, strToColor(properties.getProperty(BLOCK_COLOR)));
            config.put(EYES_COLOR, strToColor(properties.getProperty(EYES_COLOR)));
            config.put(LOG_COLOR, strToColor(properties.getProperty(LOG_COLOR)));


            config.put(GAME_LEVELS, strToInts(properties.getProperty(GAME_LEVELS)));
            config.put(GAME_SPEED, strToInts(properties.getProperty(GAME_SPEED)));
            config.put(GAME_THORN, strToInts(properties.getProperty(GAME_THORN)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    private static void createDefaultPropertyFile(Map<String, Object> configuration, File propertyFile) {
        try {
            boolean create = propertyFile.createNewFile();
            if (create){
                Properties properties = new Properties();
                properties.put(ROW_NUM, configuration.get(ROW_NUM).toString());
                properties.put(COLUMN_NUM, configuration.get(COLUMN_NUM).toString());
                properties.put(SIZE, configuration.get(SIZE).toString());
                properties.put(SNAKE_LEN, configuration.get(SNAKE_LEN).toString());

                properties.put(SNAKE_COLOR, colorToStr((Color) configuration.get(SNAKE_COLOR)));
                properties.put(FOOD_COLOR, colorToStr((Color) configuration.get(FOOD_COLOR)));
                properties.put(THORN_COLOR, colorToStr((Color) configuration.get(THORN_COLOR)));
                properties.put(BLOCK_COLOR, colorToStr((Color) configuration.get(BLOCK_COLOR)));
                properties.put(EYES_COLOR, colorToStr((Color) configuration.get(EYES_COLOR)));
                properties.put(LOG_COLOR, colorToStr((Color) configuration.get(LOG_COLOR)));

                properties.put(GAME_LEVELS, Arrays.toString((int[]) configuration.get(GAME_LEVELS)));
                properties.put(GAME_SPEED, Arrays.toString((int[]) configuration.get(GAME_SPEED)));
                properties.put(GAME_THORN, Arrays.toString((int[]) configuration.get(GAME_THORN)));
                String comments = "you can modify game by changing the following config, the default config now shown";
                properties.store(new OutputStreamWriter(
                        new FileOutputStream(propertyFile), StandardCharsets.UTF_8), comments);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Color strToColor(String str) {
        if (str == null){
            return null;
        }
        str = str.replaceAll("0x", "");
        int value = Integer.parseUnsignedInt(str, 16);
        return new Color(value, value > 0xffffff);
    }

    private static String colorToStr(Color color) {
        if (color == null){
            return null;
        }
        int a = color.getAlpha();
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return String.format("0x%02x%02x%02x%02x", a, r, g, b);
    }

    private static int[] strToInts(String str) {
        if (str == null){
            return null;
        }
        str = str.replace('[', ' ');
        str = str.replace(']', ' ');
        String[] split = str.split(",");
        int[] array = new int[split.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.parseInt(split[i].trim());
        }
        return array;
    }

    public static void main(String[] args) {
        Color color = strToColor("0xFF00FFFF");
        System.out.println("color = " + color);
        System.out.println("Color.cyan = " + Color.cyan);
        System.out.println("colorToStr(Color.cyan) = " + colorToStr(Color.cyan));
    }
}
