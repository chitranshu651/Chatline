package AudioCalling;

import AudioCalling.SockMicClient.src.av.Receiver;

public class audioClientThread extends Thread {

    private String ip;

    public audioClientThread(String ip){
        this.ip = ip;
    }

    public void run(){
        Receiver.RecieverStart(ip);
    }
}
