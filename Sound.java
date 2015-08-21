import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import javax.swing.JApplet;
public class Sound //extends JApplet
{
    public static AudioClip bclip = Applet.newAudioClip(getLocation("Skyrim.wav"));  //Background music clip
    public static AudioClip sclip;  //Sound effects clip
    
    public static void playBackground()
    {
        bclip.loop();
    }
    public static void pauseBackground()
    {
        try{bclip.wait();} catch(InterruptedException e) {}
    }
    public static void stopBackground()
    {
        bclip.stop();
    }
    public static void soundEffects()
    {
        sclip = Applet.newAudioClip(getLocation("Secret Sound.wav"));
        sclip.play();
    }
    public static URL getLocation(String filename)
    {
        URL url = null;
        try{url = Sound.class.getResource(filename);}
        catch(Exception e){System.out.println("yu are a fuck boi (also the problem is couldn't get the location. Look in getLocation in Sound)");}
        
        return url;
    }
}