import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SlimeFrame extends JFrame
{
    private Screen screen;
    private StartMenu menu;
    private KeyListener keyListener;
    private MouseListener mouseListener;
    
    public SlimeFrame()
    {
        super();
        setSize(Global.WIDTH,Global.HEIGHT);
        
        menu = new StartMenu(this);
        add(menu);
        MenuListener ml = new MenuListener(menu);
        keyListener = ml;
        mouseListener = ml;
        
        addKeyListener(keyListener);
        addMouseListener(mouseListener);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void startGame()
    {
        remove(menu);
        removeKeyListener(keyListener);
        removeMouseListener(mouseListener);
        removeAll();
        
        screen = new Screen(this);
        add(screen);
        revalidate();
        repaint();
        screen.run();
    }
}