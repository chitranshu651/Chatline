package Misc;

import VideoCalling.VideoCallingService1;

public class SessionInfo {

    private static String username;

    private static VideoCallingService1 videocalling;

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
