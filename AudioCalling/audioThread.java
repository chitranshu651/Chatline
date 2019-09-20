package AudioCalling;

import AudioCalling.SockMicServer.src.av.Radio;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

public class audioThread extends Thread {

    public void run(){
        try {
            new Radio();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
