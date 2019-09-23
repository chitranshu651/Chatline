package AudioCalling.AudioClient;

public class Receiver {

    public static void RecieverStart(String ip) {
        try {
            new Client2(ip).Start();
        } catch (Exception ex) {
            System.out.println("Error in client!!!");
        }
    }
}
