import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class spel extends Canvas implements Runnable{

    private int tid = 30;
    private BufferStrategy bs;
    //private int lövenX = 100;
    //private int lövenY = 100;
    private int lövenVX = 0;
    private int lövenVY = 0;

    private int modoVX;

    private int modoVY;


    private int puckX = 960;
    private int puckY = 540;
    private int puckVX = 0;
    private int puckVY = 0;
    private BufferedImage löven;
    private BufferedImage puck;

    private BufferedImage modo;
    private Rectangle hitbox = new Rectangle(960, 540,120,105);
    private Rectangle lövenbox = new Rectangle(100,100,152,152);

    private Rectangle modobox = new Rectangle(1800,100,152,152);
    private boolean running = false;
    private Thread thread;

    public spel() {
        try {
            löven = ImageIO.read(new File("loven.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            puck = ImageIO.read(new File("puck.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            modo = ImageIO.read(new File("modo.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setSize(1920,1080);
        JFrame frame = new JFrame();
        frame.add(this);
        frame.addKeyListener(new MyKeyListener());
        requestFocus();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void render() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        // Rita ut den nya bilden
        draw(g);
        g.setColor(Color.white);
        //g.fillRect(lövenX,lövenY,löven.getWidth(),löven.getHeight());
        g.drawImage(modo, modobox.x, modobox.y, 152,152,null);
        g.drawImage(löven, lövenbox.x, lövenbox.y, löven.getWidth(),löven.getHeight(),null);
        g.drawImage(puck, hitbox.x, hitbox.y, puck.getWidth()/10, puck.getHeight()/10, null);
        g.dispose();
        bs.show();
    }

    public void draw(Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
    }

    private void update() {

        if (lövenbox.y>hitbox.y+41 && lövenbox.intersects(hitbox)) {
            puckVY = -10;
        }

        if  (hitbox.y>lövenbox.y+40 && lövenbox.intersects(hitbox) && lövenbox.y>hitbox.y+40 && lövenbox.y<hitbox.y+41 && hitbox.y>lövenbox.y+41) {
            puckVX=20;
            puckVY=0;
        }


        if (hitbox.y>lövenbox.y+41 && lövenbox.intersects(hitbox)) {
            puckVY = 10;
        }

        if (lövenbox.intersects(hitbox)) {
            puckVX=20;
        }
        if (modobox.y>hitbox.y+41 && modobox.intersects(hitbox)) {
            puckVY = -10;
        }

        if  (hitbox.y>modobox.y+40 && modobox.intersects(hitbox) && modobox.y>hitbox.y+40) {
            puckY = hitbox.y;
            puckVY=0;

        }


        if (hitbox.y>modobox.y+41 && modobox.intersects(hitbox)) {
            puckVY = 10;
        }

        if (modobox.intersects(hitbox)) {
            puckVX=-20;
        }
        if (hitbox.x >1920) {
            puckVX= 0;
            puckVY= 0;
            hitbox.y = 540;
            hitbox.x = 960;
        }
        if (hitbox.x < 0) {
            puckVX= 0;
            puckVY= 0;
            hitbox.y = 540;
            hitbox.x = 960;
        }
        if (hitbox.y > 1080) {
            puckVY = -10;
            puckVX = puckVX*2;
        }
        if (hitbox.y < 0) {
            puckVY = 10;
            puckVX = puckVX*2;        }
        tid = tid-1;
        if (tid == 0) {tid=30; puckVX=puckVX/2; puckVY=puckVY/2; }
        hitbox.x+=puckVX;
        hitbox.y+=puckVY;
        lövenbox.x +=lövenVX;
        lövenbox.y +=lövenVY;
        modobox.x +=modoVX;
        modobox.y +=modoVY;



    }

    public static void main(String[] args) {
        spel minGrafik = new spel();
        minGrafik.start();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        double ns = 1000000000.0 / 60.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                // Uppdatera koordinaterna
                update();
                // Rita ut bilden med updaterad data
                render();
                delta--;
            }
        }
        stop();
    }
    public class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                lövenVX = -15;
            }
            if (e.getKeyChar() == 'd') {
                lövenVX = 15;
            }
            if (e.getKeyChar() == 'w') {
                lövenVY = -15;
            }
            if (e.getKeyChar() == 's') {
                lövenVY = 15;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                modoVY = 8;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                modoVY = -8;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                modoVX = -8;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                modoVX = 8;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                lövenVX = 0;
            }
            if (e.getKeyChar() == 'd') {
                lövenVX = 0;
            }
            if (e.getKeyChar() == 'w') {
                lövenVY = 0;
            }
            if (e.getKeyChar() == 's') {
                lövenVY = 0;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                modoVX = 0;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                modoVX = 0;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                modoVY = 0;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                modoVY = 0;
            }
        }
    }
}