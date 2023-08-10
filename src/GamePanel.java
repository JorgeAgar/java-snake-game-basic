import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    final int UNIT_SIZE = 10;
    final int SIDE_UNITS = 50;

    char[][] board = new char[SIDE_UNITS][SIDE_UNITS];

    int snakeX[] = new int[SIDE_UNITS*SIDE_UNITS];
    int snakeY[] = new int[SIDE_UNITS*SIDE_UNITS];

    int appleX;
    int appleY;

    int snakeSize;
    boolean isSnakeAlive;

    char direction;

    Random random;
    Timer timer;

    GamePanel(){
        this.setPreferredSize(new Dimension(SIDE_UNITS*UNIT_SIZE, SIDE_UNITS*UNIT_SIZE));
        this.setBackground(Color.BLACK);
        this.addKeyListener(this);
        this.setFocusable(true);

        random = new Random();
        setupGame();

        timer = new Timer(250, this);
        timer.start();
    }

    void setupGame(){
        //fill board
        for (int i = 0; i < SIDE_UNITS; i++) {
            for (int j = 0; j < SIDE_UNITS; j++) {
                board[i][j] = ' ';
            }
        }

        isSnakeAlive = true;
        snakeSize = 3;
        direction = 'u';
        //spawn snake in the middle
        snakeX[0] = SIDE_UNITS/2;
        snakeY[0] = SIDE_UNITS/2;
        updateBoard();
        nextApple();
        updateBoard();
    }

    void run(){
        System.out.println("snakeSize = " + snakeSize);
        if(checkForApple()){
            snakeSize++;
            move();
            spawnNewHead();
            //updateBoard();
            nextApple();
            updateBoard();
        } else{
            move();
            spawnNewHead();
            updateBoard();
        }
    }

    void move(){
        for (int i = snakeSize-1; i > 0; i++) {
            snakeX[i] = snakeX[i-1];
            snakeY[i] = snakeY[i-1];
        }
    }

    void spawnNewHead(){
        switch(direction){
            case 'u' -> {
                snakeY[0] = snakeY[0]-1;
            }
            case 'd' -> {
                snakeY[0] = snakeY[0]+1;
            }
            case 'l' -> {
                snakeX[0] = snakeX[0]-1;
            }
            case 'r' -> {
                snakeX[0] = snakeX[0]+1;
            }
        }
    }

    void updateBoard(){
        //reset
        for (int i = 0; i < SIDE_UNITS; i++) {
            for (int j = 0; j < SIDE_UNITS; j++) {
                board[i][j] = ' ';
            }
        }
        //update for snake
        for (int i = 0; i < snakeSize; i++){
            board[snakeY[i]][snakeX[i]] = 's';
            System.out.println("Updated " + i + "body Parts");
        }
        //update for apple
        board[appleY][appleX] = 'a';
    }

    boolean checkForApple(){
        switch(direction){
            case 'u'-> {
                if(board[snakeY[0]-1][snakeX[0]] == 'a'){return true;}
            }
            case 'd' -> {
                if(board[snakeY[0]+1][snakeX[0]] == 'a'){return true;}
            }
            case 'l'-> {
                if(board[snakeY[0]][snakeX[0]-1] == 'a'){return true;}
            }
            case 'r'-> {
                if(board[snakeY[0]][snakeX[0]+1] == 'a'){return true;}
            }
        }
        return false;
    }

    void nextApple(){
        boolean repeat = false;
        do{
            appleX = random.nextInt(SIDE_UNITS);
            appleY = random.nextInt(SIDE_UNITS);

            //check if cell already occuppied
            if(board[appleY][appleX] != ' '){
                repeat = true;
            }
        } while(repeat);
    }

    void printBoard(){
        for (int i = 0; i < SIDE_UNITS; i++) {
            for (int j = 0; j < SIDE_UNITS; j++) {
                System.out.print("[" + board[i][j] + "]");
            }
            System.out.println();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < SIDE_UNITS; i++) {
            for (int j = 0; j < SIDE_UNITS; j++) {
                switch(board[i][j]){
                    case ' ' -> {
                        g.setColor(Color.BLACK);
                        g.fillRect(i*UNIT_SIZE, j*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                    case 's' -> {
                        g.setColor(Color.GREEN);
                        g.fillRect(i*UNIT_SIZE, j*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                    case 'a' -> {
                        g.setColor(Color.RED);
                        g.fillOval(i*UNIT_SIZE, j*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isSnakeAlive){run();}
        this.repaint();
        if(snakeSize>10){printBoard();};
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP -> {
                direction = 'l';
            }
            case KeyEvent.VK_DOWN -> {
                direction = 'r';
            }
            case KeyEvent.VK_LEFT -> {
                direction = 'u';
            }
            case KeyEvent.VK_RIGHT -> {
                direction = 'd';
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
