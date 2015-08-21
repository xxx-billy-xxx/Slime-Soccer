public class Global
{
    public static final int WIDTH = 1120;
    public static final int HEIGHT = 645;
    public static final int minV = 5;
    public static final int maxV = 12;
    
    public static final double FLOOR = 505;
    public static final double PEAK = 420;//385;
    public static final double LEFT_BOUND = 30;
    public static final double RIGHT_BOUND  = 980;
    
    public static final double XDECELERATION_BALL = .6;
    public static final double YDECELERATION_BALL = .6;
    
    public static final double XDECELERATION = .85;
    public static final double XACCELERATION = .35;
    public static final double GRAVITY_GOING_UP = 3;//when going up
    public static final double GRAVITY_GOING_DOWN = .45;//when going down
    
    public static final double MAX_X_VELOCITY = 12;
    public static final double MAX_FALLING_SPEED = 2;
    public static final double JUMP_SPEED = 15;
    
    public static final double MAX_X_VELOCITY_BALL = 30;
    public static final double MAX_Y_VELOCITY_BALL = 30;
    
    
    
    //timestep stuff
    
    //This value would probably be stored elsewhere.
    public static final double GAME_HERTZ = 480;
    //Calculate how many ns each frame should take for our target game hertz.
    public static final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
    //At the very most we will update the game this many times before a new render.
    //If you're worried about visual hitches more than perfect timing, set this to 1.
    public static final int MAX_UPDATES_BEFORE_RENDER = 5;
    //If we are able to get as high as this FPS, don't render again.
    public static final double TARGET_FPS = 1200;
    public static final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
}