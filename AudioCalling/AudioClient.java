package AudioCalling;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;

public class AudioClient {

    TargetDataLine audio_out;

    public static AudioFormat getAudioFormat(){
        float sampleRate = 8000.00F;
        int sampleSizeinBits=16;
        int channel=2;
        boolean signed =true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate,sampleSizeinBits,channel,signed,bigEndian);
    }


}
