package snakegame.util;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.io.IOException;
import java.net.URL;

/*
 * 播放音频文件，产生音效
 */
public class Sound {
    private AudioStream as; //单次播放声音用
    private ContinuousAudioDataStream cas;//循环播放声音

    // 构造函数
    public Sound(String soundPath) {
        try {
            //打开一个声音文件流作为输入
            URL url = getClass().getResource(soundPath);
            if (url != null){
                as = new AudioStream(url.openStream());
            } else {
                System.out.println("error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 一次播放 开始
    public void start() {
        if (as == null){
            System.out.println("AudioStream object is not created!");
            return;
        } else {
            AudioPlayer.player.start(as);
        }
    }

    // 一次播放 停止
    public void stop() {
        if (as == null){
            System.out.println("AudioStream object is not created!");
            return;
        } else {
            AudioPlayer.player.stop(as);
        }
    }

    // 循环播放 开始
    public void loopStart() {
        // Create AudioData source.
        AudioData data = null;
        try {
            data = as.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cas = new ContinuousAudioDataStream(data);
        AudioPlayer.player.start(cas);
    }

    // 循环播放 停止
    public void loopStop() {
        if (cas != null){
            AudioPlayer.player.stop(cas);
        }
    }
}