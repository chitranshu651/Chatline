package Misc;

public class SessionInfo {

    private static String username;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SessionInfo.username = username;
    }
}
