import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {

    private final int SIZE = 320;
    private final int DOTSIZE = 16;     // размер единицы тела змейки в (PX)
    private final int ALL_DOTS = 400;   // максимальное количество единиц которые могут поместиться на игровом поле

    private Image dot;
    private Image apple;

    private int appleX;
    private int appleY;

    // положение в пространстве змейки
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;

    private Timer timer;

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;



    public GameField() {
        setBackground(Color.black);
        loadImges();
        initGame();
        addKeyListener(new FieldKeyBoardListner());
        setFocusable(true);
    }

    public void initGame() {
        this.dots = 3;
        for (int i = 0; i < dots; i++) {
            this.x[i] = 48 - i*DOTSIZE;
            this.y[i] = 48;
        }

        this.timer = new Timer(150,this);
        timer.start();
        createApple();
    }

    public void createApple() {
        this.appleX = new Random().nextInt(20)*DOTSIZE;
        this.appleY = new Random().nextInt(20)*DOTSIZE;
    }

    public void loadImges() {
        ImageIcon imageIconApple = new ImageIcon("apple.png");
        this.apple = imageIconApple.getImage();

        ImageIcon imageIconDot = new ImageIcon("dot.png");
        this.dot = imageIconDot.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.inGame) {
            g.drawImage(this.apple, this.appleX, this.appleY, this);
            for (int i = 0; i < this.dots; i++) {
                g.drawImage(this.dot, this.x[i],this.y[i], this);
            }
        } else {
            String string = "Game Over";
//            Font f = new Font("Arial", 14, Font.BOLD);
            g.setColor(Color.white);
//            g.setFont(f);
            g.drawString(string, 125,SIZE*2);
        }
    }

    public void move() {
        for (int i = this.dots; i > 0; i--) {
            this.x[i] = this.x[i-1];
            this.y[i] = this.y[i-1];
        }
        if (this.left) {
            this.x[0] -= DOTSIZE;
        }
        if (this.right) {
            this.x[0] += DOTSIZE;
        }
        if (this.up) {
            this.y[0] -= DOTSIZE;
        }
        if (down) {
            this.y[0] += DOTSIZE;
        }
    }

    public void checkApple() {
        if ((this.x[0] == this.appleX) && (this.y[0] == this.appleY)) {
            this.dots++;
            createApple();
        }
    }
    public void checkCollisions() {
        for (int i = dots; i > 0 ; i--) {
            if ((i > 4) && (this.x[0] == this.x[i]) && (this.y[0] == this.y[i])) {
                this.inGame = false;
            }
        }

        if (this.x[0] > SIZE) this.inGame = false;
        if (this.x[0] < 0) this.inGame = false;
        if (this.y[0] > SIZE) this.inGame = false;
        if (this.y[0] < 0) this.inGame = false;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    class FieldKeyBoardListner extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_UP && !down) {
                right = false;
                up = true;
                left = false;
            }

            if (key == KeyEvent.VK_DOWN && !up) {
                right = false;
                down = true;
                left = false;
            }
        }
    }
}
