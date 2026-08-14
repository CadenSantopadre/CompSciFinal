package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
    public void keyTyped(KeyEvent e) {
    }

    public boolean wPressed, sPressed, aPressed, dPressed, upPressed, downPressed, rightPressed, leftPressed, enterPressed, escPressed, mPressed, shiftPressed, spacePressed;

    public void keyPressed(KeyEvent e) {
    
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W) {
            wPressed = true;
        }
        if(code == KeyEvent.VK_S) {
            sPressed = true;
        }
        if(code == KeyEvent.VK_A) {
            aPressed = true;
        }
        if(code == KeyEvent.VK_D) {
            dPressed = true;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE) {
            escPressed = true;
        }
        if(code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if(code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_M) {
            mPressed = true;
        }
        if(code == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
        }
        if(code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W) {
            wPressed = false;
        }
        if(code == KeyEvent.VK_S) {
            sPressed = false;
        }
        if(code == KeyEvent.VK_A) {
            aPressed = false;
        }
        if(code == KeyEvent.VK_D) {
            dPressed = false;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if(code == KeyEvent.VK_ESCAPE) {
            escPressed = false;
        }
        if(code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if(code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_M) {
            mPressed = false;
        }
        if(code == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
        if(code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
    }
}
