package snakegame.util;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.io.IOException;
import java.net.URL;

/**
 * 游戏中的音效
 *
 * @author 失去同步
 */
public class GameSound {
    /**
     * 单次播放声音
     */
    private AudioStream as;
    /**
     * 循环播放声音
     */
    private ContinuousAudioDataStream cas;


    public GameSound(String soundPath) {
        try {
            URL url = this.getClass().getResource(soundPath);
            if (url != null){
                as = new AudioStream(url.openStream());
                // Create AudioData source.
                cas = new ContinuousAudioDataStream(as.getData());
            } else {
                System.out.println("can't find file:" + soundPath);
            }
        } catch (IOException e) {
            System.out.println("soundPath = " + soundPath);
            e.printStackTrace();
        }
    }

    /**
     * 单次播放音乐
     */
    public void start() {
        if (as != null){
            AudioPlayer.player.start(as);
        }
    }

    /**
     * 停止播放音乐
     */
    public void stop() {
        if (as != null){
            AudioPlayer.player.stop(as);
        }
    }

    /**
     * 循环播放 开始
     */
    public void loopStart() {
        if (cas != null){
            AudioPlayer.player.start(cas);
        }
    }

    /**
     * 循环播放 停止
     */
    public void loopStop() {
        if (cas != null){
            AudioPlayer.player.stop(cas);
        }
    }
}