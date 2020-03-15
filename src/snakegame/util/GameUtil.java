package snakegame.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 工具类
 *
 * @author 失去同步
 */
public class GameUtil {

    /**
     * 读取一张图片
     * @param imagePath 图片路径
     * @return image对象
     */
    public static Image getImage(String imagePath) {
        Image image = null;
        File imageFile = new File(imagePath);
        //判断是否为绝对路径
        if (!imageFile.isAbsolute()){
            //不是绝对路径则加上classpath
            imageFile = new File(GameUtil.class.getResource("/").getPath(), imagePath);
        }
        if (imageFile.exists()){
            try {
                image = ImageIO.read(imageFile);
            } catch (IOException e) {
                System.out.println("读取图片" + imageFile.getPath() + "失败!");
                e.printStackTrace();
            }
        } else {
            System.out.println("文件：" + imageFile.getPath() + "不存在！");
        }
        return image;
    }
}
