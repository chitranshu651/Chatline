package Misc;

import VideoCalling.VideoCall1;
import VideoCalling.VideoCallingService1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SessionInfo {

    private static int AudioPort;

    private static int VideoPort;

    public static int getAudioPort() {
        return AudioPort;
    }

    public static void setAudioPort(int audioPort) {
        AudioPort = audioPort;
    }

    public static int getVideoPort() {
        return VideoPort;
    }

    public static void setVideoPort(int videoPort) {
        VideoPort = videoPort;
    }

    private static String username;

    private static VideoCallingService1 videocalling;

    private static VideoCall1 videoCall1;

    public static VideoCall1 getVideoCall1() {
        return videoCall1;
    }

    public static void setVideoCall1(VideoCall1 videoCall1) {
        SessionInfo.videoCall1 = videoCall1;
    }

    public static VideoCallingService1 getVideocalling() {
        return videocalling;
    }

    public static void setVideocalling(VideoCallingService1 videocalling) {
        SessionInfo.videocalling = videocalling;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SessionInfo.username = username;
    }

    public static BufferedImage BytetoBuff(byte[] img) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(img);
            BufferedImage image = ImageIO.read(bis);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
