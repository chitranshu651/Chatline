package Misc;

import VideoCalling.VideoCall1;
import VideoCalling.VideoCallingService1;

public class SessionInfo {

    private static String username;

    private static VideoCallingService1 videocalling;

    public static VideoCall1 getVideoCall1() {
        return videoCall1;
    }

    public static void setVideoCall1(VideoCall1 videoCall1) {
        SessionInfo.videoCall1 = videoCall1;
    }

    private static VideoCall1 videoCall1;

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
}
