package av;

import java.io.IOException;

/**
 * Created by VarunKr on 22-04-2015.
 */
public class Receiver {

    public static void RecieverStart(String ip){
        try{
            new av.Client(ip).Start();
        }
        catch(Exception ex){
            System.out.println("Error in client!!!");
        }
    }
}
