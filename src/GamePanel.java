import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_HEIGHT * SCREEN_WIDTH) /UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void newApple(){
        appleX = UNIT_SIZE * random.nextInt(0,(int)(SCREEN_WIDTH/UNIT_SIZE));
        appleY = UNIT_SIZE * random.nextInt(0,(int)(SCREEN_HEIGHT/UNIT_SIZE));
    }

    public void draw(Graphics g) {
        if(running){
//            for (int i = 0; i < (SCREEN_HEIGHT / UNIT_SIZE); i++) {
//                g.setColor(Color.white);
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(Color.red);
            g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.orange);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.yellow);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score : " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score : " + applesEaten))/2,g.getFont().getSize());
        }else{
            gameOver(g);
        }
    }

    public void move(){
        for (int i = bodyParts; i > 0; i--){
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        //Checks if head collides with snake body
        for(int i = 1; i < bodyParts; i++){
            if ((x[i] == x[0]) && (y[i] == y[0])){
                running = false;
            }
        }
        //Checks if head touches border
//        if (x[0] > SCREEN_WIDTH || x[0] < 0 || y[0] > SCREEN_HEIGHT || y[0] < 0){
//            running = false;
//        }
        if (x[0] >= SCREEN_WIDTH){
            x[0] = 0;
        }
        if (x[0] < 0){
            x[0] = SCREEN_WIDTH;
        }
        if (y[0] >= SCREEN_HEIGHT){
            y[0] = 0;
        }
        if (y[0] < 0){
            y[0] = SCREEN_HEIGHT;
        }


        if (!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score : " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score : " + applesEaten))/2,g.getFont().getSize());

        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);

    }

    public void replay(){
        running = true;
        bodyParts = 6;
        for (int i = 0; i < bodyParts; i++){
            x[i] = 0;
            y[i] = 0;
        }
        applesEaten = 0;
        direction = 'R';
        timer = new Timer(DELAY,this);
        timer.start();
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                        break;
                    }
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                        break;
                    }
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                        break;
                    }
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                        break;
                    }
                case KeyEvent.VK_ENTER:
                    if (!running){
                        replay();
                        break;
                    }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
}
