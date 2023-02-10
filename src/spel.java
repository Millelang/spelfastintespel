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
    private BufferStrategy bs;
    private int lövenX = 100;
    private int lövenY = 100;
    private int lövenVX = 0;
    private int lövenVY = 0;

    private int puckX = 960;
    private int puckY = 540;
    private int puckVX = 0;
    private int puckVY = 0;
    private BufferedImage löven;
    private BufferedImage puck;

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
        setSize(1920,1080);
        JFrame frame = new JFrame();
        frame.add(this);
        frame.addKeyListener(new MyKeyListener());
        this.addMouseMotionListener(new MyMouseMotionListener());
        this.addMouseListener(new MyMouseListener());
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
        g.drawImage(löven,lövenX,lövenY,löven.getWidth(),löven.getHeight(),null);
        g.drawImage(puck,puckX,puckY,puck.getWidth()/10, puck.getHeight()/10, null);


        g.dispose();
        bs.show();
    }

    public void draw(Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
    }

    private void update() {
        lövenX +=lövenVX;
        lövenY +=lövenVY;
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
        double ns = 1000000000.0 / 25.0;
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

    public class MyMouseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    public class MyMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    public class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                lövenVX = -3;
            }
            if (e.getKeyChar() == 'd') {
                lövenVX = 3;
            }
            if (e.getKeyChar() == 'w') {
                lövenVY = -3;
            }
            if (e.getKeyChar() == 's') {
                lövenVY = 3;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                lövenVX = -3;
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
        }
    }
}