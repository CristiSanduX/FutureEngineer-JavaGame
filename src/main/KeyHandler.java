package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public KeyHandler(GamePanel gp)
    {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (gp.gameState == gp.titleState)
        {
            if (keyCode == KeyEvent.VK_UP)
            {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0)
                {
                    gp.ui.commandNum = 2;
                }
            }
            if (keyCode == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (keyCode == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0)
                {
                    gp.gameState = gp.playState;
                }
                if (gp.ui.commandNum == 2) {
                    System.exit(0);
                }
            }
        }

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
            upPressed = true;
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
            leftPressed = true;
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
            downPressed = true;
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
            rightPressed = true;

        if (keyCode == KeyEvent.VK_ESCAPE)
        {
            if (gp.gameState == gp.playState)
            {
                gp.gameState = gp.pauseState;
            }
            else if (gp.gameState == gp.pauseState)
            {
                gp.gameState = gp.playState;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
            upPressed = false;
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
            leftPressed = false;
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
            downPressed = false;
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
            rightPressed = false;
    }
}