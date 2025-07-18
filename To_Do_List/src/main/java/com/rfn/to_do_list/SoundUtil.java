package com.rfn.to_do_list;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundUtil {
    private static MediaPlayer mediaPlayer;

    public static void playAlarm() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            return;
        }

        try {
            URL resource = SoundUtil.class.getResource("/alarm.mp3");
            if (resource != null) {
                Media media = new Media(resource.toString());
                mediaPlayer = new MediaPlayer(media);

                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

                mediaPlayer.play();
            } else {
                System.err.println("Alarm sound file '/alarm.mp3' not found in resources!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopAlarm() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}