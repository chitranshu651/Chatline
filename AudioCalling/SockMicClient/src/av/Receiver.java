package AudioCalling.SockMicClient.src.av;
import java.io.IOException;

/**
 * Created by VarunKr on 22-04-2015.
 */
public class Receiver {

    public static void RecieverStart(String ip){
        try{
            new Client2(ip).Start();
        }
        catch(Exception ex){
            System.out.println("Error in client!!!");
        }
    }
}
